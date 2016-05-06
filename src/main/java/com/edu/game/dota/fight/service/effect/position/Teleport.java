package com.eyu.snm.module.fight.service.effect.position;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.eyu.snm.module.fight.model.report.StageReport;
import com.eyu.snm.module.fight.service.core.Position;
import com.eyu.snm.module.fight.service.core.Unit;
import com.eyu.snm.module.fight.service.effect.Effect;
import com.eyu.snm.module.fight.service.effect.EffectType;

/**
 * 瞬移到敌方目标效果
 * @author Kent
 */
@Component
public class Teleport implements Effect {

	private static final Logger logger = LoggerFactory.getLogger(Teleport.class);

	@Override
	public EffectType getType() {
		return EffectType.TELEPORT;
	}

	@Override
	public void execute(StageReport ret, Map<String, Object> context, Map<String, Object> content, Unit owner, List<Unit> targets) {
		if (targets.size() > 1) {
			logger.error("瞬移效果的释放目标数量[" + targets.size() + "]请检查配置是否正确");
		}
		// 瞬移到地方位置
		Position t = targets.get(0).getPosition();
		Position alter = owner.relocate(t.getX(), t.getY());
		ret.addPositionReport(alter);
	}

}
