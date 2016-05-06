package com.edu.game.dota.fight.service.op;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eyu.snm.module.fight.model.report.ActionReport;
import com.eyu.snm.module.fight.model.report.RoundReport;
import com.eyu.snm.module.fight.service.core.Battle;
import com.eyu.snm.module.fight.service.core.Element;
import com.eyu.snm.module.fight.service.core.OperationResult;

/**
 * 手操大招行动
 * @author shenlong
 */
public class UniqueOperation extends TimingOperation {

	private static final Logger logger = LoggerFactory.getLogger(UniqueOperation.class);

	/** 战斗单位id */
	private short unitId;

	public static UniqueOperation valueOf(short unitId, long timing, int id) {
		UniqueOperation op = new UniqueOperation();
		op.unitId = unitId;
		op.timing = timing;
		op.id = id;
		return op;
	}

	@Override
	public Operation execute(Battle battle) {
		OperationResult result = battle.executeUnique(this);
		// 添加行动战报
		ActionReport report = result.getReport();
		if (report != null) {
			if (logger.isDebugEnabled()) {
				logger.debug(report.toString());
			}
			RoundReport last = battle.getReport().lastRoundReport();
			last.addActionReport(report);
		}
		return result.getNext();
	}

	public short getUnitId() {
		return unitId;
	}

	public void setUnitId(short unitId) {
		this.unitId = unitId;
	}

	@Override
	public Element getOwner() {
		return null;
	}

}
