package com.edu.game.jct.fight.service.alter;

import com.edu.game.jct.fight.model.UnitValue;
import com.edu.game.jct.fight.service.core.Unit;

/**
 *修改血量和最大血量 {@link UnitValue#HP}与{@link UnitValue#HP_MAX}
 * @author Administrator
 *
 */
public class LifeAlter extends IntegerTemplate {

	@Override
	public void execute(Unit unit, Integer value) {
		unit.increaseValue(UnitValue.HP_MAX, value);
		unit.increaseValue(UnitValue.HP, value);
	}

}
