package com.eyu.snm.module.fight.service.action;

import com.eyu.snm.module.fight.model.UnitState;
import com.eyu.snm.module.fight.model.report.ActionReport;
import com.eyu.snm.module.fight.service.core.Skill;
import com.eyu.snm.module.fight.service.core.Unit;
import com.eyu.snm.module.fight.service.move.MoveEffect;
import com.eyu.snm.module.fight.service.move.MoveFactory;

/**
 * 主动技能动作实现类
 * @author Frank
 */
public class SkillAction implements Action {

	/** 动作所有者 */
	private Unit owner;
	/** 执行的技能对象 */
	private Skill skill;

	@Override
	public ActionReport execute() {
		// 如果技能在施放中或允许施放，则施放技能
		if (skill.isInProgress() || skill.isAllow()) {
			return skill.execute();
		}
		// 如果行动者可以移动，则进行移动
		if (!owner.hasState(UnitState.DISABLE)) {
			MoveEffect effect = MoveFactory.getInstance().getEffect(owner.getMoveType());
			return effect.execute(owner);
		}
		// 无法行动，则返回null
		return null;
	}

	public static Action valueOf(Unit unit, Skill skill) {
		SkillAction ret = new SkillAction();
		ret.owner = unit;
		ret.skill = skill;
		return ret;
	}

	public Skill getSkill() {
		return skill;
	}

}
