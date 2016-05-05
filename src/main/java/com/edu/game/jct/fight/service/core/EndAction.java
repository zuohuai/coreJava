package com.edu.game.jct.fight.service.core;

import com.edu.game.jct.fight.model.report.BuffReport;
import com.edu.game.jct.fight.model.report.EndReport;
import com.edu.game.jct.fight.model.report.PassiveReport;
import com.edu.game.jct.fight.service.effect.buff.Buff;
import com.edu.game.jct.fight.service.effect.buff.BuffFactory;
import com.edu.game.jct.fight.service.effect.buff.BuffState;
import com.edu.game.jct.fight.service.effect.passive.Passive;
import com.edu.game.jct.fight.service.effect.passive.PassiveFactory;
import com.edu.game.jct.fight.service.effect.passive.PassiveState;
import com.edu.game.jct.fight.service.effect.passive.PassiveType;

/**
 * 回合结束时的行动
 * @author Frank
 */
public class EndAction {
	
	/** 行动所有者 */
	private Unit owner;
	/** 行动的战报 */
	private EndReport report;

	public EndReport execute() {
		
		if(!owner.isDead()) {
			BuffFactory buffFactory = BuffFactory.getInstance();
			// 执行目标身上的全部BUFF效果
			for (BuffState state : owner.getAllBuffStates()) {
				Context ctx = new Context(1);
				Buff buff = buffFactory.getBuff(state.getId());
				BuffReport buffReport = buff.update(state, owner, ctx);
				if (buffReport != null) {
					getReport().addBuff(buffReport);
				}
				ctx.effect();
				if(owner.isDead()) {
					break;
				}
			}
			Context ctx = new Context(1);
			// 执行回合结束阶段的被动效果
			PassiveFactory factory = PassiveFactory.getInstance();
			for (PassiveState state : owner.getPassiveState(Phase.ROUND_END)) {
				Passive passive = factory.getPassive(state.getId());
				PassiveReport passiveReport = passive.execute(state, owner, null, ctx);
				if (passiveReport != null) {
					getReport().addPassive(passiveReport);
				}
			}
			// 将变更数值作用到战斗单位上
			ctx.effect();
			return report;
		} else {
			// 对每个施放目标做独立运算
			Context ctx = new Context(1);
			// 执行复活效果
			PassiveFactory factory = PassiveFactory.getInstance();
			for (PassiveState state : owner.getPassiveState(Phase.ROUND_END)) {
				Passive passive = factory.getPassive(state.getId());
				if(passive.getType() != PassiveType.REVIVE) {
					continue;
				}
				PassiveReport passiveReport = passive.execute(state, owner, null, ctx);
				if (passiveReport != null) {
					getReport().addPassive(passiveReport);
				}
				// 将变更数值作用到战斗单位上
				ctx.effect();
				return report;
			}
			return null;
		}
	}
	
	public EndReport getReport() {
		if (report == null) {
			report = EndReport.valueOf(owner);
		}
		return report;
	}

	/** 构造方法 */
	public static EndAction valueOf(Unit owner) {
		EndAction result = new EndAction();
		result.owner = owner;
		return result;
	}

}
