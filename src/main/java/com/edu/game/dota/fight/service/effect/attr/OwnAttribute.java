package com.eyu.snm.module.fight.service.effect.attr;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.eyu.common.utils.json.JsonUtils;
import com.eyu.snm.module.fight.model.UnitValue;
import com.eyu.snm.module.fight.model.report.Alter;
import com.eyu.snm.module.fight.model.report.StageReport;
import com.eyu.snm.module.fight.service.core.Unit;
import com.eyu.snm.module.fight.service.effect.Effect;
import com.eyu.snm.module.fight.service.effect.EffectKeys;
import com.eyu.snm.module.fight.service.effect.EffectType;
import com.eyu.snm.module.fight.service.effect.FormulaHelper;
import com.eyu.snm.module.fight.service.effect.Formulas;

/**
 * 自身属性变更效果(用于修改HP及MP)
 * @author Kent
 */
@Component
public class OwnAttribute implements Effect {

	@Override
	public EffectType getType() {
		return EffectType.OWN_ATTR;
	}

	@Override
	public void execute(StageReport ret, Map<String, Object> context, Map<String, Object> content, Unit owner, List<Unit> targets) {
		Map<UnitValue, Integer> alterMap = JsonUtils.string2Map((String) content.get(EffectKeys.ATTR), UnitValue.class, Integer.class);
		for (Entry<UnitValue, Integer> entry : alterMap.entrySet()) {
			// 修改自身属性
			Map<String, Object> ctx = FormulaHelper.toAlterCtx(owner, null, content, entry.getKey(), entry.getValue());
			Integer changeValue = FormulaHelper.calculate(Formulas.FIGHT_ATTR_CHANGE, ctx);
			Alter alter = owner.increaseValue(entry.getKey(), changeValue);
			ret.addAlter(alter);
		}
	}

}
