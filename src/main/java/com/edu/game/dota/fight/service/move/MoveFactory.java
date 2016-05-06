package com.eyu.snm.module.fight.service.move;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 移动效果工厂
 * @author frank
 */
@Component
public class MoveFactory implements ApplicationContextAware {

	private final static Logger logger = LoggerFactory.getLogger(MoveFactory.class);

	private ApplicationContext applicationContext;

	/** 移动效果实例 */
	private Map<MoveType, MoveEffect> effects = new HashMap<MoveType, MoveEffect>();

	/** 初始化方法 */
	@PostConstruct
	protected void init() {
		Map<String, MoveEffect> beans = this.applicationContext.getBeansOfType(MoveEffect.class);
		for (MoveEffect effect : beans.values()) {
			if (effects.put(effect.getType(), effect) != null) {
				FormattingTuple message = MessageFormatter.format("移动效果类型[{}]的实例重复", effect.getType());
				logger.error(message.getMessage());
				throw new RuntimeException(message.getMessage());
			}
		}
		MoveFactory.instance = this;
	}

	/**
	 * 通过配置标识获取对应的对象实例
	 * @param id 配置标识
	 * @return 不存在会抛异常
	 */
	public MoveEffect getEffect(MoveType type) {
		MoveEffect result = effects.get(type);
		if (result == null) {
			FormattingTuple message = MessageFormatter.format("移动效果类型[{}]的实例不存在", type);
			logger.error(message.getMessage());
			throw new RuntimeException(message.getMessage());
		}
		return result;
	}

	// 其它方法

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	// Static Method's ...

	private static MoveFactory instance;

	/**
	 * 获取效果工厂实例
	 * @return
	 */
	public static MoveFactory getInstance() {
		if (instance == null) {
			while (true) {
				Thread.yield();
				if (instance != null) {
					break;
				}
			}
		}
		return instance;
	}

}
