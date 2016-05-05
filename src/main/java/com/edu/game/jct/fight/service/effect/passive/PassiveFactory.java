package com.edu.game.jct.fight.service.effect.passive;

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
import com.edu.game.jct.fight.resource.PassiveConfig;
import com.edu.game.resource.Storage;
import com.edu.game.resource.anno.Static;

/**
 * 被动效果工厂
 * @author frank
 */
@Component
public class PassiveFactory implements ApplicationContextAware {
	
	private final static Logger logger = LoggerFactory.getLogger(PassiveFactory.class);
	
	private ApplicationContext applicationContext;

	@Static
	private Storage<String, PassiveConfig> configs;
	
	/** 技能效果实例 */
	private Map<PassiveType, Passive> skills = new HashMap<PassiveType, Passive>();

	/** 初始化方法 */
	@PostConstruct
	protected void init() {
		Map<String, Passive> beans = this.applicationContext.getBeansOfType(Passive.class);
		for (Passive passive : beans.values()) {
			if (skills.put(passive.getType(), passive) != null) {
				FormattingTuple message = MessageFormatter.format("被动效果类型[{}]的实例重复", passive.getType());
				logger.error(message.getMessage());
				throw new FightException(FightExceptionCode.CONFIG_ERROR, message.getMessage());
			}
		}
		PassiveFactory.instance = this;
	}

	/**
	 * 通过配置标识获取对应的回合技能对象实例
	 * @param id 配置标识
	 * @return 不存在会抛异常
	 */
	public Passive getPassive(String id) {
		PassiveConfig config = configs.get(id, false);
		if (config == null) {
			FormattingTuple message = MessageFormatter.format("标识为[{}]被动效果配置不存在", id);
			logger.error(message.getMessage());
			throw new FightException(FightExceptionCode.CONFIG_ERROR, message.getMessage());
		}
		Passive result = skills.get(config.getType());
		if (result == null) {
			FormattingTuple message = MessageFormatter.format("被动效果类型[{}]的实例不存在", config.getType());
			logger.error(message.getMessage());
			throw new FightException(FightExceptionCode.CONFIG_ERROR, message.getMessage());
		}
		return result;
	}
	
	/**
	 * 获取指定技能的初始化状态
	 * @param id 技能标识
	 * @return
	 */
	public PassiveState initState(String id) {
		PassiveConfig config = configs.get(id, false);
		if (config == null) {
			FormattingTuple message = MessageFormatter.format("标识为[{}]被动效果配置不存在", id);
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
	
	private static PassiveFactory instance;
	
	/**
	 * 获取效果工厂实例
	 * @return
	 */
	public static PassiveFactory getInstance() {
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
