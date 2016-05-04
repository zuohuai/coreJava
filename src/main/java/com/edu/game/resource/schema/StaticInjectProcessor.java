package com.edu.game.resource.schema;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import com.edu.game.resource.Storage;
import com.edu.game.resource.StorageManager;
import com.edu.game.resource.anno.Id;
import com.edu.game.resource.anno.Resource;
import com.edu.game.resource.anno.Static;

/**
 * 静态注入处理器，负责完成 {@link Static} 声明的资源的注入工作
 * @author frank
 */
public class StaticInjectProcessor extends InstantiationAwareBeanPostProcessorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(StaticInjectProcessor.class);

	/**
	 * 注入类型定义
	 * @author frank
	 */
	public static enum InjectType {
		/** 存储空间 */
		STORAGE,
		/** 实例 */
		INSTANCE;
	}

	@Autowired
	private StorageManager manager;
	@Autowired
	private ConversionService conversionService;

	@Override
	public boolean postProcessAfterInstantiation(final Object bean, String beanName) throws BeansException {
		ReflectionUtils.doWithFields(bean.getClass(), new FieldCallback() {
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
				Static anno = field.getAnnotation(Static.class);
				if (anno == null) {
					return;
				}
				InjectType type = checkInjectType(field);
				switch (type) {
				case STORAGE:
					injectStorage(bean, field, anno);
					break;
				case INSTANCE:
					injectInstance(bean, field, anno);
					break;
				}
			}
		});
		return super.postProcessAfterInstantiation(bean, beanName);
	}

	/**
	 * 注入静态资源实例
	 * @param bean 被注入对象
	 * @param field 注入属性
	 * @param anno 注入声明
	 */
	private void injectInstance(final Object bean, final Field field, final Static anno) {
		Class<?> clz = field.getType();
		Resource fieldAnno = clz.getAnnotation(Resource.class);
		if (fieldAnno == null) {
			clz = anno.type();
			fieldAnno = clz.getAnnotation(Resource.class);
			if (fieldAnno == null) {
				FormattingTuple message = MessageFormatter.format("属性[{}]的注入资源类型[{}]错误", field, clz);
				logger.debug(message.getMessage());
				throw new RuntimeException(message.getMessage());
			}
		}

		TypeDescriptor sourceType = TypeDescriptor.valueOf(String.class);
		// 获取注入资源主键
		Object key = anno.value();
		TypeDescriptor idType = getIdType(clz);
		try {
			key = conversionService.convert(anno.value(), sourceType, idType);
			// key = JsonUtils.string2Object(anno.value(), idType);
		} catch (Exception e) {
			FormattingTuple message = MessageFormatter.format("属性[{}]的主键转换异常", field);
			logger.debug(message.getMessage());
			throw new RuntimeException(message.getMessage(), e);
		}

		// 添加监听器
		@SuppressWarnings("rawtypes")
		Storage storage = manager.getStorage(clz);
		StaticObserver observer = new StaticObserver(conversionService, bean, field, anno, key, clz);
		storage.addObserver(observer);

		@SuppressWarnings("unchecked")
		final Object instance = storage.get(key, false);
		if (anno.required() && instance == null) {
			FormattingTuple message = MessageFormatter.format("属性[{}]的注入值不存在", field);
			logger.debug(message.getMessage());
			throw new RuntimeException(message.getMessage());
		}

		Class<?> fieldClz = field.getType();
		if (instance == null) {
			// NULL 不注入
		} else if (fieldClz.isInstance(instance)) {
			// 注入实例
			inject(bean, field, instance);
		} else {
			// 注入属性
			final String name = anno.field();
			if (StringUtils.isEmpty(name)) {
				FormattingTuple message = MessageFormatter.format("属性[{}]的注入的字段属性未定义", field);
				logger.debug(message.getMessage());
				throw new RuntimeException(message.getMessage());
			}
			ReflectionUtils.doWithFields(clz, new FieldCallback() {
				@Override
				public void doWith(Field subField) throws IllegalArgumentException, IllegalAccessException {
					if (subField.getName().equals(name)) {
						ReflectionUtils.makeAccessible(subField);
						Object subValue = subField.get(instance);
						if (!field.getType().isInstance(subValue)) {
							try {
								subValue = conversionService.convert(subValue,
										TypeDescriptor.valueOf(subValue.getClass()), new TypeDescriptor(field));
							} catch (Exception e) {
								FormattingTuple message = MessageFormatter.format("属性[{}]的类型转换异常", field);
								logger.debug(message.getMessage());
								throw new RuntimeException(message.getMessage(), e);
							}
						}
						inject(bean, field, subValue);
					}
				}
			});
		}
	}

	/**
	 * 获取唯一标识类型
	 * @param field
	 * @return
	 */
	private TypeDescriptor getIdType(Class<?> clz) {
		Field field = com.edu.utils.ReflectionUtility.getFirstDeclaredFieldWith(clz, Id.class);
		return new TypeDescriptor(field);
	}

	/**
	 * 注入存储空间对象
	 * @param bean 被注入对象
	 * @param field 注入属性
	 * @param anno 注入声明
	 */
	@SuppressWarnings("rawtypes")
	private void injectStorage(Object bean, Field field, Static anno) {
		Type type = field.getGenericType();
		if (!(type instanceof ParameterizedType)) {
			String message = "类型声明不正确";
			logger.debug(message);
			throw new RuntimeException(message);
		}

		Type[] types = ((ParameterizedType) type).getActualTypeArguments();
		if (types[1] instanceof ParameterizedType) {
			types[1] = ((ParameterizedType) types[1]).getRawType();
		}
		if (!(types[1] instanceof Class)) {
			String message = "类型声明不正确";
			logger.debug(message);
			throw new RuntimeException(message);
		}

		Class clz = (Class) types[1];
		Storage storage = manager.getStorage(clz);

		boolean required = anno.required();
		if (required && storage == null) {
			FormattingTuple message = MessageFormatter.format("静态资源类[{}]不存在", clz);
			logger.debug(message.getMessage());
			throw new RuntimeException(message.getMessage());
		}

		inject(bean, field, storage);
	}

	/**
	 * 注入属性值
	 * @param bean
	 * @param field
	 * @param value
	 */
	private void inject(Object bean, Field field, Object value) {
		ReflectionUtils.makeAccessible(field);
		try {
			field.set(bean, value);
		} catch (Exception e) {
			FormattingTuple message = MessageFormatter.format("属性[{}]注入失败", field);
			logger.debug(message.getMessage());
			throw new RuntimeException(message.getMessage());
		}
	}

	/**
	 * 检测注入类型
	 * @param field
	 * @return
	 */
	private InjectType checkInjectType(Field field) {
		if (Storage.class.isAssignableFrom(field.getType())) {
			return InjectType.STORAGE;
		}
		return InjectType.INSTANCE;
	}

}
