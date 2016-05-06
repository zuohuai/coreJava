package com.eyu.snm.module.fight.service.effect.buff;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.eyu.snm.module.fight.model.UnitValue;
import com.eyu.snm.module.fight.model.report.Alter;
import com.eyu.snm.module.fight.model.report.EffectReport;
import com.eyu.snm.module.fight.model.report.StageReport;
import com.eyu.snm.module.fight.model.report.seed.Random;
import com.eyu.snm.module.fight.service.buff.Buff;
import com.eyu.snm.module.fight.service.buff.BuffFactory;
import com.eyu.snm.module.fight.service.buff.BuffState;
import com.eyu.snm.module.fight.service.buff.StateCtxKeys;
import com.eyu.snm.module.fight.service.core.Unit;
import com.eyu.snm.module.fight.service.effect.Effect;
import com.eyu.snm.module.fight.service.effect.EffectKeys;
import com.eyu.snm.module.fight.service.effect.EffectType;
import com.eyu.snm.module.fight.service.effect.FormulaHelper;
import com.eyu.snm.module.fight.service.effect.Formulas;

/**
 * 根据目标单位对自己释放战斗单元buff
 * @author shenlong
 */
@Component
public class SelfBuffCast implements Effect {

	private static Logger logger = LoggerFactory.getLogger(UnitBuffCast.class);

	@Override
	public EffectType getType() {
		return EffectType.SELF_BUFF;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(StageReport ret, Map<String, Object> context, Map<String, Object> content, Unit owner, List<Unit> targets) {
		Random random = owner.getBattle().getRandom();
		BuffFactory facatory = BuffFactory.getInstance();
		String buffId = (String) content.get(EffectKeys.BUFF);
		if (buffId == null) {
			logger.error("buff配置信息错误,未配置buffId");
			return;
		}

		BuffState state = facatory.initState(buffId);
		HashMap<String, Object> buffCtx = state.getContent();
		Map<UnitValue, Integer> alterMap = null; // 属性变化值
		String attrFormula = null; // 属性变化公式
		// 获取buff需要修改的单位属性的公式及参数
		if (buffCtx != null && buffCtx.containsKey(StateCtxKeys.ALTERS)) {
			alterMap = (Map<UnitValue, Integer>) state.getContent().get(StateCtxKeys.ALTERS);
			attrFormula = (String) content.get(Formulas.FIGHT_ATTR_CHANGE);
		}

		Buff buff = facatory.getBuff(buffId);
		String buffHitFormula = (String) content.get(Formulas.FIGHT_IS_BUFF_HIT); // buff 命中公式
		for (int index = 0; index < targets.size(); ++index) {
			Unit target = targets.get(index);
			// 计算buff是否能够命中
			Map<String, Object> ctx = FormulaHelper.toSkillEffectCtx(owner, target, content);
			boolean isSucceed = false;
			int hitValue = FormulaHelper.calculate(buffHitFormula, ctx);
			if (hitValue > random.nextInt(100)) {
				isSucceed = true;
			}
			EffectReport report = ret.getEffectReport(owner.getId());
			// 对战斗单元施放BUFF
			if (isSucceed) {
				BuffState cloneState = state.clone(buffId);
				buff.add(cloneState, owner, owner, ret, report);
				if (alterMap != null) {
					// 做buff对战斗单元的属性加成
					Map<UnitValue, Integer> alterValue = new HashMap<>();
					for (Entry<UnitValue, Integer> entry : alterMap.entrySet()) {
						Map<String, Object> alterCtx = FormulaHelper.toAlterCtx(owner, target, content, entry.getKey(), entry.getValue());
						// 计算属性加成值
						int changeValue = 0;
						changeValue = FormulaHelper.calculate(attrFormula, alterCtx);
						Alter alter = owner.increaseValue(entry.getKey(), changeValue);
						report.addAlter(alter);
						alterValue.put(entry.getKey(), changeValue);
					}
					cloneState.getContent().put(StateCtxKeys.ALTER_VALUE, alterValue);
				}
			} else {
				// 加入抵抗判定
				report.addResist();
			}
		}
	}

}
