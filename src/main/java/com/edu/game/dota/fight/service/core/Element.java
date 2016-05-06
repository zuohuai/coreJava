package com.eyu.snm.module.fight.service.core;

import com.eyu.snm.module.fight.service.action.Action;
import com.eyu.snm.module.fight.service.op.Operation;

/**
 * 战斗元素，用于表示战斗中遇到表现元素或逻辑元素
 * @author Frank
 */
public interface Element {

	/**
	 * 获取战斗元素的标识
	 * @return
	 */
	short getId();

	/**
	 * 设置战斗元素的所在位置
	 * @param position
	 */
	void setPosition(Position... position);

	/**
	 * 检查战斗元素是否还有效
	 * 
	 * <pre>
	 * 1.战斗单位是否活着
	 * 2.场景元素是否还需要运算
	 * </pre>
	 * @param battle 所属的战斗对象
	 * @return 当返回 false 时，会不再获取战斗元素的后续行动(Operation)
	 */
	boolean isValid(Battle battle);

	/**
	 * 获取战斗元素上的具体行为
	 * @param timing 动作执行的时间点
	 * @param battle 当前的战斗对象
	 * @return 会返回null,返回null表示战斗元素在该时间点没有具体的行为(可能为硬直等原因导致)
	 */
	Action getAction(long timing, Battle battle);

	/**
	 * 获取当前战斗元素的下次行动
	 * @return
	 */
	Operation getNextOp();

}