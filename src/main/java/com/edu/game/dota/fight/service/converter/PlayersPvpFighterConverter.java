package com.eyu.snm.module.fight.service.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.eyu.snm.module.fight.model.FighterType;
import com.eyu.snm.module.fight.service.core.Fighter;
import com.eyu.snm.module.fight.service.core.FighterFactory;

/**
 * 玩家PVP玩法通用战斗对象转换器
 * @author ramon
 */
@Component
public class PlayersPvpFighterConverter implements FighterConvertor<Long[]> {

	@Override
	public FighterType getType() {
		return FighterType.PLAYERS_PVP;
	}

	@Override
	public List<Fighter> convert(Long[] players, boolean attacker) {
		List<Fighter> fighters = new ArrayList<>();
		for (long player : players) {
			Fighter fighter = FighterFactory.getInstance().getPlayerFighter(player);
			fighters.add(fighter);
		}
		return fighters;
	}

}
