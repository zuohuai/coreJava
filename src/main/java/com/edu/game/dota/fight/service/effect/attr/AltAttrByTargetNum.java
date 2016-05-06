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
import com.eyu.snm.module.fight.model.report.StageReport;
import com.eyu.snm.module.fight.service.core.Unit;
import com.eyu.snm.module.fight.service.effect.Effect;
import com.eyu.snm.module.fight.service.effect.EffectKeys;
import com.eyu.snm.module.fight.service.effect.EffectType;
import com.eyu.snm.module.fight.service.effect.FormulaHelper;
import com.eyu.snm.module.fight.service.effect.Formulas;
import com.eyu.snm.module.fight.service.effect.attr.AltAttrByTargetNum;

/**
 * 根据命中目标及暴击改变自身属性(只能用于修改MP,HP)
 * @author shenlong
 */
@Component
public class AltAttrByTargetNum implements Effect {

	private static final Logger logger = LoggerFactory.getLogger(AltAttrByTargetNum.class);

	@Override
	public EffectType getType() {
		return EffectType.ALT_ATTR_BY_TARGET_NUM;
	}

	@Override
	public void execute(StageReport ret, Map<String, Object> context, Map<String, Object> content, Unit owner, List<Unit> targets) {
		Integer targetNum = (Integer) context.get(EffectKeys.TARGET_NUM); // 命中数
		if (targetNum == null) {
			logger.error("技能[{}]技能效果ALT_ATTR_BY_TARGET_NUM找不到前置效果的目标", ret.getSkill());
			// 容错处理
			return;
		}
		Integer critNum = (Integer) context.get(EffectKeys.CRIT_NUM); // 暴击数
		Map<UnitValue, Integer> alterMap = JsonUtils.string2Map((String) content.get(EffectKeys.ATTR), UnitValue.class, Integer.class);
		for (Entry<UnitValue, Integer> entry : alterMap.entrySet()) {
			// 计算普通能量加成
			Map<String, Object> ctx = FormulaHelper.toAlterCtx(owner, null, content, entry.getKey(), entry.getValue() * targetNum);
			Integer changeValue = FormulaHelper.calculate(Formulas.FIGHT_ATTR_CHANGE, ctx);
			// 计算暴击能量加成
			if (critNum != null) {
				ctx = FormulaHelper.toAlterCtx(owner, null, content, entry.getKey(), entry.getValue() * critNum);
				int critValue = FormulaHelper.calculate(Formulas.FIGHT_ATTR_CRIT_CHANGE, ctx);
				changeValue += critValue;
			}
			Alter alter = owner.increaseValue(entry.getKey(), changeValue);
			ret.addAlter(alter);
		}
	}

}
