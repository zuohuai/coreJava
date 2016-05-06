package com.eyu.snm.module.fight.service.action;

import com.eyu.snm.module.fight.model.report.ActionReport;
import com.eyu.snm.module.fight.service.core.Skill;
import com.eyu.snm.module.fight.service.core.Unit;

/**
 * 死亡技能行动
 * @author shenlong
 */
public class DeadSkillAction implements Action {

	/** 动作所有者 */
	private Unit owner;
	/** 执行的技能对象 */
	private Skill skill;

	@Override
	public ActionReport execute() {
		return skill.execute();
	}

	private DeadSkillAction() {
	}

	public Unit getOwner() {
		return owner;
	}

	public Skill getSkill() {
		return skill;
	}

	public static Action valueOf(Unit unit, Skill skill) {
		DeadSkillAction action = new DeadSkillAction();
		action.owner = unit;
		action.skill = skill;
		return action;
	}

}
