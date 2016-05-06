package com.eyu.snm.module.fight.service.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.eyu.common.resource.Storage;
import com.eyu.common.resource.anno.Static;
import com.eyu.snm.module.fight.resource.EffectSetting;
import com.eyu.snm.module.fight.resource.SelectorSetting;
import com.eyu.snm.module.fight.resource.SkillSetting;
import com.eyu.snm.module.fight.resource.SkillStage;
import com.eyu.snm.module.fight.service.effect.Effect;
import com.eyu.snm.module.fight.service.effect.EffectConfig;
import com.eyu.snm.module.fight.service.effect.EffectType;

/**
 * 技能工厂
 * @author Kent
 */
@Component
public class SkillFactory implements ApplicationContextAware {

	@Static
	private Storage<String, SkillSetting> skillStorage;
	@Static
	private Storage<String, EffectSetting> effectStorage;
	@Static
	private Storage<String, SelectorSetting> selectorStorage;

	private Map<EffectType, Effect> effects;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Collection<Effect> values = applicationContext.getBeansOfType(Effect.class).values();
		effects = new HashMap<>(values.size());
		for (Effect effect : values) {
			effects.put(effect.getType(), effect);
		}
		SkillFactory.instance = this;
	}

	/**
	 * 根据技能标识获取技能
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Skill getSkill(String id) {
		SkillSetting skillSetting = skillStorage.get(id, true);
		SkillStage[] stages = skillSetting.getStages();

		List<EffectConfig>[] effects = new ArrayList[stages.length];
		Selector[][] selectorses = new Selector[stages.length][];

		boolean[] canBreaks = new boolean[stages.length]; // 能否打断
		boolean[] canHrs = new boolean[stages.length]; // 能否硬直

		for (int i = 0; i < effects.length; ++i) {
			effects[i] = new ArrayList<>();
			SkillStage skillStage = stages[i];
			// 初始化效果信息
			for (String eid : skillStage.getEffects()) {
				EffectSetting effectSetting = effectStorage.get(eid, true);
				Effect effect = this.effects.get(effectSetting.getType());
				EffectConfig effectConfig = EffectConfig.valueOf(effectSetting, effect);
				effects[i].add(effectConfig);
			}
			// 初始化选择器信息
			String selector = skillStage.getSelector();
			SelectorSetting selectorSetting = selectorStorage.get(selector, true);
			selectorses[i] = selectorSetting.getSelectors();
			canBreaks[i] = skillStage.isCanBreak();
			canHrs[i] = skillStage.isCanHr();
		}

		// 初始化技能信息
		SelectorSetting selectorSetting = selectorStorage.get(skillSetting.getAllow(), true);
		Selector[] allow = selectorSetting.getSelectors();
		Skill skill = Skill.valueOf(id, allow, effects, selectorses, skillSetting.getSkilltype(), canBreaks, canHrs, skillSetting.getBigSkillTime());
		return skill;
	}

	private static SkillFactory instance;

	protected SkillFactory() {
	}

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
