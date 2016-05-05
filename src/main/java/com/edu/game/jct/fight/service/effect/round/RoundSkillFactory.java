package com.edu.game.jct.fight.service.effect.round;

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
import com.edu.game.jct.fight.model.RoundSkillType;
import com.edu.game.jct.fight.resource.RoundSkillConfig;
import com.edu.game.resource.Storage;
import com.edu.game.resource.anno.Static;

/**
 * 回合技能工厂
 * @author frank
 */
@Component
public class RoundSkillFactory implements ApplicationContextAware {
	
	private final static Logger logger = LoggerFactory.getLogger(RoundSkillFactory.class);
	
	private ApplicationContext applicationContext;

	@Static
	private Storage<String, RoundSkillConfig> configs;
	
	/** 技能效果实例 */
	private Map<RoundSkillType, RoundSkill> skills = new HashMap<RoundSkillType, RoundSkill>();

	/** 初始化方法 */
	@PostConstruct
	protected void init() {
		Map<String, RoundSkill> skillBeans = this.applicationContext.getBeansOfType(RoundSkill.class);
		for (RoundSkill skill : skillBeans.values()) {
			if (skills.put(skill.getType(), skill) != null) {
				FormattingTuple message = MessageFormatter.format("回合技能类型[{}]的实例重复", skill.getType());
				logger.error(message.getMessage());
				throw new FightException(FightExceptionCode.CONFIG_ERROR, message.getMessage());
			}
		}
		RoundSkillFactory.instance = this;
	}

	/**
	 * 通过配置标识获取对应的回合技能对象实例
	 * @param configId 配置标识
	 * @return 不存在会抛异常
	 */
	public RoundSkill getRoundSkill(String configId) {
		RoundSkillConfig config = configs.get(configId, false);
		if (config == null) {
			FormattingTuple message = MessageFormatter.format("标识为[{}]回合技能配置不存在", configId);
			logger.error(message.getMessage());
			throw new FightException(FightExceptionCode.CONFIG_ERROR, message.getMessage());
		}
		RoundSkill result = skills.get(config.getType());
		if (result == null) {
			FormattingTuple message = MessageFormatter.format("回合技能类型[{}]的实例不存在", config.getType());
			logger.error(message.getMessage());
			throw new FightException(FightExceptionCode.CONFIG_ERROR, message.getMessage());
		}
		return result;
	}
	
	// 其它方法
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	// Static Method's ...
	
	private static RoundSkillFactory instance;
	
	/**
	 * 获取效果工厂实例
	 * @return
	 */
	public static RoundSkillFactory getInstance() {
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
