package com.eyu.snm.module.fight.service.effect.buff;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
 * 子弹类型的战场buff释放
 * 该效果用于在所选中的目标位置放置一个统一的战场buff(已废弃)
 * @author shenlong
 */
@Component
public class BulletBuffCast implements Effect {

	private static final Logger logger = LoggerFactory.getLogger(BulletBuffCast.class);

	@Override
	public EffectType getType() {
		return EffectType.BULLET_BUFF;
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
		Set<Position> positions = new HashSet<>();
		for (Unit target : targets) {
			// 根据目标位置施放战场buff
			Position position = target.getPosition();
			positions.add(position);
		}

		Map<String, Object> ctx = FormulaHelper.toSkillEffectCtx(owner, null, content);
		// 计算是否命中
		boolean isSucceed = false;
		int hitValue = FormulaHelper.calculate(Formulas.FIGHT_AREA_BUFF, ctx);
		if (hitValue > random.nextInt(100)) {
			isSucceed = true;
		}
		if (isSucceed) {
			buff.add(cloneState, owner, null, ret, null, positions.toArray(new Position[0]));
		}

	}
}
