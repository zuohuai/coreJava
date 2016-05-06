package com.eyu.snm.module.fight.service.effect.init;

import java.util.List;

import com.eyu.snm.module.fight.service.BattleType;
import com.eyu.snm.module.player.service.Player;

/**
 * 构建初始化技能处理器,用于其他模块如BUFF构建战斗用的初始化技能
 * @author shenlong
 */
public interface InitSkillProcessor {

	/**
	 * 获取战斗用BUFF
	 * @param player
	 * @return
	 */
	List<String> getFightBuff(Player player, BattleType battleType);

	/**
	 * 获取对应的处理器类型
	 * @return
	 */
	InitSkillType getInitSkillType();

}
