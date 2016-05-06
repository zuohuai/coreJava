package com.edu.game.dota.fight.exception;

/**
 * 战斗异常代码
 * @author shenlong
 */
public interface FightExceptionCode {

	/** 配置错误 */
	int CONFIG_ERROR = 0;

	/** 武将上阵数量为空 */
	int BLOCK_BY_ATTACKER_HERO_AMOUNT = -1;

	/** 玩家已经在战斗中 */
	int ALREADY_IN_BATTLE = -2;

	/** 验证失败 */
	int JUSTIFY_FAILED = -3;
}
