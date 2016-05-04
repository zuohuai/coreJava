package com.edu.game.jct.fight.service.effect.init;

import java.util.List;

import com.edu.game.jct.fight.model.report.InitTargetReport;
import com.edu.game.jct.fight.service.core.Context;
import com.edu.game.jct.fight.service.core.Fighter;
import com.edu.game.jct.fight.service.core.Unit;

/**
 * 初始化技能接口
 * @author administrator
 */
public interface InitEffect {

	/** 获取初始化技能类型 */
	InitType getType();

	/** 选择执行目标 */
	List<Unit> select(InitEffectState state, Unit owner, Fighter friend, Fighter enemy);

	/** 运算技能带来的战斗单元数值变更 */
	void execute(InitEffectState state, Unit owner, Unit target, Context ctx, InitTargetReport report);
}
