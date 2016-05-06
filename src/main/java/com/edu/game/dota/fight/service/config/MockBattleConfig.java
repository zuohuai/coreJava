package com.eyu.snm.module.fight.service.config;

import org.springframework.stereotype.Component;

import com.eyu.snm.module.fight.service.BattleType;
import com.eyu.snm.module.fight.service.core.DefaultBattleConfig;

@Component
public class MockBattleConfig extends DefaultBattleConfig {

	@Override
	public BattleType getType() {
		return BattleType.MOCK;
	}

}
