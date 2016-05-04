package com.eyu.ahxy.module.fight.model.report;

import java.util.ArrayList;
import java.util.List;

import com.eyu.ahxy.module.fight.service.core.Unit;
import com.my9yu.common.protocol.annotation.Transable;

/**
 * 回合开始战报
 * @author Frank
 */
@Transable
public class StartReport {

	/** 所有者标识 */
	private String owner;
	/** 被动效果执行信息 */
	private List<PassiveReport> passives;

	/** 添加被动效果信息 */
	public void addPassive(PassiveReport report) {
		if (passives == null) {
			passives = new ArrayList<PassiveReport>();
		}
		passives.add(report);
	}

	// Getter and Setter ....

	public String getOwner() {
		return owner;
	}

	protected void setOwner(String owner) {
		this.owner = owner;
	}

	public List<PassiveReport> getPassives() {
		return passives;
	}

	protected void setPassives(List<PassiveReport> passives) {
		this.passives = passives;
	}

	/** 构造方法 */
	public static StartReport valueOf(Unit owner) {
		StartReport result = new StartReport();
		result.owner = owner.getId();
		return result;
	}

}
