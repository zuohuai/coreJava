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
 * 战斗单元buff释放效果
 * @author shenlong
 */
@Component
public class UnitBuffCast implements Effect {

	private static Logger logger = LoggerFactory.getLogger(UnitBuffCast.class);

	@Override
	public EffectType getType() {
		return EffectType.UNIT_BUFF;
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
		boolean isRelay = false; // 是否依赖上下文释放buff
		if (content.get(EffectKeys.RELAY) != null) {
			isRelay = (boolean) content.get(EffectKeys.RELAY);
		}
		boolean[] isHits = null; // 命中序列
		if (isRelay) {
			isHits = (boolean[]) context.get(EffectKeys.RELAY);
		} else {
			isHits = new boolean[targets.size()];
		}
		if (isHits == null) {
			logger.error("buff配置信息错误,找不到依赖对象");
			// 容错处理
			isHits = new boolean[targets.size()];
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
			Map<String, Object> ctx = FormulaHelper.toSkillEffectCtx(owner, target, content);
			if (!isRelay || isHits[index]) {
				// 计算buff是否能够命中
				boolean isSucceed = false;
				int hitValue = FormulaHelper.calculate(buffHitFormula, ctx);
				if (hitValue > random.nextInt(100)) {
					isSucceed = true;
				}
				isHits[index] = isSucceed;
				EffectReport report = ret.getEffectReport(target.getId());
				if (isSucceed) {
					BuffState cloneState = state.clone(buffId);
					// 对战斗单元施放BUFF
					buff.add(cloneState, owner, target, ret, report);
					if (alterMap != null) {
						// 做buff对战斗单元的属性加成
						Map<UnitValue, Integer> alterValue = new HashMap<>();
						for (Entry<UnitValue, Integer> entry : alterMap.entrySet()) {
							Map<String, Object> alterCtx = FormulaHelper.toAlterCtx(owner, target, content, entry.getKey(), entry.getValue());
							// 计算属性加成值
							int changeValue = 0;
							changeValue = FormulaHelper.calculate(attrFormula, alterCtx);
							Alter alter = target.increaseValue(entry.getKey(), changeValue);
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

		context.put(EffectKeys.RELAY, isHits);

	}
}
