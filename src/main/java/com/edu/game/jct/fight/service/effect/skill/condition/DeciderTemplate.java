package com.edu.game.jct.fight.service.effect.skill.condition;

import java.util.List;

import com.edu.game.jct.fight.model.UnitState;
import com.edu.game.jct.fight.service.core.Fighter;
import com.edu.game.jct.fight.service.core.Unit;
import com.edu.game.jct.fight.service.effect.select.SelectHelper;
import com.edu.game.jct.fight.service.effect.select.SelectType;

public abstract class DeciderTemplate implements Decider {

	public List<Unit> select(SelectType selectType, Unit owner, Fighter friend, Fighter enemy) {
		if (owner.hasState(UnitState.CHAOS)) {
			selectType = SelectHelper.getReverseType(selectType);
		}
		return SelectHelper.select(selectType, owner, friend, enemy);
	}

}
