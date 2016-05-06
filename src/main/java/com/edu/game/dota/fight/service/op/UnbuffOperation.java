package com.edu.game.dota.fight.service.op;

import com.eyu.snm.module.fight.model.report.ActionReport;
import com.eyu.snm.module.fight.model.report.RoundReport;
import com.eyu.snm.module.fight.service.buff.Buff;
import com.eyu.snm.module.fight.service.buff.BuffFactory;
import com.eyu.snm.module.fight.service.buff.BuffState;
import com.eyu.snm.module.fight.service.core.Battle;
import com.eyu.snm.module.fight.service.core.Element;
import com.eyu.snm.module.fight.service.core.Unit;

/**
 * BUFF移除操作
 * @author shenlong
 */
public class UnbuffOperation extends TimingOperation {

	/** BUFF所有者 */
	private Unit owner;
	/** buff标识 */
	private short buffId;
	/** buff基础信息 */
	private BuffState state;
	/** buff战场元素 */
	private Element buffElement;

	@Override
	public Operation execute(Battle battle) {
		battle.updateTime(this);
		BuffFactory facatory = BuffFactory.getInstance();
		Buff buff = facatory.getBuff(state.getId());
		// 移除buff
		ActionReport ret = buff.remove(owner, battle, state.getId(), buffId, buffElement);
		if (ret != null) {
			RoundReport report = battle.getReport().lastRoundReport();
			if (report != null) {
				report.addActionReport(ret);
			}
		}
		battle.removeOperation(this);
		return battle.getSoonOp();
	}

	public static UnbuffOperation valueOf(Unit owner, long timing, short buffId, BuffState state, Element buffElement, int id) {
		UnbuffOperation operation = new UnbuffOperation();
		operation.owner = owner;
		operation.timing = timing;
		operation.buffId = buffId;
		operation.state = state;
		operation.buffElement = buffElement;
		operation.id = id;
		return operation;
	}

	@Override
	public Element getOwner() {
		return owner;
	}

	public short getBuffId() {
		return buffId;
	}

	public void setBuffId(byte buffId) {
		this.buffId = buffId;
	}

	public BuffState getState() {
		return state;
	}

	public void setState(BuffState state) {
		this.state = state;
	}

	public void setTiming(long timing) {
		this.timing = timing;
	}

	public void setOwner(Unit owner) {
		this.owner = owner;
	}

	public Element getBuff() {
		return buffElement;
	}

	public void setBuff(Element buffElement) {
		this.buffElement = buffElement;
	}

}
