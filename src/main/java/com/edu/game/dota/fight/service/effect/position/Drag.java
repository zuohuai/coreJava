package com.eyu.snm.module.fight.service.effect.position;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.eyu.snm.module.fight.model.report.EffectReport;
import com.eyu.snm.module.fight.model.report.StageReport;
import com.eyu.snm.module.fight.service.core.Position;
import com.eyu.snm.module.fight.service.core.Unit;
import com.eyu.snm.module.fight.service.effect.Effect;
import com.eyu.snm.module.fight.service.effect.EffectType;

/**
 * 把目标拖拽到施法者位置
 * @author Kent
 */
@Component
public class Drag implements Effect {

	@Override
	public EffectType getType() {
		return EffectType.DRAG;
	}

	@Override
	public void execute(StageReport ret, Map<String, Object> context, Map<String, Object> content, Unit owner, List<Unit> targets) {
		Position o = owner.getPosition();
		for (int index = 0; index < targets.size(); ++index) {
			Unit target = targets.get(index);
			// 拖拽到自身位置
			Position alter = target.relocate(o.getX(), o.getY());
			EffectReport report = ret.getEffectReport(target.getId());
			report.addPositionReport(alter);
		}
	}

}
