package com.edu.game.jct.fight.service.effect.round;

import com.edu.game.jct.fight.model.RoundSkillType;
import com.edu.game.jct.fight.model.report.UltimateReport;
import com.edu.game.jct.fight.service.Round;
import com.edu.game.jct.fight.service.core.Fighter;

/**
 * 回合技能接口
 * @author Frank
 */
public interface RoundSkill {

	/** 获取回合技能类型 */
	RoundSkillType getType();

	/**
	 * 检查回合技能是否有效(能够施放)
	 * @param state 状态
	 * @return true:有效,false:无效
	 */
	boolean isVaild(RoundSkillState state, Round round);

	/**
	 * 是否开始选中该技能
	 * @param state
	 * @param round
	 * @return
	 */
	boolean isChoseVaild(RoundSkillState state, Round round);

	/**
	 * 执行回合技能并返回战报信息
	 * @param state 技能状态
	 * @param owner 所有者
	 * @param round 当前的回合对象
	 * @return
	 */
	UltimateReport execute(RoundSkillState state, Fighter owner, Round round);
}
