package com.eyu.snm.module.fight.service.effect.init;

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
import com.eyu.snm.module.fight.resource.InitSkillSetting;
import com.eyu.snm.module.fight.resource.SelectorSetting;
import com.eyu.snm.module.fight.service.BattleType;
import com.eyu.snm.module.fight.service.core.Fighter;
import com.eyu.snm.module.fight.service.core.TargetHelper;
import com.eyu.snm.module.fight.service.core.Unit;
import com.eyu.snm.module.player.service.Player;

/**
 * 初始化技能执行器
 * @author shenlong
 */
@Component
public class InitSkillEffector implements ApplicationContextAware {

	@Static
	private Storage<String, SelectorSetting> selectorStorage;
	@Static
	private Storage<String, InitSkillSetting> initSkillStorage;

	/** 初始化技能处理器 */
	private Map<InitSkillType, InitSkillProcessor> initSkillProcessors = new HashMap<InitSkillType, InitSkillProcessor>();
	/** 初始化效果 */
	private Map<InitType, InitEffect> initEffects;

	/**
	 * 执行初始化技能
	 * @param fighter
	 * @param round
	 */
	private void initEffect(Fighter fighter, int round) {
		for (String initSkillId : fighter.getEffects()) {
			InitSkillSetting skillSetting = initSkillStorage.get(initSkillId, true);
			if (skillSetting.isOnlyShow()) { // 忽略用于战报显示的技能
				continue;
			}
			for (InitEffectState state : skillSetting.getInitEffectState()) {
				if (!state.isRepeat() && fighter.getWin() > 0) {
					continue;
				}
				SelectorSetting selectorSetting = selectorStorage.get(state.getSelector(), true);
				// 选择目标,执行初始化效果
				List<Unit> targets = TargetHelper.select(fighter, null, selectorSetting.getSelectors());
				if (targets.isEmpty()) {
					continue;
				}
				initEffects.get(state.getType()).execute(state.getCtx(), targets);
			}
		}
	}

	/**
	 * 获取初始化技能列表
	 * @param player
	 * @param battleType
	 * @return
	 */
	private List<String> getInitSkills(Player player, BattleType battleType) {
		List<String> result = new ArrayList<>();
		for (InitSkillProcessor pro : initSkillProcessors.values()) {
			// 获取各类型初始化技能
			List<String> skillList = pro.getFightBuff(player, battleType);
			if (skillList != null) {
				result.addAll(skillList);
			}
		}
		return result;
	}

	/** 单例 */
	private static InitSkillEffector instance;

	/** 对战斗单元生效初始化效果 */
	public static void effect(Fighter fighter, int round) {
		while (instance == null) {
			Thread.yield();
		}
		instance.initEffect(fighter, round);
	}

	/** 用于FighterConverter 获取玩家公共的初始化技能 */
	public static List<String> getPlayerInitSkills(Player player, BattleType battleType) {
		return instance.getInitSkills(player, battleType);
	}

	public static InitSkillEffector getInstance() {
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

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// 初始化处理器
		Collection<InitEffect> values = applicationContext.getBeansOfType(InitEffect.class).values();
		initEffects = new HashMap<>(values.size());
		for (InitEffect effect : values) {
			initEffects.put(effect.getInitType(), effect);
		}
		Collection<InitSkillProcessor> processers = applicationContext.getBeansOfType(InitSkillProcessor.class)
				.values();
		initSkillProcessors = new HashMap<>(processers.size());
		for (InitSkillProcessor pro : processers) {
			initSkillProcessors.put(pro.getInitSkillType(), pro);
		}
		InitSkillEffector.instance = this;
	}

}
