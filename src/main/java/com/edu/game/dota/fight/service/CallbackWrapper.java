package com.edu.game.dota.fight.service;

import com.eyu.snm.module.fight.service.core.Battle;

/**
 * 回调对象包装器
 * @author Kent
 */
@SuppressWarnings("rawtypes")
public class CallbackWrapper {

	final FighterId attacker;
	final FighterId defender;
	final BattleCallback callback;
	final Battle battle;

	public CallbackWrapper(FighterId attacker, FighterId defender, BattleCallback callback, Battle battle) {
		this.attacker = attacker;
		this.defender = defender;
		this.callback = callback;
		this.battle = battle;
	}

}
