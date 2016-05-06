package com.eyu.snm.module.fight.service.op;

import com.eyu.snm.module.fight.service.core.Battle;
import com.eyu.snm.module.fight.service.core.Element;

/**
 * 行动抽象接口<br/>
 * 用于表示战斗中需要执行的下一个操作。
 * @author Frank
 */
public interface Operation extends Comparable<Operation> {

	/**
	 * 执行当前的操作/行为，并返回下一个操作/行为
	 * @return
	 */
	Operation execute(Battle battle);

	/**
	 * 获取该操作/行为的执行时间点
	 * @return
	 */
	long getTiming();

	/**
	 * 获取行动所有者
	 * @return
	 */
	Element getOwner();
	
	/**
	 * 获取操作自增ID
	 * @return
	 */
	int getId();

}