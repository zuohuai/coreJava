package com.edu.game.jct.fight.service.convertor;

import com.edu.game.jct.fight.service.config.FighterType;
import com.edu.game.jct.fight.service.core.Fighter;

/**
 * 战斗单位转换器
 * @author frank
 */
public interface FighterConvertor<T> {
	
	/**
	 * 根据标识符获取战斗单位集合
	 * @param id 标识符(标识符由具体实现类解析)
	 * @param isAttacker 是否攻击方(不是攻击方就是防守方)
	 * @return
	 */
	Fighter convert(T id, boolean isAttacker);

	/**
	 * 获取该转换器对应的战斗单位类型
	 * @return
	 */
	FighterType getType();

}
