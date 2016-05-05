package com.edu.game.jct.fight.service.effect.skill.condition;

import java.util.List;

import org.springframework.stereotype.Component;

import com.edu.game.jct.fight.model.UnitValue;
import com.edu.game.jct.fight.service.core.Fighter;
import com.edu.game.jct.fight.service.core.Unit;
import com.edu.game.jct.fight.service.effect.select.SelectType;

/**
 * {@link ConditionType#HP_GT}的实现类
 * @author Frank
 */
@Component
public class HpGt extends DeciderTemplate {

	@Override
	public ConditionType getType() {
		return ConditionType.HP_GT;
	}

	@Override
	public boolean isAllow(Unit owner, Fighter friend, Fighter enemy, SelectType selectType, String value) {
		double rate = Double.valueOf(value);
		List<Unit> targets = select(selectType, owner, friend, enemy);
		for (Unit unit : targets) {
			double current = (double) unit.getValue(UnitValue.HP) / unit.getValue(UnitValue.HP_MAX);
			if (current > rate) {
				return true;
			}
		}
		return false;
	}

}
