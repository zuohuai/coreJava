package com.edu.game.jct.fight.service.effect.skill;

import java.util.List;

import com.edu.game.jct.fight.model.report.TargetReport;
import com.edu.game.jct.fight.service.core.Context;
import com.edu.game.jct.fight.service.core.Fighter;
import com.edu.game.jct.fight.service.core.SkillEffectAction;
import com.edu.game.jct.fight.service.core.Unit;

/**
 * 技能效果接口
 * @author Frank
 */
public interface SkillEffect {

	/** 获取效果类型 */
	EffectType getType();

	/** 选择执行目标 */
	List<Unit> select(EffectState state, Unit owner, Fighter friend, Fighter enemy);

	/** 运算技能带来的战斗单元数值变更 */
	void execute(EffectState state, Unit owner, Unit target, Context ctx, TargetReport report, SkillEffectAction action);

}
