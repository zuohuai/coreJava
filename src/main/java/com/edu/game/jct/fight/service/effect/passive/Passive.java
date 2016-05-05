package com.edu.game.jct.fight.service.effect.passive;

import com.edu.game.jct.fight.model.report.PassiveReport;
import com.edu.game.jct.fight.service.core.Context;
import com.edu.game.jct.fight.service.core.Unit;

/**
 * 被动效果接口
 * @author Frank
 */
public interface Passive {

	/**
	 * 获取被动效果类型定义
	 * @return
	 */
	PassiveType getType();

	/**
	 * 执行被动效果，当被动效果无效时返回null
	 * @param state 被动效果状态
	 * @param owner 效果所有者
	 * @param target 效果执行的对应方
	 * @param ctx 当前的主动技能效果上下文
	 * @return
	 */
	PassiveReport execute(PassiveState state, Unit owner, Unit target, Context ctx);

}
