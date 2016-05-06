package com.eyu.snm.module.fight.service.effect.attr;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.eyu.common.utils.json.JsonUtils;
import com.eyu.snm.module.fight.model.UnitValue;
import com.eyu.snm.module.fight.model.report.Alter;
import com.eyu.snm.module.fight.model.report.EffectReport;
import com.eyu.snm.module.fight.model.report.StageReport;
import com.eyu.snm.module.fight.service.core.Unit;
import com.eyu.snm.module.fight.service.effect.Effect;
import com.eyu.snm.module.fight.service.effect.EffectKeys;
import com.eyu.snm.module.fight.service.effect.EffectType;
import com.eyu.snm.module.fight.service.effect.FormulaHelper;
import com.eyu.snm.module.fight.service.effect.Formulas;

/**
 * 修改目标的属性(只能用于修改HP及MP,并且该方法不会致死)
 * @author shenlong
 */
@Component
public class TargetAttribute implements Effect {

	private static final Logger logger = LoggerFactory.getLogger(TargetAttribute.class);

	@Override
	public EffectType getType() {
		return EffectType.TARGET_ATTR;
	}

	@Override
	public void execute(StageReport ret, Map<String, Object> context, Map<String, Object> content, Unit owner, List<Unit> targets) {
		// 配置信息
		String attributeMapString = (String) content.get(EffectKeys.ATTR);
		Map<UnitValue, Integer> alterMap = JsonUtils.string2Map(attributeMapString, UnitValue.class, Integer.class);
		// 修改目标属性
		for (int index = 0; index < targets.size(); ++index) {
			Unit target = targets.get(index);
			EffectReport report = ret.getEffectReport(target.getId());
			for (Entry<UnitValue, Integer> entry : alterMap.entrySet()) {
				UnitValue key = entry.getKey();
				if (key != UnitValue.HP && key != UnitValue.HP_MAX && key != UnitValue.MP) {
					logger.error("[{}]不能用于[{}]类型属性修改", ret.getSkill(), key);
				}
				Map<String, Object> ctx = FormulaHelper.toAlterCtx(owner, target, content, key, entry.getValue());
				Integer changeValue = FormulaHelper.calculate(Formulas.FIGHT_ATTR_CHANGE, ctx);
				Alter alter = target.increaseValue(entry.getKey(), changeValue);
				report.addAlter(alter);
			}
		}
	}

}
