package com.eyu.snm.module.fight.service.effect.buff;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eyu.snm.module.fight.model.report.StageReport;
import com.eyu.snm.module.fight.service.buff.Buff;
import com.eyu.snm.module.fight.service.buff.BuffFactory;
import com.eyu.snm.module.fight.service.buff.BuffState;
import com.eyu.snm.module.fight.service.core.Area;
import com.eyu.snm.module.fight.service.core.Position;
import com.eyu.snm.module.fight.service.core.Unit;
import com.eyu.snm.module.fight.service.effect.Effect;
import com.eyu.snm.module.fight.service.effect.EffectKeys;
import com.eyu.snm.module.fight.service.effect.EffectType;

/**
 * 固定位置buff释放(影魔的影压等)
 * @author shenlong
 */
public class PositionBuffCast implements Effect {

	private static final Logger logger = LoggerFactory.getLogger(PositionBuffCast.class);

	@Override
	public EffectType getType() {
		return EffectType.POSITION_BUFF;
	}

	@Override
	public void execute(StageReport ret, Map<String, Object> context, Map<String, Object> content, Unit owner, List<Unit> targets) {
		int[] distance = (int[]) content.get(EffectKeys.POSITION);
		// 固定位置距离
		Position[] positions = new Position[distance.length];
		for (int i = 0; i < distance.length; i++) {
			Position position = Area.justBoundary(owner.getPosition(), distance[i]);
			positions[i] = position;
		}

		String buffId = (String) content.get(EffectKeys.BUFF);
		if (buffId == null) {
			// 容错
			logger.error("[{}]找不到buff配置信息", ret.getSkill());
			return;
		}
		// 获取相应buff处理器
		BuffFactory facatory = BuffFactory.getInstance();
		Buff buff = facatory.getBuff(buffId);
		BuffState state = facatory.initState(buffId);
		BuffState cloneState = state.clone(buffId);
		// 添加buff
		buff.add(cloneState, owner, null, ret, null, positions);
	}
}
