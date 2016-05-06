package com.eyu.snm.module.fight.service.effect.init;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.eyu.snm.module.fight.model.UnitValue;
import com.eyu.snm.module.fight.service.buff.Buff;
import com.eyu.snm.module.fight.service.buff.BuffFactory;
import com.eyu.snm.module.fight.service.buff.BuffState;
import com.eyu.snm.module.fight.service.buff.StateCtxKeys;
import com.eyu.snm.module.fight.service.core.Unit;
import com.eyu.snm.module.fight.service.effect.EffectKeys;
import com.eyu.snm.module.fight.service.effect.FormulaHelper;

/**
 * 初始化buff释放效果
 * @author shenlong
 */
@Component
public class InitBuffEffect implements InitEffect {

	private static Logger logger = LoggerFactory.getLogger(InitBuffEffect.class);

	@Override
	public InitType getInitType() {
		return InitType.FOREVER_BUFF;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(Map<String, Object> content, List<Unit> targets) {
		String buffId = (String) content.get(EffectKeys.BUFF);
		if (buffId == null) {
			logger.error("buff配置信息错误,未配置buffId");
			return;
		}
		Buff buff = BuffFactory.getInstance().getBuff(buffId);
		BuffState state = BuffFactory.getInstance().initState(buffId);
		HashMap<String, Object> buffCtx = state.getContent();
		Map<UnitValue, Integer> alterMap = null; // 属性变化值
		String attrFormula = null; // 属性变化公式
		// 获取buff需要修改的单位属性的公式及参数
		if (buffCtx != null && buffCtx.containsKey(StateCtxKeys.ALTERS)) {
			alterMap = (Map<UnitValue, Integer>) state.getContent().get(StateCtxKeys.ALTERS);
			attrFormula = (String) content.get(EffectKeys.FORMULA_ID);
		}
		for (Unit target : targets) {
			buff.add(state.clone(buffId), null, target, null, null);
			if (alterMap != null) {
				// 修正buff上的普通属性影响
				for (Entry<UnitValue, Integer> entry : alterMap.entrySet()) {
					Map<String, Object> alterCtx = FormulaHelper.toAlterCtx(null, target, content,entry.getKey(), entry.getValue());
					int changeValue = 0;
					changeValue = FormulaHelper.calculate(attrFormula, alterCtx);
					target.increaseValue(entry.getKey(), changeValue);
				}
			}
		}
	}
}
