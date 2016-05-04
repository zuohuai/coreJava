package com.edu.game.jct.fight.service.effect.buff;

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
import com.edu.game.jct.fight.resource.BuffConfig;
import com.edu.game.resource.Storage;
import com.edu.game.resource.anno.Static;

/**
 * BUFF效果工厂
 * @author administrator
 */
@Component
public class BuffFactory implements ApplicationContextAware {
	
	private final static Logger logger = LoggerFactory.getLogger(BuffFactory.class);
	
	private ApplicationContext applicationContext;

	@Static
	private Storage<String, BuffConfig> configs;
	
	/** BUFF效果实例 */
	private Map<BuffType, Buff> buffs = new HashMap<BuffType, Buff>();

	/** 初始化方法 */
	@PostConstruct
	protected void init() {
		Map<String, Buff> beans = this.applicationContext.getBeansOfType(Buff.class);
		for (Buff buff : beans.values()) {
			if (buffs.put(buff.getType(), buff) != null) {
				FormattingTuple message = MessageFormatter.format("BUFF效果类型[{}]的实例重复", buff.getType());
				logger.error(message.getMessage());
				throw new FightException(FightExceptionCode.CONFIG_ERROR, message.getMessage());
			}
		}
		BuffFactory.instance = this;
	}

	/**
	 * 通过配置标识获取对应的对象实例
	 * @param id 配置标识
	 * @return 不存在会抛异常
	 */
	public Buff getBuff(String id) {
		BuffConfig config = configs.get(id, false);
		if (config == null) {
			FormattingTuple message = MessageFormatter.format("标识为[{}]BUFF效果配置不存在", id);
			logger.error(message.getMessage());
			throw new FightException(FightExceptionCode.CONFIG_ERROR, message.getMessage());
		}
		Buff result = buffs.get(config.getType());
		if (result == null) {
			FormattingTuple message = MessageFormatter.format("BUFF效果类型[{}]的实例不存在", config.getType());
			logger.error(message.getMessage());
			throw new FightException(FightExceptionCode.CONFIG_ERROR, message.getMessage());
		}
		return result;
	}
	
	/**
	 * 获取指定BUFF的初始化状态
	 * @param id 配置标识
	 * @return
	 */
	public BuffState initState(String id) {
		BuffConfig config = configs.get(id, false);
		if (config == null) {
			FormattingTuple message = MessageFormatter.format("标识为[{}]BUFF效果配置不存在", id);
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
	
	private static BuffFactory instance;
	
	/**
	 * 获取效果工厂实例
	 * @return
	 */
	public static BuffFactory getInstance() {
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
