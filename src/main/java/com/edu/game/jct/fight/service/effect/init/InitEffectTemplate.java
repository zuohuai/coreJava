package com.eyu.ahxy.module.fight.service.effect.init;

import java.util.List;

import com.eyu.ahxy.module.fight.service.core.Fighter;
import com.eyu.ahxy.module.fight.service.core.Unit;
import com.eyu.ahxy.module.fight.service.effect.select.SelectHelper;
import com.eyu.ahxy.module.fight.service.effect.select.SelectType;

/**
 * 初始化技能效果模版
 * @author Frank
 */
public abstract class InitEffectTemplate implements InitEffect {

	@Override
	public List<Unit> select(InitEffectState state, Unit owner, Fighter friend, Fighter enemy) {
		SelectType selectType = state.getTarget();
		return SelectHelper.select(selectType, owner, friend, enemy);
	}
	
}
