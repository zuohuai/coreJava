package com.edu.game.dota.fight.service.op;

import java.util.Date;

import com.eyu.common.utils.time.DateUtils;
import com.eyu.snm.module.fight.model.ResultType;
import com.eyu.snm.module.fight.model.report.RoundReport;
import com.eyu.snm.module.fight.service.core.Battle;
import com.eyu.snm.module.fight.service.core.Element;

/**
 * 超时操作,如果该行动被执行则表示战斗已经超时
 * @author shenlong
 */
public class TimeOutOperation extends TimingOperation {

	@Override
	public Operation execute(Battle battle) {
		// 配置战斗超时
		battle.updateTime(this);
		battle.timeOut();
		ResultType result = ResultType.TIME_OUT;
		RoundReport last = battle.getReport().lastRoundReport();
		if (last != null) {
			last.setResult(result);
		}
		battle.getReport().setResult(result);
		return null;
	}

	@Override
	public Element getOwner() {
		return null;
	}

	@Override
	public String toString() {
		return "Timeout[" + DateUtils.date2String(new Date(timing), "mm:ss.SSS") + "]";
	}

	public static TimeOutOperation valueOf(long timing, int id) {
		TimeOutOperation op = new TimeOutOperation();
		op.timing = timing;
		op.id = id;
		return op;
	}

}
