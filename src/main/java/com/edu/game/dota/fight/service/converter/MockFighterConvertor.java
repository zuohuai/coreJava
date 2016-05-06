package com.eyu.snm.module.fight.service.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.eyu.snm.module.fight.model.FighterType;
import com.eyu.snm.module.fight.service.converter.FighterConvertor;
import com.eyu.snm.module.fight.service.core.Fighter;
import com.eyu.snm.module.fight.service.core.FighterFactory;

@Component
public class MockFighterConvertor implements FighterConvertor<String[]> {

	@Override
	public List<Fighter> convert(String[] ids, boolean isAttacker) {
		List<Fighter> fighters = new ArrayList<>(ids.length);
		for (String id : ids) {
			Fighter fighter = FighterFactory.getInstance().getFighter(id);
			fighters.add(fighter);
		}
		return fighters;
	}

	@Override
	public FighterType getType() {
		return FighterType.MOCK;
	}

}
