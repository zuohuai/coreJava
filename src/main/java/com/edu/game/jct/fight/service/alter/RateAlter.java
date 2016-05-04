package com.edu.game.jct.fight.service.alter;

import com.edu.game.jct.fight.model.UnitRate;
import com.edu.game.jct.fight.service.core.Unit;

/**
 * 比率属性(累加关系)
 * @author Administrator
 *
 */
public class RateAlter extends DoubleTemplate {

	private UnitRate rate;

	public RateAlter(UnitRate rate) {
		this.rate = rate;
	}

	@Override
	public void execute(Unit unit, Double value) {
		unit.increaseRate(rate, value);
	}

}
