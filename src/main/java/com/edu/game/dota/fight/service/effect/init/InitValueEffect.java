package com.eyu.snm.module.fight.service.effect.init;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.eyu.common.utils.json.JsonUtils;
import com.eyu.snm.module.fight.model.UnitValue;
import com.eyu.snm.module.fight.service.core.Unit;
import com.eyu.snm.module.fight.service.effect.EffectKeys;
import com.eyu.snm.module.fight.service.effect.FormulaHelper;

/**
 * 初始化值修改效果(该效果修改的值不会被移除掉)
 * @author shenlong
 */
@Component
public class InitValueEffect implements InitEffect {

	@Override
	public InitType getInitType() {
		return InitType.VALUE;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(Map<String, Object> content, List<Unit> targets) {
		// 获取配置信息
		String formulaId = (String) content.get(EffectKeys.FORMULA_ID);
		String alterString = (String) content.get(EffectKeys.ATTR);
		// 做值修改
		Map<UnitValue, Integer> altermap = JsonUtils.string2Map(alterString, UnitValue.class, Integer.class, HashMap.class);
		for (Unit target : targets) {
			for (Entry<UnitValue, Integer> entry : altermap.entrySet()) {
				Map<String, Object> ctx = FormulaHelper.toAlterCtx(null, target, content, entry.getKey(), entry.getValue());
				int value = FormulaHelper.calculate(formulaId, ctx);
				target.increaseValue(entry.getKey(), value);
			}
		}

	}
}
