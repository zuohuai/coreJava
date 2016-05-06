package com.eyu.snm.module.fight.service.effect.other;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.eyu.snm.module.fight.model.report.StageReport;
import com.eyu.snm.module.fight.service.core.Unit;
import com.eyu.snm.module.fight.service.effect.BreakType;
import com.eyu.snm.module.fight.service.effect.Effect;
import com.eyu.snm.module.fight.service.effect.EffectKeys;
import com.eyu.snm.module.fight.service.effect.EffectType;

/**
 * 打断效果
 * @author shenlong
 */
@Component
public class Break implements Effect {

	@Override
	public EffectType getType() {
		return EffectType.BREAK;
	}

	@Override
	public void execute(StageReport ret, Map<String, Object> context, Map<String, Object> content, Unit owner, List<Unit> targets) {
		BreakType type = BreakType.NORMAL;
		if (content.containsKey(EffectKeys.BREAKTYPE)) {
			type = BreakType.valueOf((String) content.get(EffectKeys.BREAKTYPE));
		}
		// 获取命中结果
		boolean[] isHits = (boolean[]) context.get(EffectKeys.RELAY);
		for (int index = 0; index < targets.size(); ++index) {
			if (isHits[index]) {
				// 根据打断类型进行打断
				Unit target = targets.get(index);
				if (target.doBreak(type)) {
					ret.addBreak(target.getId());
				}
			}
		}
	}

}
