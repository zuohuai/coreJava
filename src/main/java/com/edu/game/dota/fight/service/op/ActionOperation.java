package com.eyu.snm.module.fight.service.op;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eyu.common.utils.time.DateUtils;
import com.eyu.snm.module.fight.model.report.ActionReport;
import com.eyu.snm.module.fight.model.report.RoundReport;
import com.eyu.snm.module.fight.service.core.Battle;
import com.eyu.snm.module.fight.service.core.Element;
import com.eyu.snm.module.fight.service.core.OperationResult;

/**
 * 行动，表示战斗元素下一个触发行为的时间点。<br/>
 * 到达该时间点战斗元素的具体行为(Action)需要看行动所有者(owner)的冷却时间是否到达，来判定行动是否有效。<br/>
 * 具体的行为(Action)选择，在到达是时间点后再进行运算。
 * @author Frank
 */
public class ActionOperation extends TimingOperation {

	private static final Logger logger = LoggerFactory.getLogger(ActionOperation.class);

	/** 行动所有者 */
	private Element owner;

	/** 构造方法 */
	private ActionOperation(Element owner, long timing, int id) {
		this.owner = owner;
		this.timing = timing;
		this.id = id;
	}

	@Override
	public Operation execute(Battle battle) {
		OperationResult ret = battle.execute(this);
		// 添加行动战报
		ActionReport report = ret.getReport();
		if (report != null) {
			if (logger.isDebugEnabled()) {
				logger.debug(report.toString());
			}
			RoundReport last = battle.getReport().lastRoundReport();
			last.addActionReport(report);
		}
		return ret.getNext();
	}

	// Getter and Setter ...

	@Override
	public String toString() {
		return "Action[" + owner.getId() + "," + DateUtils.date2String(new Date(timing), "mm:ss.SSS") + "]";
	}

	public Element getOwner() {
		return owner;
	}

	// Statics...

	/** 构造方法 */
	public static ActionOperation valueOf(Element owner, long timing, int id) {
		return new ActionOperation(owner, timing, id);
	}

}
