package com.edu.game.jct.fight.service.alter;

import com.edu.game.jct.fight.model.UnitValue;
import com.edu.game.jct.fight.service.core.Unit;

/**
 * 怒气初始化修改器
 * @author Administrator
 *
 */
public class MpInitAlter extends IntegerTemplate {

	@Override
	public void execute(Unit unit, Integer value) {
		unit.increaseValue(UnitValue.MP_INIT, value);
		unit.changeMp(value);
	}

}
