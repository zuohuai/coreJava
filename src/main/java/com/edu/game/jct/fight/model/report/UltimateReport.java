package com.eyu.ahxy.module.fight.model.report;

import java.util.ArrayList;
import java.util.List;

import com.my9yu.common.protocol.annotation.Transable;

/**
 * 回合技能的战报对象
 * @author Frank
 */
@Transable
public class UltimateReport {

	/** 主动技能标识 */
	private String skill;
	/** 目标的战报信息 */
	private List<TargetReport> targets = new ArrayList<TargetReport>(6);

	// Getter and Setter ...

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public List<TargetReport> getTargets() {
		return targets;
	}

	public void setTargets(List<TargetReport> targets) {
		this.targets = targets;
	}

	// Static
	public static UltimateReport valueOf(String skill, List<TargetReport> targets) {
		UltimateReport report = new UltimateReport();
		report.skill = skill;
		report.targets = targets;
		return report;
	}

}
