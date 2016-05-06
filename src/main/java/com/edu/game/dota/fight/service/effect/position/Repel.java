package com.eyu.snm.module.fight.service.effect.position;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.eyu.snm.module.fight.model.report.EffectReport;
import com.eyu.snm.module.fight.model.report.StageReport;
import com.eyu.snm.module.fight.service.core.Area;
import com.eyu.snm.module.fight.service.core.Position;
import com.eyu.snm.module.fight.service.core.Unit;
import com.eyu.snm.module.fight.service.effect.Effect;
import com.eyu.snm.module.fight.service.effect.EffectType;

/**
 * 击退效果
 * @author Kent
 */
@Component
public class Repel implements Effect {

	private static final String DISTANCE = "distance";

	@Override
	public EffectType getType() {
		return EffectType.REPEL;
	}

	@Override
	public void execute(StageReport ret, Map<String, Object> context, Map<String, Object> content, Unit owner, List<Unit> targets) {
		// 击退的距离
		Integer distance = (Integer) content.get(DISTANCE);
		for (int index = 0; index < targets.size(); ++index) {
			Unit target = targets.get(index);
			Position pos = target.getPosition();
			int x = target.isAttacker() ? pos.getX() - distance : pos.getX() + distance;
			// x 修正
			if (x < 0) {
				x = 0;
			} else if (x > Area.X_MAX) {
				x = Area.X_MAX;
			}
			int y = pos.getY();
			// 将目标击退到指定位置
			Position alter = target.relocate(x, y);

			EffectReport effectReport = ret.getEffectReport(target.getId());
			effectReport.addPositionReport(alter);
		}
	}
}
