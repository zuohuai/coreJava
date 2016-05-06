package com.eyu.snm.module.fight.service.buff;

import java.util.List;

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
import com.eyu.snm.module.fight.service.op.ActionOperation;
import com.eyu.snm.module.fight.service.op.Operation;
import com.eyu.snm.module.fight.service.op.UnbuffOperation;

/**
 * 间隔生效型buff处理器
 * @author shenlong
 */
@Component
public class IntervalBuff implements Buff {

	@Override
	public BuffType getType() {
		return BuffType.UNIT_INTERVAL;
	}

	@Override
	public void add(BuffState state, Unit owner, Unit target, StageReport ret, EffectReport effect, Position... positions) {
		Fighter fighter = owner.getFighter();
		Battle battle = fighter.getBattle();
		short buffId = fighter.getNextId();
		target.addBuff(buffId, state);
		// 构建战斗单元buff战斗元素
		UBuffElement uBuff = UBuffElement.valueOf(buffId, state, owner, target);
		// 添加移除战斗单元buff op
		battle.addOperation(UnbuffOperation.valueOf(target, battle.getDuration() + state.getTime(), buffId, state, uBuff, battle.getNextOpId()));
		// 添加战斗单元buff 初始执行op
		battle.addOperation(ActionOperation.valueOf(uBuff, battle.getDuration() + state.getInitTime(), battle.getNextOpId()));
		// 战报部分
		effect.addUnitBuffReport(state.getId(), buffId);
	}

	@Override
	public ActionReport remove(Unit owner, Battle battle, String baseId, short buffId, Element buff) {
		// 移除间隔生效型buff元素
		Operation nextOp = buff.getNextOp();
		if (nextOp != null) {
			battle.removeOperation(nextOp);
		}
		// 移除buff所带的效果
		if (owner.hasState(UnitState.DEAD)) {
			return null;
		}
		List<Alter> alters = owner.removeBuffEffect(buffId);
		UnbuffReport ur = UnbuffReport.valueOf(battle, baseId, buffId, owner.getId());
		ur.addAlters(alters);

		return ur;
	}
}
