package com.edu.game.jct.fight.service.alter;

import com.edu.game.jct.fight.model.UnitValue;
import com.edu.game.jct.fight.service.core.Unit;

/**
 * 战斗单位的{@link UnitValue}值修改器
 * @author Administrator
 */
public class UnitValueAlter extends IntegerTemplate {

	/** 修改类型 */
	private UnitValue type;

	public UnitValueAlter(UnitValue type) {
		this.type = type;
	}

	@Override
	public void execute(Unit unit, Integer value) {
		unit.increaseValue(type, value);
	}

}
