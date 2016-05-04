package com.edu.game.jct.fight.service.effect.init;

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

import com.edu.game.jct.fight.exception.FightException;
import com.edu.game.jct.fight.exception.FightExceptionCode;
import com.edu.game.jct.fight.resource.InitEffectConfig;
import com.edu.game.resource.Storage;
import com.edu.game.resource.anno.Static;

/**
 * 初始化技能工厂
 * @author administrator
 */
@Component
public class InitEffectFactory implements ApplicationContextAware {

	private final static Logger logger = LoggerFactory.getLogger(InitEffectFactory.class);

	private ApplicationContext applicationContext;

	@Static
	private Storage<String, InitEffectConfig> configs;

	/** 初始化技能效果实例 */
	private Map<InitType, InitEffect> effects = new HashMap<InitType, InitEffect>();

	/** 初始化方法 */
	@PostConstruct
	protected void init() {
		Map<String, InitEffect> skillBeans = this.applicationContext.getBeansOfType(InitEffect.class);
		for (InitEffect skill : skillBeans.values()) {
			if (effects.put(skill.getType(), skill) != null) {
				FormattingTuple message = MessageFormatter.format("初始化技能类型[{}]的实例重复", skill.getType());
				logger.error(message.getMessage());
				throw new FightException(FightExceptionCode.CONFIG_ERROR, message.getMessage());
			}
		}
		InitEffectFactory.instance = this;
	}

	/**
	 * 通过配置标识获取对应的初始化效果对象实例
	 * @param configId 配置标识
	 * @return 不存在会抛异常
	 */
	public InitEffect getInitEffect(String configId) {
		InitEffectConfig config = configs.get(configId, false);
		if (config == null) {
			FormattingTuple message = MessageFormatter.format("标识为[{}]初始化技能配置不存在", configId);
			logger.error(message.getMessage());
			throw new FightException(FightExceptionCode.CONFIG_ERROR, message.getMessage());
		}
		InitEffect result = effects.get(config.getType());
		if (result == null) {
			FormattingTuple message = MessageFormatter.format("初始化技能类型[{}]的实例不存在", config.getType());
			logger.error(message.getMessage());
			throw new FightException(FightExceptionCode.CONFIG_ERROR, message.getMessage());
		}
		return result;
	}

	/**
	 * 获取指定初始化效果的状态
	 * @param id 初始化效果标识
	 * @return
	 */
	public InitEffectState initState(String id) {
		InitEffectConfig config = configs.get(id, false);
		if (config == null) {
			FormattingTuple message = MessageFormatter.format("标识为[{}]初始化效果配置不存在", id);
			logger.error(message.getMessage());
			throw new FightException(FightExceptionCode.CONFIG_ERROR, message.getMessage());
		}
		return config.getState();
	}

	// 其它方法

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	// Static Method's ...

	private static InitEffectFactory instance;

	/**
	 * 获取效果工厂实例
	 * @return
	 */
	public static InitEffectFactory getInstance() {
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
