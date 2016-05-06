package com.edu.game.dota.fight.service;

import com.eyu.snm.module.fight.model.VerifyResult;
import com.eyu.snm.module.fight.service.core.Battle;

/**
 * 随机种子战斗回调
 * @author shenlong
 */
public interface RandomCallBack {

	/**
	 * 当战斗结束时触发
	 * @param battle 战斗对象
	 */
	VerifyResult backResult(Battle result);

	/**
	 * 战斗超时处理
	 * @param battle
	 */
	void timeOut(Battle battle);
}
