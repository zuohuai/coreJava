package com.edu.game.dota.fight.service.action;

import com.edu.game.dota.fight.model.report.ActionReport;
import com.edu.game.dota.fight.service.core.Element;
import com.edu.game.dota.fight.service.core.Skill;

/**
 * 普通buff行为
 * @author shenlong
 */
public class BuffAction implements Action {

	/** 动作所有者 */
	private Element owner;
	/** 执行的技能对象 */
	private Skill skill;

	@Override
	public ActionReport execute() {
		if (skill.isAllow()) {
			return skill.execute();
		}
		return null;
	}

	public Element getOwner() {
		return owner;
	}

	public void setOwner(Element owner) {
		this.owner = owner;
	}

	public Skill getSkill() {
		return skill;
	}

	public void setSkill(Skill skill) {
		this.skill = skill;
	}

	public static Action valueOf(Element owner, Skill skill) {
		BuffAction ret = new BuffAction();
		ret.owner = owner;
		ret.skill = skill;
		return ret;
	}

}
