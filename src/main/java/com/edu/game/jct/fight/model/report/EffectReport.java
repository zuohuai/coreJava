package com.edu.game.jct.fight.model.report;

import java.util.ArrayList;
import java.util.List;

import com.edu.game.jct.fight.service.effect.skill.EffectState;

/**
 * 主动技能效果战报
 * @author Frank
 */
public class EffectReport {

	/** 效果标识 */
	private String id;
	/** 目标的战报信息 */
	private List<TargetReport> targets = new ArrayList<TargetReport>(1);

	/** 添加目标战报信息 */
	public void addTarget(TargetReport report) {
		targets.add(report);
	}
	
	// Getter and Setter ...

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setTargets(List<TargetReport> targets) {
		this.targets = targets;
	}

	public List<TargetReport> getTargets() {
		return targets;
	}

	// 构造方法

	/** 构造方法 */
	public static EffectReport valueOf(EffectState state) {
		EffectReport result = new EffectReport();
		result.id = state.getId();
		return result;
	}

}
