package com.edu.game.jct.fight.model.report;

import java.util.ArrayList;
import java.util.List;

import com.edu.game.jct.fight.service.core.Unit;
import com.edu.game.jct.fight.service.effect.init.InitEffectState;


/**
 * 初始化效果战报(BATTLE_START 阶段触发的效果)
 * @author Frank
 */
public class InitReport {

	/** 所有者标识 */
	private String owner;
	/** 效果标识 */
	private String id;
	/** 目标的战报信息 */
	private List<InitTargetReport> targets = new ArrayList<InitTargetReport>(1);

	/** 添加目标战报信息 */
	public void addTarget(InitTargetReport report) {
		targets.add(report);
	}

	// Getter and Setter ...

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<InitTargetReport> getTargets() {
		return targets;
	}

	public void setTargets(List<InitTargetReport> targets) {
		this.targets = targets;
	}

	// Static Method's ...

	/** 构造方法 */
	public static InitReport valueOf(Unit unit, InitEffectState state) {
		InitReport result = new InitReport();
		result.owner = unit.getId();
		result.id = state.getId();
		return result;
	}

}
