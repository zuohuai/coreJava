package com.eyu.snm.module.fight.service.move;

import org.springframework.stereotype.Component;

import com.eyu.snm.module.fight.model.report.ActionReport;
import com.eyu.snm.module.fight.model.report.MoveReport;
import com.eyu.snm.module.fight.service.core.Area;
import com.eyu.snm.module.fight.service.core.Position;
import com.eyu.snm.module.fight.service.core.Unit;

/**
 * 后排寻路规则
 * @author shenlong
 */
@Component
public class Back extends AbstractTargetMove {

	@Override
	public MoveType getType() {
		return MoveType.BACK;
	}

	@Override
	public ActionReport execute(Unit owner) {
		Position p1 = owner.getPosition();

		// 未进场的战斗单位先移动进场
		if (!p1.isValid()) {
			int x = p1.getX();
			if (x == -1) {
				x = 0;
			} else {
				x = Area.X_MAX;
			}
			int y = p1.getY();
			Area area = owner.getOwner().getBattle().getArea();
			area.move(owner, x, y, true);
			return MoveReport.valueOf(owner);
		}

		// 已经进场的战斗单位，选择最近的目标进行移动
		Unit target = findTarget(owner);
		if (target == null) {
			return null;
		}
		// 距离目标3列之内时不移动
		Position p2 = target.getPosition();
		int x = p1.getX();
		if (p2.getX() > x + 2) {
			x++;
		} else if (p2.getX() < x - 2) {
			x--;
		}
		int y = p1.getY();
		// 只在距离目标只有3列时，才进行Y轴调整，以便兼容出场的移动
		if (Math.abs(p1.getX() - x) <= 2) {
			if (p2.getY() > y + 2) {
				y++;
			} else if (p2.getY() < y - 2) {
				y--;
			}
		}
		Area area = owner.getOwner().getBattle().getArea();
		area.move(owner, x, y, true);
		return MoveReport.valueOf(owner);
	}

}
