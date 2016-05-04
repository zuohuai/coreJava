package com.edu.game.jct.fight.service.effect.init;

import org.springframework.stereotype.Component;

import com.edu.game.jct.fight.model.report.BuffReport;
import com.edu.game.jct.fight.model.report.InitTargetReport;
import com.edu.game.jct.fight.service.core.Context;
import com.edu.game.jct.fight.service.core.Unit;
import com.edu.game.jct.fight.service.effect.StateCtxKeys;
import com.edu.game.jct.fight.service.effect.buff.Buff;
import com.edu.game.jct.fight.service.effect.buff.BuffFactory;
import com.edu.game.jct.fight.service.effect.buff.BuffState;


/**
 * {@link InitType#BUFF}效果的实现类
 * @author Frank
 */
@Component
public class BuffEffect extends InitEffectTemplate {

	@Override
	public InitType getType() {
		return InitType.BUFF;
	}

	@Override
	public void execute(InitEffectState state, Unit owner, Unit target, Context ctx, InitTargetReport report) {
		// 获取要施放的BUFF信息
		BuffFactory factory = BuffFactory.getInstance();
		String buffId = state.getCtxValue(StateCtxKeys.BUFF, String.class);
		BuffState buffState = factory.initState(buffId);
		// 检查目标身上是否有同类BUFF
		String tag = buffState.getTag();
		BuffState prev = target.getBuffState(tag);
		if (prev != null) {
			// 移除已经存在的同类BUFF
			Buff buff = factory.getBuff(prev.getId());
			BuffReport removed = buff.remove(prev, target, ctx);
			report.addBuff(removed);
		}
		// 添加新的BUFF到目标
		Buff buff = factory.getBuff(buffId);
		BuffReport buffReport = buff.add(buffState, owner, target, ctx);
		report.addBuff(buffReport);
	}

}
