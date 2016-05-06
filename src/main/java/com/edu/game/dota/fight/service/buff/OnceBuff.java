package com.eyu.snm.module.fight.service.buff;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.eyu.snm.module.fight.model.UnitState;
import com.eyu.snm.module.fight.model.report.ActionReport;
import com.eyu.snm.module.fight.model.report.Alter;
import com.eyu.snm.module.fight.model.report.EffectReport;
import com.eyu.snm.module.fight.model.report.StageReport;
import com.eyu.snm.module.fight.model.report.UnbuffReport;
import com.eyu.snm.module.fight.service.core.Battle;
import com.eyu.snm.module.fight.service.core.Element;
import com.eyu.snm.module.fight.service.core.Fighter;
import com.eyu.snm.module.fight.service.core.Position;
import com.eyu.snm.module.fight.service.core.Unit;
import com.eyu.snm.module.fight.service.op.UnbuffOperation;

/**
 * 一次生效型buff处理器
 * @author shenlong
 */
@Component
public class OnceBuff implements Buff {

	private static final Logger logger = LoggerFactory.getLogger(OnceBuff.class);

	@Override
	public BuffType getType() {
		return BuffType.UNIT_ONCE;
	}

	@Override
	public void add(BuffState state, Unit owner, Unit target, StageReport ret, EffectReport effect, Position... positions) {
		Fighter fighter = target.getFighter();
		short buffId = fighter.getNextId();
		// 给目标添加buff
		target.addBuff(buffId, state);
		Battle battle = fighter.getBattle();
		battle.addOperation(UnbuffOperation.valueOf(target, battle.getDuration() + state.getTime(), buffId, state, null, battle.getNextOpId()));
		// 战报部分
		if (effect == null) {
			logger.error("buff配置表ID[{}]不能是初始化BUFF", state.getId());
		}
		effect.addUnitBuffReport(state.getId(), buffId);
	}

	@Override
	public ActionReport remove(Unit owner, Battle battle, String baseId, short buffId, Element buff) {
		if (owner.hasState(UnitState.DEAD)) {
			return null;
		}
		// 移除buff所带的效果
		List<Alter> alters = owner.removeBuffEffect(buffId);
		UnbuffReport ur = UnbuffReport.valueOf(battle, baseId, buffId, owner.getId());
		ur.addAlters(alters);

		return ur;
	}

}
