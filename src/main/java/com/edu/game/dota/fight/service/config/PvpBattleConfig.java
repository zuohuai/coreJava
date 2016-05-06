package com.eyu.snm.module.fight.service.config;

import org.springframework.stereotype.Component;

import com.eyu.snm.module.fight.service.BattleType;
import com.eyu.snm.module.fight.service.core.DefaultBattleConfig;

/**
 * PVP战斗类型配置对象
 * @author Kent
 */
@Component
public class PvpBattleConfig extends DefaultBattleConfig {

	@Override
	public BattleType getType() {
		return BattleType.PVP;
	}

}
