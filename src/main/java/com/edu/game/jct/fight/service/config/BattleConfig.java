package com.edu.game.jct.fight.service.config;

import java.util.Date;

import com.edu.game.jct.fight.service.core.Fighter;

/**
 * 战斗配置接口<br/>
 * 战斗锁次序:synchronized(Battle) -> ChainLock(Player...) -> synchronized(LockObject)
 * @author Administrator
 */
public interface BattleConfig {

	/**
	 * 获取战斗的类型名，由该类型名决定正场比赛中的各相关对象的具体类型
	 * @return
	 */
	BattleType getType();

	/**
	 * 速度出手类型，该类型由战斗性质决定
	 * @return
	 */
	SpeedType getSeepType();

	/**
	 * 创建战斗对象
	 * @param id 战斗对象标识
	 * @param attacker 攻击方
	 * @param defender 防守方
	 * @param callback 回调接口
	 * @return
	 */
	Battle build(int id, Fighter attacker, Fighter defender, BattleCallback callback);

	/**
	 * 获取整场战斗的超时时间
	 * @return 没有超时时间则返回null
	 */
	Date getBattleOvertime();

	/**
	 * 获取战斗中每一回合的超时时间
	 * @return 没有超时时间则返回null
	 */
	Date getRoundOvertime();

	/**
	 * 获取最大回合数
	 * @return
	 */
	int getMaxRound();

	/**
	 * 能否设置技能后即时返回战报
	 * @return
	 */
	boolean isNoWait();

	/**
	 * 是否有后续战斗处理
	 * @return true:有,在回调完后处理BATTLE_END; false:没有,在回调前处理BATTLE_END
	 */
	boolean hasNext();

	/**
	 * 获取战斗恢复超时时间
	 * @return 没有超时时间则返回null
	 */
	Date getRestoreOvertime();

	/** 能否跳过战斗 */
	boolean isSkip();
}
