package com.eyu.snm.module.fight.service.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.eyu.snm.module.fight.model.FighterType;
import com.eyu.snm.module.fight.service.core.Fighter;
import com.eyu.snm.module.fight.service.core.FighterFactory;

/**
 * NPC PVP玩法通用战斗对象转换器
 * @author ramon
 */
@Component
public class NpcPvpFighterConverter implements FighterConvertor<String[]> {

	@Override
	public FighterType getType() {
		return FighterType.NPC_PVP;
	}

	@Override
	public List<Fighter> convert(String[] ids, boolean attacker) {
		List<Fighter> fighters = new ArrayList<>(ids.length);
		for (String id : ids) {
			Fighter fighter = FighterFactory.getInstance().getFighter(id);
			fighters.add(fighter);
		}
		return fighters;

	}

}
