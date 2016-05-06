package com.eyu.snm.module.fight.service.effect;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.eyu.snm.module.fight.model.report.Alter;
import com.eyu.snm.module.fight.model.report.EffectReport;
import com.eyu.snm.module.fight.model.report.StageReport;
import com.eyu.snm.module.fight.model.report.seed.Random;
import com.eyu.snm.module.fight.service.core.Unit;

/**
 * 伤害输出效果
 * @author shenlong
 */
@Component
public class Damage implements Effect {

	private static final Logger logger = LoggerFactory.getLogger(Damage.class);

	@Override
	public EffectType getType() {
		return EffectType.DAMAGE;
	}

	@Override
	public void execute(StageReport ret, Map<String, Object> context, Map<String, Object> content, Unit owner, List<Unit> targets) {
		Random random = owner.getBattle().getRandom();
		// 获取效果对应公式
		String damageFormula = (String) content.get(Formulas.FIGHT_DAMAGE); // 伤害公式
		String critDamageFormula = (String) content.get(Formulas.FIGHT_CRIT_DAMAGE); // 暴击伤害公式
		int targetNum = 0; // 命中数
		int critNum = 0;// 暴击数量
		boolean[] isHits = new boolean[targets.size()]; // 本轮目标命中结果
		// 目标受击结果
		for (int index = 0; index < targets.size(); ++index) {
			Unit target = targets.get(index);
			if (target.getId() == owner.getId()) {
				logger.error("技能[{}]正在伤害自己,请数值让其停止自残", ret.getSkill());
			}
			EffectReport report = ret.getEffectReport(target.getId());
			// 计算是否命中
			Map<String, Object> ctx = FormulaHelper.toSkillEffectCtx(owner, target, content);
			int hitValue = FormulaHelper.calculate(Formulas.FIGHT_ISHIT, ctx);
			boolean isHit = false;
			if (hitValue > random.nextInt(100)) {
				isHit = true;
			}
			isHits[index] = isHit;
			if (isHit) {
				// 计算是否暴击
				int critValue = FormulaHelper.calculate(Formulas.FIGHT_ISCRIT, ctx);
				boolean isCrit = false;
				if (critValue > random.nextInt(100)) {
					isCrit = true;
				}
				int damage = 0;
				// 计算伤害值
				if (isCrit) {
					critNum++;
					damage = FormulaHelper.calculate(critDamageFormula, ctx);
				} else {
					damage = FormulaHelper.calculate(damageFormula, ctx);
				}
				if (logger.isDebugEnabled()) {
					logger.debug("攻击者[{}]对战斗单元[{}]造成[{}]点伤害", new Object[] { owner.getId(), target.getId(), damage });
				}
				if (damage < 0) {
					logger.error("单位受到的伤害必须为负数");
					// 作容错处理
					damage = 0;
				}
				// 计算硬直伤害
				int hrDamage = FormulaHelper.calculate(Formulas.FIGHT_HR_DAMAGE, ctx);
				Alter alter = target.hurt(-damage);
				// 判断是否造成硬直
				boolean hr = target.doHR(hrDamage);
				if (hr) {
					ret.addHr(target.getId());
				}
				report.addAlter(alter);
				targetNum++;
				// 加入攻击状态
				if (damage == 0) {
					report.addDamageState(DamageState.IMMUNE);
				} else if (isCrit) {
					report.addDamageState(DamageState.CRIT);
				} else {
					report.addDamageState(DamageState.NORMAL);
				}

				// ----- 加入快速比对战报 -----
				ret.addDamage(damage);

			} else {
				report.addDamageState(DamageState.MISS);
			}
		}
		// 放置上下文所需信息
		context.put(EffectKeys.TARGET_NUM, targetNum);
		context.put(EffectKeys.CRIT_NUM, critNum);
		context.put(EffectKeys.RELAY, isHits);
	}
}
