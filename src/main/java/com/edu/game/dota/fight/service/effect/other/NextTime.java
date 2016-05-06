package com.eyu.snm.module.fight.service.effect.other;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.eyu.snm.module.fight.model.report.StageReport;
import com.eyu.snm.module.fight.service.core.Unit;
import com.eyu.snm.module.fight.service.effect.Effect;
import com.eyu.snm.module.fight.service.effect.EffectKeys;
import com.eyu.snm.module.fight.service.effect.EffectType;
import com.eyu.snm.module.fight.service.effect.FormulaHelper;
import com.eyu.snm.module.fight.service.effect.Formulas;

/**
 * 调整单位下次行动时间
 * @author shenlong
 */
@Component
public class NextTime implements Effect {

	@Override
	public EffectType getType() {
		return EffectType.NEXT_TIME;
	}

	@Override
	public void execute(StageReport ret, Map<String, Object> context, Map<String, Object> content, Unit owner, List<Unit> targets) {
		int updateTime = (int) content.get(EffectKeys.UPDATETIME);
		owner.updateNextTiming(updateTime);

		Map<String, Object> ctx = FormulaHelper.toNextTimeCtx(owner, updateTime);
		updateTime = FormulaHelper.calculate(Formulas.FIGHT_NEXT_TIME, ctx);

		// 手游用方法
		ret.setNextStageTime(updateTime);
	}

}
