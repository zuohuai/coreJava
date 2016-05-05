package com.edu.game.jct.fight.service.effect.skill.condition;

import java.util.List;

import org.springframework.stereotype.Component;

import com.edu.game.jct.fight.service.core.Fighter;
import com.edu.game.jct.fight.service.core.Unit;
import com.edu.game.jct.fight.service.effect.select.SelectType;

/**
 * {@link ConditionType#TARGET_LT}的实现类
 * @author Frank
 */
@Component
public class TargetLt extends DeciderTemplate {

	@Override
	public ConditionType getType() {
		return ConditionType.TARGET_LT;
	}

	@Override
	public boolean isAllow(Unit owner, Fighter friend, Fighter enemy, SelectType selectType, String value) {
		int size = Integer.valueOf(value);
		List<Unit> targets = select(selectType, owner, friend, enemy);
		if (targets.size() < size) {
			return true;
		} else {
			return false;
		}
	}

}
