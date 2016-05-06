package com.eyu.snm.module.fight.service.effect.buff;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.eyu.snm.module.fight.model.report.StageReport;
import com.eyu.snm.module.fight.model.report.seed.Random;
import com.eyu.snm.module.fight.service.buff.Buff;
import com.eyu.snm.module.fight.service.buff.BuffFactory;
import com.eyu.snm.module.fight.service.buff.BuffState;
import com.eyu.snm.module.fight.service.core.Position;
import com.eyu.snm.module.fight.service.core.Unit;
import com.eyu.snm.module.fight.service.effect.Effect;
import com.eyu.snm.module.fight.service.effect.EffectKeys;
import com.eyu.snm.module.fight.service.effect.EffectType;
import com.eyu.snm.module.fight.service.effect.FormulaHelper;
import com.eyu.snm.module.fight.service.effect.Formulas;

/**
 * 战场buff施放效果
 * 该效果会对每一个被选择的目标的位置上释放战场buff,
 * 如果被选择到的目标为空则变为全局战场buff(TODO 可能有问题)
 * 2014/8/4 经与页游客户端商讨小黑的大招不经过该效果改为直接伤害不使用该效果
 * @author shenlong
 */
@Component
public class AreaBuffCast implements Effect {

	private static final Logger logger = LoggerFactory.getLogger(AreaBuffCast.class);

	@Override
	public EffectType getType() {
		return EffectType.AREA_BUFF;
	}

	@Override
	public void execute(StageReport ret, Map<String, Object> context, Map<String, Object> content, Unit owner, List<Unit> targets) {
		Random random = owner.getBattle().getRandom();
		BuffFactory facatory = BuffFactory.getInstance();
		String buffId = (String) content.get(EffectKeys.BUFF);
		if (buffId == null) {
			// 容错
			logger.error("[{}]找不到buff配置信息", ret.getSkill());
			return;
		}
		// 获取相应buff处理器
		Buff buff = facatory.getBuff(buffId);
		BuffState state = facatory.initState(buffId);
		BuffState cloneState = state.clone(buffId);
		if (targets.size() == 0) {
			// 添加全局战场buff
			buff.add(cloneState, owner, null, ret, null);
		}
		for (Unit target : targets) {
			// 根据目标位置施放战场buff
			Position position = target.getPosition();
			Map<String, Object> ctx = FormulaHelper.toSkillEffectCtx(owner, target, content);
			// 判断是否命中
			boolean isSucceed = false;
			int hitValue = FormulaHelper.calculate(Formulas.FIGHT_AREA_BUFF, ctx);
			if (hitValue > random.nextInt(100)) {
				isSucceed = true;
			}
			if (isSucceed) {
				buff.add(cloneState, owner, target, ret, null, position);
			}
		}
	}

}
