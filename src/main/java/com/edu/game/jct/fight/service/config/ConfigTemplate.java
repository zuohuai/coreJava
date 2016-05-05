package com.edu.game.jct.fight.service.config;

import java.util.Date;

import javax.annotation.PostConstruct;

import com.edu.game.jct.fight.resource.BattleSetting;
import com.edu.game.jct.fight.service.BattleConfig;
import com.edu.game.resource.Storage;
import com.edu.game.resource.anno.Static;

public abstract class ConfigTemplate implements BattleConfig {

	@Static
	protected Storage<BattleType, BattleSetting> settingStorage;

	protected BattleSetting setting;

	@PostConstruct
	protected void init() {
		setting = settingStorage.get(getType(), true);
	}

	@Override
	public Date getBattleOvertime() {
		Integer sec = setting.getBattleTimeOut();
		if (sec == null) {
			return null;
		} else {
			return new Date(System.currentTimeMillis() + sec * 1000);
		}
	}

	@Override
	public Date getRoundOvertime() {
		Integer sec = setting.getRoundTimeOut();
		if (sec == null) {
			return null;
		} else {
			return new Date(System.currentTimeMillis() + sec * 1000);
		}
	}

	@Override
	public Date getRestoreOvertime() {
		Integer sec = setting.getRestoreTimeOut();
		if (sec == null) {
			return null;
		} else {
			return new Date(System.currentTimeMillis() + sec * 1000);
		}
	}

	@Override
	public int getMaxRound() {
		return setting.getRound();
	}

	@Override
	public boolean isNoWait() {
		return setting.isNoWait();
	}

	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public boolean isSkip() {
		return setting.isSkip();
	}

}
