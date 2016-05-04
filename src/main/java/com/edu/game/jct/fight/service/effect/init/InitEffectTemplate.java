package com.edu.game.jct.fight.service.effect.init;

import java.util.List;

import com.edu.game.jct.fight.service.core.Fighter;
import com.edu.game.jct.fight.service.core.Unit;
import com.edu.game.jct.fight.service.effect.select.SelectHelper;
import com.edu.game.jct.fight.service.effect.select.SelectType;

/**
 * 初始化技能效果模版
 * @author administrator
 */
public abstract class InitEffectTemplate implements InitEffect {

	@Override
	public List<Unit> select(InitEffectState state, Unit owner, Fighter friend, Fighter enemy) {
		SelectType selectType = state.getTarget();
		return SelectHelper.select(selectType, owner, friend, enemy);
	}
	
}
