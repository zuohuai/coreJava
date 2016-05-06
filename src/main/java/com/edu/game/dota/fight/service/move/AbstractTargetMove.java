package com.edu.game.dota.fight.service.move;

import com.eyu.snm.module.fight.model.UnitState;
import com.eyu.snm.module.fight.service.core.Area;
import com.eyu.snm.module.fight.service.core.Position;
import com.eyu.snm.module.fight.service.core.TargetHelper;
import com.eyu.snm.module.fight.service.core.Unit;

/**
 * 移动寻路
 * @author shenlong
 */
public abstract class AbstractTargetMove implements MoveEffect {

	protected Unit findTarget(Unit owner) {
		Position p = owner.getPosition();
		Area area = owner.getArea();

		// 判断自身身份
		boolean attacker = owner.isAttacker();
		if (owner.hasState(UnitState.CHAOS)) {
			attacker = !attacker;
		}

		int x, y;
		// 获取X轴y轴目标搜寻序列
		x = p.getX();
		if (x < 0) {
			x = 0;
		} else if (x > Area.X_MAX) {
			x = Area.X_MAX;
		}
		Integer[] xs = attacker ? TargetHelper.XSA[x] : TargetHelper.XSD[x];
		Integer[] ys = TargetHelper.YS[p.getY()];
		// 按序列搜寻目标
		for (int i = 0; i < xs.length; i++) {
			x = xs[i];
			for (int j = 0; j < ys.length; j++) {
				y = ys[j];
				for (Unit u : area.getUnits(x, y)) {
					if (u == owner) {
						continue;
					}
					if (attacker && u.isAttacker()) {
						continue;
					}
					if (!attacker && !u.isAttacker()) {
						continue;
					}
					if (u.isDead()) {
						continue;
					}
					return u;
				}
			}
		}
		// 找不到目标
		return null;
	}

}
