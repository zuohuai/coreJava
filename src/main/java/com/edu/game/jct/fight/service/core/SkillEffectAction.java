package com.edu.game.jct.fight.service.core;

import java.util.List;

import com.edu.game.jct.fight.model.report.EffectReport;
import com.edu.game.jct.fight.model.report.PassiveReport;
import com.edu.game.jct.fight.model.report.TargetReport;
import com.edu.game.jct.fight.service.effect.passive.Passive;
import com.edu.game.jct.fight.service.effect.passive.PassiveFactory;
import com.edu.game.jct.fight.service.effect.passive.PassiveState;
import com.edu.game.jct.fight.service.effect.skill.EffectState;
import com.edu.game.jct.fight.service.effect.skill.SkillEffect;

/**
 * 主动技能行动效果
 * @author Frank
 */
public class SkillEffectAction {

	/** 技能所有者 */
	private Unit owner;
	/** 技能效果 */
	private SkillEffect effect;
	/** 技能状态 */
	private EffectState state;
	/** 友方 */
	private Fighter friend;
	/** 敌方 */
	private Fighter enemy;
	/** 效果战报 */
	private EffectReport report;
	
	/** 执行技能效果 */
	public EffectReport execute() {
		// 选择技能效果的施放目标
		List<Unit> targets = effect.select(state, owner, friend, enemy);
		// 对每个施放目标做独立运算
		Context ctx = new Context(targets.size());
		for (Unit target : targets) {
			TargetReport itemReport = TargetReport.valueOf(target);
			// 进行技能效果计算，并对上下文提供信息
			effect.execute(state, owner, target, ctx, itemReport, this);
			// 执行调整阶段的被动效果(攻方修正)
			PassiveFactory factory = PassiveFactory.getInstance();
			for (PassiveState state : owner.getPassiveState(Phase.ACTION_ADJUST)) {
				Passive passive = factory.getPassive(state.getId());
				PassiveReport passiveReport = passive.execute(state, owner, target, ctx);
				if (passiveReport != null) {
					itemReport.addAdjust(passiveReport);
				}
			}
			// 执行防守阶段的被动效果(守方修正)
			for (PassiveState state : target.getPassiveState(Phase.ACTION_DEFENCE)) {
				Passive passive = factory.getPassive(state.getId());
				PassiveReport passiveReport = passive.execute(state, target, owner, ctx);
				if (passiveReport != null) {
					itemReport.addDefence(passiveReport);
				}
			}
			// 执行行动结束修正(攻方修正)
			for (PassiveState state : owner.getPassiveState(Phase.ACTION_END)) {
				Passive passive = factory.getPassive(state.getId());
				PassiveReport passiveReport = passive.execute(state, owner, target, ctx);
				if (passiveReport != null) {
					itemReport.addEnd(passiveReport);
				}
			}
			// 添加战报
			getReport().addTarget(itemReport);
		}
		// 将变更数值作用到战斗单位上
		ctx.effect();
		return report;
	}

	private EffectReport getReport() {
		if (report == null) {
			report = EffectReport.valueOf(state);
		}
		return report;
	}

	public Fighter getFighter(Unit target) {
		String prefix = target.getId().substring(0, 1);
		String[] array = friend.getId().split(":");
		if (array[1].equals(prefix)) {
			return friend;
		} else {
			return enemy;
		}
	}

	/** 构造方法 */
	public static SkillEffectAction valueOf(Unit owner, SkillEffect effect, EffectState state, Fighter friend,
			Fighter enemy) {
		SkillEffectAction result = new SkillEffectAction();
		result.owner = owner;
		result.effect = effect;
		result.state = state;
		result.friend = friend;
		result.enemy = enemy;
		return result;
	}

}
