package com.eyu.snm.module.fight.service.buff;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import com.eyu.snm.module.fight.model.report.ActionReport;
import com.eyu.snm.module.fight.model.report.EffectReport;
import com.eyu.snm.module.fight.model.report.StageReport;
import com.eyu.snm.module.fight.model.report.UnbuffReport;
import com.eyu.snm.module.fight.service.core.Area;
import com.eyu.snm.module.fight.service.core.Battle;
import com.eyu.snm.module.fight.service.core.Element;
import com.eyu.snm.module.fight.service.core.Fighter;
import com.eyu.snm.module.fight.service.core.Position;
import com.eyu.snm.module.fight.service.core.Unit;
import com.eyu.snm.module.fight.service.op.ActionOperation;
import com.eyu.snm.module.fight.service.op.Operation;
import com.eyu.snm.module.fight.service.op.UnbuffOperation;

/**
 * 战场buff,子弹类型
 * @author shenlong
 */
@Component
public class BulletBuff implements Buff {

	@Override
	public BuffType getType() {
		return BuffType.BULLET_BUFF;
	}

	@Override
	public void add(BuffState state, Unit owner, Unit target, StageReport ret, EffectReport effect, Position... positions) {
		Fighter fighter = owner.getOwner();
		short buffId = fighter.getNextId();
		// 构建战场buff战斗元素
		ABuffElement aBuff = ABuffElement.valueOf(buffId, state, owner, positions);
		Battle battle = fighter.getBattle();
		// 添加移除战场buff op
		battle.addOperation(UnbuffOperation.valueOf(target, battle.getDuration() + state.getTime(), buffId, state, aBuff, battle.getNextOpId()));
		// 添加战场buff初始执行op
		battle.addOperation(ActionOperation.valueOf(aBuff, battle.getDuration() + state.getInitTime(), battle.getNextOpId()));
		battle.getArea().addElement(aBuff);

		// 战报部分
		if (ArrayUtils.isEmpty(positions)) {
			ret.addAreaBuffReport(state.getId(), buffId, null);
		} else {
			ret.addAreaBuffReport(state.getId(), buffId, positions[positions.length - 1]); // 如船长大等要求,技能施放到最后一个目标位置
		}
	}

	@Override
	public ActionReport remove(Unit owner, Battle battle, String baseId, short buffId, Element buff) {
		Area area = battle.getArea();
		area.removeElement(buff);

		UnbuffReport ur = UnbuffReport.valueOf(battle, baseId, buffId, (short) 0);
		// 移除还存在于op队列中的op
		Operation nextOp = buff.getNextOp();
		if (nextOp != null) {
			battle.removeOperation(nextOp);
		}

		return ur;
	}

}
