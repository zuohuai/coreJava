package com.eyu.snm.module.fight.service.effect.position;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.eyu.snm.module.fight.model.report.StageReport;
import com.eyu.snm.module.fight.service.core.Position;
import com.eyu.snm.module.fight.service.core.Unit;
import com.eyu.snm.module.fight.service.effect.Effect;
import com.eyu.snm.module.fight.service.effect.EffectType;

/**
 * 朝指定目标移动
 * @author shenlong
 */
@Component
public class TowardsTarget implements Effect {

	@Override
	public EffectType getType() {
		return EffectType.TOWARDS_TARGET;
	}

	@Override
	public void execute(StageReport ret, Map<String, Object> context, Map<String, Object> content, Unit owner, List<Unit> targets) {
		if (targets.size() == 0) {
			return;
		}
		Unit target = targets.get(0);
		Position ownerPosition = owner.getPosition(); // 自身位置
		Position targetPosition = target.getPosition(); // 地方位置
		int x = 0;
		// 朝目标位置移动
		if (ownerPosition.getX() > targetPosition.getX()) {
			x = ownerPosition.getX() - 1;
			Position me = owner.relocate(x, ownerPosition.getY());
			ret.addPositionReport(me);
		} else if (ownerPosition.getX() < targetPosition.getX()) {
			x = ownerPosition.getX() + 1;
			Position me = owner.relocate(x, ownerPosition.getY());
			ret.addPositionReport(me);
		}
	}
}
