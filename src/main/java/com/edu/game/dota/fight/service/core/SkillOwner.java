package com.eyu.snm.module.fight.service.core;

/**
 * 技能拥有者,用于目标搜寻
 * @author shenlong
 */
public interface SkillOwner {

	/**
	 * 是否为攻击方
	 * @return
	 */
	boolean isAttacker();

	/**
	 * 获取战斗信息
	 * @return
	 */
	Battle getBattle();

}
