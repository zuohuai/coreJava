package com.eyu.snm.module.fight.service.action;

import com.eyu.snm.module.fight.model.UnitState;
import com.eyu.snm.module.fight.model.report.ActionReport;
import com.eyu.snm.module.fight.service.core.Skill;
import com.eyu.snm.module.fight.service.core.Unit;
import com.eyu.snm.module.fight.service.move.MoveEffect;
import com.eyu.snm.module.fight.service.move.MoveFactory;

/**
 * 必杀行为的实现类
 * @author Frank
 */
public class UniqueAction implements Action {

	/** 动作所有者 */
	private Unit owner;
	/** 执行的技能对象 */
	private Skill unique;

	@Override
	public ActionReport execute() {
		// 如果技能在施放中或允许施放，则施放技能
		if (unique.isInProgress() || unique.isAllow()) {
			// 只在第一阶段修正黑屏时间修正黑屏时间
			if (unique.getStep() == 0) {
				owner.getBattle().updateBlackTime(unique);
			}
			return unique.execute();
		}
		// 如果行动者可以移动，则进行移动
		if (!owner.hasState(UnitState.DISABLE)) {
			MoveEffect effect = MoveFactory.getInstance().getEffect(owner.getMoveType());
			return effect.execute(owner);
		}
		// 无法行动，则返回null
		return null;
	}

	private UniqueAction() {
	}

	public Unit getOwner() {
		return owner;
	}

	public Skill getUnique() {
		return unique;
	}

	public static Action valueOf(Unit unit, Skill unique) {
		UniqueAction action = new UniqueAction();
		action.owner = unit;
		action.unique = unique;
		return action;
	}

}
