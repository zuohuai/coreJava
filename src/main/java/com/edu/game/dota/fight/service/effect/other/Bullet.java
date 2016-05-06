package com.eyu.snm.module.fight.service.effect.other;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.eyu.snm.module.fight.model.report.StageReport;
import com.eyu.snm.module.fight.service.core.Unit;
import com.eyu.snm.module.fight.service.effect.Effect;
import com.eyu.snm.module.fight.service.effect.EffectType;
import com.eyu.snm.module.fight.service.effect.FormulaHelper;
import com.eyu.snm.module.fight.service.effect.Formulas;

/**
 * 子弹类型效果暂时应手游要求加上
 * @author shenlong
 */
@Component
public class Bullet implements Effect {

	@Override
	public EffectType getType() {
		return EffectType.BULLET;
	}

	@Override
	public void execute(StageReport ret, Map<String, Object> context, Map<String, Object> content, Unit owner, List<Unit> targets) {
		// 获取双方距离
		int grid = Math.abs(targets.get(0).getPosition().getX() - owner.getPosition().getX());
		Map<String, Object> ctx = FormulaHelper.toBulletSpeedCtx(content, grid);
		// 计算子弹飞行时间
		int updateTime = FormulaHelper.calculate(Formulas.FIGHT_BULLET_TIME, ctx);
		owner.updateNextTiming(updateTime);
		for (Unit target : targets) {
			ret.getEffectReport(target.getId());
		}

		// 手游方法,配置下一个阶段时间
		ret.setNextStageTime(updateTime);
	}
}
