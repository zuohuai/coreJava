package com.edu.game.jct.fight.model.report;

import java.util.ArrayList;
import java.util.List;

import com.edu.game.jct.fight.service.core.Unit;

public class EndReport {

	/** 所有者标识 */
	private String owner;
	/** BUFF变更信息 */
	private List<BuffReport> buffs;
	/** 被动效果执行信息 */
	private List<PassiveReport> passives;

	/** 添加BUFF变更信息 */
	public void addBuff(BuffReport report) {
		if (buffs == null) {
			buffs = new ArrayList<BuffReport>();
		}
		buffs.add(report);
	}

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

	public List<BuffReport> getBuffs() {
		return buffs;
	}

	protected void setBuffs(List<BuffReport> buffs) {
		this.buffs = buffs;
	}

	public List<PassiveReport> getPassives() {
		return passives;
	}

	public void setPassives(List<PassiveReport> passives) {
		this.passives = passives;
	}

	/** 构造方法 */
	public static EndReport valueOf(Unit owner) {
		EndReport result = new EndReport();
		result.owner = owner.getId();
		return result;
	}

}
