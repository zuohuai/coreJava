package com.eyu.snm.module.fight.service.buff;

import org.springframework.stereotype.Component;

import com.eyu.snm.module.fight.model.report.ActionReport;
import com.eyu.snm.module.fight.model.report.EffectReport;
import com.eyu.snm.module.fight.model.report.StageReport;
import com.eyu.snm.module.fight.service.core.Battle;
import com.eyu.snm.module.fight.service.core.Element;
import com.eyu.snm.module.fight.service.core.Fighter;
import com.eyu.snm.module.fight.service.core.Position;
import com.eyu.snm.module.fight.service.core.Unit;

/**
 * 永久buff
 * @author shenlong
 */
@Component
public class ForeverBuff implements Buff {

	@Override
	public BuffType getType() {
		return BuffType.UNIT_FOREVER;
	}

	@Override
	public void add(BuffState state, Unit owner, Unit target, StageReport ret, EffectReport effect, Position... positions) {
		Fighter fighter = target.getFighter();
		short buffId = fighter.getNextId();
		state.setForever(true);
		target.addBuff(buffId, state);
		// 战报部分
		if (effect != null) { // 初始化buff 无战报
			effect.addUnitBuffReport(state.getId(), buffId);
		}
	}

	@Override
	public ActionReport remove(Unit owner, Battle battle, String baseId, short buffId, Element buff) {
		return null;
	}

}
