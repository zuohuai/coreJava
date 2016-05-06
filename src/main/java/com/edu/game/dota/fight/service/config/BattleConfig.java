package com.eyu.snm.module.fight.service.config;

import com.eyu.snm.module.fight.model.BattleResult;
import com.eyu.snm.module.fight.service.BattleType;
import com.eyu.snm.module.fight.service.core.Battle;

/**
 * 战斗配置接口
 * @author frank
 */
public interface BattleConfig {

	/**
	 * 获取战斗的类型名，由该类型名决定正场比赛中的各相关对象的具体类型
	 * @return
	 */
	BattleType getType();

	/**
	 * 每个回合战斗开始时的重置方法
	 */
	void reset(Battle battle);

	/**
	 * 获取回合战斗的超时时间
	 * @return 战斗的最大时长，单位毫秒
	 */
	int getBattleOvertime();

	/**
	 * 验证超时时间
	 * @return
	 */
	int getVerifyTime();

	/**
	 * 获取战斗结果
	 * @return
	 */
	boolean judgeResult(Battle battle, BattleResult battleResult);

	/**
	 * 配置超时结果
	 */
	void timeOut(Battle battle);

	/**
	 * 是否手操
	 * @return
	 */
	boolean isManual();
}
