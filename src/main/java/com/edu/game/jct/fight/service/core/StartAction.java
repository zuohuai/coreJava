package com.edu.game.jct.fight.service.core;

import java.util.List;

import com.edu.game.jct.fight.model.report.PassiveReport;
import com.edu.game.jct.fight.model.report.StartReport;
import com.edu.game.jct.fight.service.effect.passive.Passive;
import com.edu.game.jct.fight.service.effect.passive.PassiveFactory;
import com.edu.game.jct.fight.service.effect.passive.PassiveState;

/**
 * 回合开始的战斗单位行动
 * @author Frank
 */
public class StartAction {

	/** 行动所有者 */
	private Unit owner;
	/** 需要执行的被动效果 */
	private List<PassiveState> passiveStates;
	/** 行动的战报 */
	private StartReport report;

	public StartReport execute() {
		// 对每个施放目标做独立运算
		Context ctx = new Context(1);
		// 执行回合结束阶段的被动效果
		PassiveFactory factory = PassiveFactory.getInstance();
		for (PassiveState state : passiveStates) {
			Passive passive = factory.getPassive(state.getId());
			PassiveReport passiveReport = passive.execute(state, owner, null, ctx);
			if (passiveReport != null) {
				getReport().addPassive(passiveReport);
			}
		}
		// 将变更数值作用到战斗单位上
		ctx.effect();
		return report;
	}

	private StartReport getReport() {
		if (report == null) {
			report = StartReport.valueOf(owner);
		}
		return report;
	}

	public static StartAction valueOf(Unit owner, List<PassiveState> states) {
		StartAction result = new StartAction();
		result.owner = owner;
		result.passiveStates = states;
		return result;
	}

}
