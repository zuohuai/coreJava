package com.edu.orm;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import javax.persistence.Version;

import org.apache.commons.lang3.ClassUtils;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;


@SuppressWarnings("rawtypes")
public class HibernateMetadata implements EntityMetadata {

	/** 完整实体名 */
	private String fullName;
	/** 简短实体名 */
	private String shortName;
	/** 实体类 */
	private Class entityClass;
	/** 主键名 */
	private String primaryKey;
	/** 主键类型 */
	private Class primaryKeyClass;
	/** 属性域 */
	private Map<String, String> fields = new HashMap<String, String>();
	/** 索引域名 */
	private Collection<String> indexKeys = new HashSet<String>();
	/** 版本号域名 */
	private String versionKey;

	/** 删除的HQL */
	private String deleteHql;

	/**
	 * 构造方法
	 * 
	 * @param metadata
	 */
	public HibernateMetadata(ClassMetadata metadata) {
		fullName = metadata.getEntityName();
		shortName = fullName.substring(fullName.lastIndexOf('.') + 1);
		try {
			this.entityClass = Class.forName(this.fullName);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("无法加载实体类型:" + this.fullName, e);
		}
		// 获取实体的属性域信息
		ReflectionUtils.doWithFields(entityClass, new FieldCallback() {
			@Override
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
				if (Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers())) {
					return;
				}
				if (field.isAnnotationPresent(Version.class)) {
					versionKey = field.getName();
					return;
				}
				Class<?> type = ClassUtils.primitiveToWrapper(field.getType());
				if (String.class == type) {
					fields.put(field.getName(), type.getName());
				} else if (type.isEnum()) {
					fields.put(field.getName(), type.getName());
				} else if (Collection.class.isAssignableFrom(type) || type.isArray()) {
					fields.put(field.getName(), List.class.getName());
				} else if (Date.class.isAssignableFrom(type)) {
					fields.put(field.getName(), Date.class.getName());
				} else {
					fields.put(field.getName(), Map.class.getName());
				}
				if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(EmbeddedId.class)) {
					primaryKey = field.getName();
					primaryKeyClass = type;
				}
			}
		});
		// 生成实体的删除HQL
		StringBuilder builder = new StringBuilder();
		builder.append("DELETE ").append(shortName).append(" T WHERE T.").append(primaryKey).append("=?");
		deleteHql = builder.toString();
	}

	// Getter and Setter ...

	@Override
	public String getEntityName() {
		return fullName;
	}

	@Override
	public Map<String, String> getFields() {
		return fields;
	}

	@Override
	public String getName() {
		return this.shortName;
	}

	@Override
	public String getPrimaryKey() {
		return this.primaryKey;
	}

	@Override
	public Collection<String> getIndexKeys() {
		return indexKeys;
	}

	@Override
	public String getVersionKey() {
		return this.versionKey;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <PK extends Serializable> Class<PK> getPrimaryKeyClass() {
		return primaryKeyClass;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends IEntity> Class<T> getEntityClass() {
		return entityClass;
	}

	public String getDeleteHql() {
		return deleteHql;
	}

}
