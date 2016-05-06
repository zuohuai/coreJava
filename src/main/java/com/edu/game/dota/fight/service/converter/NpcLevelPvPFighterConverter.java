package com.eyu.snm.module.fight.service.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.eyu.snm.module.fight.model.FighterType;
import com.eyu.snm.module.fight.service.core.Fighter;
import com.eyu.snm.module.fight.service.core.FighterFactory;

/**
 * NPC PVP玩法通过（等级）参数构建战斗对象转换器
 * @author moping
 */
@Component
public class NpcLevelPvPFighterConverter implements FighterConvertor<Object[]> {

	@Override
	public FighterType getType() {
		return FighterType.NPC_LEVEL_PVP;
	}

	@Override
	public List<Fighter> convert(Object[] params, boolean attacker) {
		List<Fighter> fighters = new ArrayList<>();
		String fighterId = (String) params[0];
		// 一个修正战斗单元的参数，一般为玩家等级或者世界等级
		int level = (int) params[1];
		Fighter fighter = FighterFactory.getInstance().getNpcFighter(fighterId, level);
		fighters.add(fighter);
		return fighters;
	}

}
