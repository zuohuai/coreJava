package com.edu.game.dota.fight.service;

import com.eyu.snm.module.fight.service.core.Battle;

/**
 * 战斗回调接口
 * @author Kent
 */
public interface BattleCallback<A, D> {

	/**
	 * 当战斗结束时触发
	 * @param battle 战斗对象
	 */
	void onBattleEnd(FighterId<A> attacker, FighterId<D> defender, Battle battle);

}
