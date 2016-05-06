package com.edu.game.dota.fight.model.report;

import io.netty.buffer.ByteBuf;

import java.util.LinkedList;
import java.util.List;

import com.eyu.snm.module.fight.model.ResultType;
import com.eyu.snm.module.fight.resource.IdHolder;
import com.eyu.snm.module.fight.service.core.Fighter;

/**
 * 回合战报
 * @author Frank
 */
public class RoundReport {

	/** 攻击方信息 */
	private FighterInfo attacker;
	/** 防守方信息 */
	private FighterInfo defender;
	/** 动作战报信息 */
	private List<ActionReport> actions;
	/** 回合结果 */
	private ResultType result;

	/** 所属战报 */
	private Report owner;

	/**
	 * 追加行动战报信息
	 * @param action
	 */
	public void addActionReport(ActionReport action) {
		actions.add(action);
	}

	/**
	 * 战报编码
	 * @param buffer
	 */
	public void encode(ByteBuf buffer) {
		// 攻击方
		attacker.encode(buffer);
		// 防守方
		defender.encode(buffer);
		// 行动
		buffer.writeShort(actions.size());
		for (ActionReport action : actions) {
			// 行动类型
			buffer.writeByte(action.getType().ordinal());
			// 行动时间
			buffer.writeInt((int) (action.getTiming() - owner.getTiming()));
			// 动作发起人
			buffer.writeShort(action.getOwner());
			// 行动内容
			action.encode(buffer);
		}
		// 回合结果
		buffer.writeByte(result.ordinal());
	}

	// Getter and Setter ...

	private RoundReport() {
	}

	public FighterInfo getAttacker() {
		return attacker;
	}

	public void setAttacker(FighterInfo attacker) {
		this.attacker = attacker;
	}

	public FighterInfo getDefender() {
		return defender;
	}

	public void setDefender(FighterInfo defender) {
		this.defender = defender;
	}

	public List<ActionReport> getActions() {
		return actions;
	}

	public void setActions(List<ActionReport> actions) {
		this.actions = actions;
	}

	public ResultType getResult() {
		return result;
	}

	public void setResult(ResultType result) {
		this.result = result;
	}

	public static RoundReport valueOf(Fighter attacker, Fighter defender, Report owner, IdHolder idHolder) {
		RoundReport report = new RoundReport();
		report.attacker = FighterInfo.valueOf(attacker, idHolder);
		report.defender = FighterInfo.valueOf(defender, idHolder);
		report.owner = owner;
		report.actions = new LinkedList<>();
		return report;
	}

}
