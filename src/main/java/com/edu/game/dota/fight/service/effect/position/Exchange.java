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
 * 互换位置
 * @author Kent
 */
@Component
public class Exchange implements Effect {

	private static final Logger logger = LoggerFactory.getLogger(Exchange.class);

	@Override
	public EffectType getType() {
		return EffectType.EXCHANGE;
	}

	@Override
	public void execute(StageReport ret, Map<String, Object> context, Map<String, Object> content, Unit owner, List<Unit> targets) {
		if (targets.size() > 1) {
			logger.error("互换位置效果的释放目标数量[" + targets.size() + "]请检查配置是否正确");
		}
		
		// 目标位置
		Position t = targets.get(0).getPosition();
		// 自身位置
		Position o = owner.getPosition();
		// 互换位置
		Position me = owner.relocate(t.getX(), t.getY());
		ret.addPositionReport(me);

		Position target = targets.get(0).relocate(o.getX(), o.getY());
		ret.getEffectReport(targets.get(0).getId()).addPositionReport(target);
	}

}
