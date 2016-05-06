package com.eyu.snm.module.fight.service.converter;

import java.util.ArrayList;
import java.util.Arrays;
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
public class PlayerPvpFighterConverter implements FighterConvertor<Long> {

	@Override
	public FighterType getType() {
		return FighterType.PLAYER_PVP;
	}

	@Override
	public List<Fighter> convert(Long player, boolean attacker) {
		Fighter fighter = FighterFactory.getInstance().getPlayerFighter(player);
		return new ArrayList<>(Arrays.asList(fighter));
	}

}
