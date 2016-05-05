package com.edu.game.jct.fight.service.effect.skill;

import java.util.List;

import com.edu.game.Formula;
import com.edu.game.jct.fight.model.UnitRate;
import com.edu.game.jct.fight.model.UnitState;
import com.edu.game.jct.fight.service.core.Fighter;
import com.edu.game.jct.fight.service.core.Unit;
import com.edu.game.jct.fight.service.effect.select.SelectHelper;
import com.edu.game.jct.fight.service.effect.select.SelectType;
import com.edu.game.resource.anno.Static;
import com.edu.utils.RandomUtils;

/**
 * 主动技能效果模版
 * @author Frank
 */
public abstract class SkillEffectTemplate implements SkillEffect {

	/** 技能是否成功的计算公式 */
	@Static("FIGHT:SKILL:HIT")
	protected Formula isHitFormula;
	/** 是否暴击计算公式 */
	@Static("FIGHT:SKILL:CRIT")
	protected Formula isCritFormula;
	/** 是否破击计算公式 */
	@Static("FIGHT:SKILL:FATAL")
	protected Formula isFatalFormula;

	@Override
	public List<Unit> select(EffectState state, Unit owner, Fighter friend, Fighter enemy) {
		SelectType selectType = state.getTarget();
		if (owner.hasState(UnitState.CHAOS) && RandomUtils.isHit(owner.getRate(UnitRate.FUZZY))) {
			selectType = SelectHelper.getReverseType(selectType);
		}
		return SelectHelper.select(selectType, owner, friend, enemy);
	}
	
}
