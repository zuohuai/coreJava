package com.eyu.ahxy.module.fight.model.report;

import java.util.ArrayList;
import java.util.List;

import com.eyu.ahxy.module.fight.service.core.Unit;
import com.my9yu.common.protocol.annotation.Transable;

/**
 * 主动技能战报
 * @author Frank
 */
@Transable
public class SkillReport {

	/** 所有者标识 */
	private String owner;
	/** 主动技能标识 */
	private String skill;
	/** 技能效果战报 */
	private List<EffectReport> effects;
	/** 使用技能消耗的MP */
	private int mp;
	
	/** 添加技能效果战报 */
	public void addEffect(EffectReport report) {
		if (effects == null) {
			effects = new ArrayList<EffectReport>(2);
		}
		effects.add(report);
	}

	public void addMp(int mp) {
		this.mp = mp;
	}

	// Getter and Setter ...

	public String getOwner() {
		return owner;
	}

	protected void setOwner(String owner) {
		this.owner = owner;
	}

	public String getSkill() {
		return skill;
	}

	protected void setSkill(String skill) {
		this.skill = skill;
	}
	
	public int getMp() {
		return mp;
	}
	
	protected void setMp(int mp) {
		this.mp = mp;
	}
	
	public List<EffectReport> getEffects() {
		return effects;
	}
	
	protected void setEffects(List<EffectReport> effects) {
		this.effects = effects;
	}

	// Static Method's ...

	/** 构造方法 */
	public static SkillReport valueOf(Unit owner, String skill) {
		SkillReport result = new SkillReport();
		result.owner = owner.getId();
		result.skill = skill;
		return result;
	}

}
