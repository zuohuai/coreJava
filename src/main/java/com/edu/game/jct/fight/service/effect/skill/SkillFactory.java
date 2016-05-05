package com.edu.game.jct.fight.service.effect.skill;

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
import com.edu.game.jct.fight.resource.ConditionConfig;
import com.edu.game.jct.fight.resource.SkillConfig;
import com.edu.game.jct.fight.resource.SkillEffectConfig;
import com.edu.game.jct.fight.service.core.Fighter;
import com.edu.game.jct.fight.service.core.Unit;
import com.edu.game.jct.fight.service.effect.skill.condition.ConditionType;
import com.edu.game.jct.fight.service.effect.skill.condition.Decider;
import com.edu.game.resource.Storage;
import com.edu.game.resource.anno.Static;

/**
 * 主动技能工厂
 * @author frank
 */
@Component
public class SkillFactory implements ApplicationContextAware {

	private final static Logger logger = LoggerFactory.getLogger(SkillFactory.class);

	private ApplicationContext applicationContext;

	@Static
	private Storage<String, SkillConfig> skillStorage;
	@Static
	private Storage<String, SkillEffectConfig> effectStorage;
	@Static
	private Storage<String, ConditionConfig> conditionStorage;

	/** 主动技能效果实例 */
	private Map<EffectType, SkillEffect> effects = new HashMap<EffectType, SkillEffect>();
	/** 技能施放条件判断器实例 */
	private Map<ConditionType, Decider> deciders = new HashMap<ConditionType, Decider>();

	/** 初始化方法 */
	@PostConstruct
	protected void init() {
		try {
			// 初始化技能效果实例
			Map<String, SkillEffect> skillBeans = this.applicationContext.getBeansOfType(SkillEffect.class);
			for (SkillEffect skill : skillBeans.values()) {
				if (effects.put(skill.getType(), skill) != null) {
					FormattingTuple message = MessageFormatter.format("主动技能效果类型[{}]的实例重复", skill.getType());
					logger.error(message.getMessage());
					throw new FightException(FightExceptionCode.CONFIG_ERROR, message.getMessage());
				}
			}
			// 初始化技能施放判断条件信息
			Map<String, Decider> deciderBeans = this.applicationContext.getBeansOfType(Decider.class);
			for (Decider decider : deciderBeans.values()) {
				if (deciders.put(decider.getType(), decider) != null) {
					FormattingTuple message = MessageFormatter.format("技能施放条件判断类型[{}]的实例重复", decider.getType());
					logger.error(message.getMessage());
					throw new FightException(FightExceptionCode.CONFIG_ERROR, message.getMessage());
				}
			}
			SkillFactory.instance = this;
		} catch (Exception err) {
			logger.error("SkillFactory error:{}", err);
		}
	}

	/**
	 * 获取指定技能效果标识对应的技能效果实例
	 * @param effectId 技能效果配置标识
	 * @return
	 */
	public SkillEffect getSkillEffect(String effectId) {
		SkillEffectConfig config = effectStorage.get(effectId, true);
		SkillEffect result = effects.get(config.getType());
		if (result == null) {
			FormattingTuple message = MessageFormatter.format("类型为[{}]主动技能效果不存在", config.getType());
			logger.error(message.getMessage());
			throw new FightException(FightExceptionCode.CONFIG_ERROR, message.getMessage());

		}
		return result;
	}

	/**
	 * 获取指定技能效果类型对应的技能效果实例
	 * @param type 效果类型
	 * @return
	 */
	public SkillEffect getSkillEffect(EffectType type) {
		SkillEffect result = effects.get(type);
		if (result == null) {
			FormattingTuple message = MessageFormatter.format("类型为[{}]主动技能效果不存在", type);
			logger.error(message.getMessage());
			throw new FightException(FightExceptionCode.CONFIG_ERROR, message.getMessage());
		}
		return result;
	}

	/**
	 * 检查技能能否施放
	 * @param id 技能标识
	 * @param owner 技能所有者
	 * @param friend 友军
	 * @param enemy 敌军
	 * @return
	 */
	public boolean isAllow(String id, Unit owner, Fighter friend, Fighter enemy) {
		SkillConfig config = skillStorage.get(id, true);
		if (config.getConditions() == null) {
			return true;
		}
		for (String conditionId : config.getConditions()) {
			ConditionConfig conditionConfig = conditionStorage.get(conditionId, true);
			Decider decider = deciders.get(conditionConfig.getType());
			if (decider == null) {
				FormattingTuple message = MessageFormatter.format("技能施放条件判断类型[{}]不存在", conditionConfig.getType());
				logger.error(message.getMessage());
				throw new FightException(FightExceptionCode.CONFIG_ERROR, message.getMessage());
			}
			if (!decider.isAllow(owner, friend, enemy, conditionConfig.getTarget(), conditionConfig.getValue())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 获取指定技能的初始化状态
	 * @param id 技能标识
	 * @return
	 */
	public SkillState initState(String id) {
		SkillConfig config = skillStorage.get(id, false);
		if (config == null) {
			FormattingTuple message = MessageFormatter.format("标识为[{}]主动技能配置不存在", id);
			logger.error(message.getMessage());
			throw new FightException(FightExceptionCode.CONFIG_ERROR, message.getMessage());
		}
		SkillState result = config.getState();
		for (String effectId : config.getEffects()) {
			SkillEffectConfig effectConfig = effectStorage.get(effectId, true);
			result.addEffectState(effectConfig.getState());
		}
		return result;
	}

	// 其它方法

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	// Static Method's ...

	private static volatile SkillFactory instance;

	/**
	 * 获取效果工厂实例
	 * @return
	 */
	public static SkillFactory getInstance() {
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
