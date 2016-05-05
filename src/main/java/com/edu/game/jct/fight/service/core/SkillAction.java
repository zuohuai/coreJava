package com.edu.game.jct.fight.service.core;

import com.edu.game.jct.fight.model.UnitValue;
import com.edu.game.jct.fight.model.report.EffectReport;
import com.edu.game.jct.fight.model.report.SkillReport;
import com.edu.game.jct.fight.service.config.BattleType;
import com.edu.game.jct.fight.service.effect.skill.EffectState;
import com.edu.game.jct.fight.service.effect.skill.SkillEffect;
import com.edu.game.jct.fight.service.effect.skill.SkillFactory;
import com.edu.game.jct.fight.service.effect.skill.SkillState;
import com.edu.utils.RandomUtils;

/**
 * 主动技能行动
 * @author frank
 */
public class SkillAction {
	/** 技能发起者 */
	private Unit owner;
	/** 技能的状态 */
	private SkillState state;
	/** 友方 */
	private Fighter friend;
	/** 敌方 */
	private Fighter enemy;
	/** 主动技能行动的战报 */
	private SkillReport report;

	/** 执行行动并返回行动战报 */
	public SkillReport execute(BattleType type) {
		SkillFactory factory = SkillFactory.getInstance();
		// 执行技能效果
		for (EffectState effectState : state.getEffectStates()) {
			SkillEffect effect = factory.getSkillEffect(effectState.getId());
			SkillEffectAction sea = SkillEffectAction.valueOf(owner, effect, effectState, friend, enemy);
			EffectReport ser = sea.execute();
			if (ser != null) {
				getReport().addEffect(ser);
			}
		}

		// 给玩家相关战斗单元添加怒气
		if (report != null) {
			boolean flag = false;
			for (Unit unit : friend.getMajors()) {
				unit.increaseValue(UnitValue.MP, state.getMp() + unit.getValue(UnitValue.MP_ADD));
				flag = true;
			}
			if (flag) {
				report.addMp(state.getMp());
			}
		}
		// 产生冷却时间
		if (state.getRound() > 0 && !RandomUtils.isHit(state.getRate())) {
			state.addCd();
		}
		return report;
	}

	private SkillReport getReport() {
		if (report == null) {
			report = SkillReport.valueOf(owner, state.getId());
		}
		return report;
	}

	// 静态方法

	/** 构造方法 */
	public static SkillAction valueOf(Unit owner, SkillState state, Fighter friend, Fighter enemy) {
		SkillAction result = new SkillAction();
		result.owner = owner;
		result.state = state;
		result.friend = friend;
		result.enemy = enemy;
		return result;
	}

}
