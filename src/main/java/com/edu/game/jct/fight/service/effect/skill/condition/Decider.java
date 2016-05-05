package com.edu.game.jct.fight.service.effect.skill.condition;

import com.edu.game.jct.fight.service.core.Fighter;
import com.edu.game.jct.fight.service.core.Unit;
import com.edu.game.jct.fight.service.effect.select.SelectType;

/**
 * 技能能否施放条件的判断者接口
 * @author Frank
 */
public interface Decider {

	/**
	 * 获取判断者负责处理的条件类型
	 * @return
	 */
	ConditionType getType();

	/**
	 * 判断当前技能的执行场景是否满足要求
	 * @param owner 技能所有者
	 * @param friend 友军
	 * @param enemy 敌军
	 * @param target 判断选择目标
	 * @param value 判断值
	 * @return
	 */
	boolean isAllow(Unit owner, Fighter friend, Fighter enemy, SelectType target, String value);
}
