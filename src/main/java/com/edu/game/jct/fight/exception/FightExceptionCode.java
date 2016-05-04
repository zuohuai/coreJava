package com.edu.game.jct.fight.exception;

import com.edu.game.jct.fight.facade.FightResult;

/**
 * 异常代码值表
 * @author frank
 */
public interface FightExceptionCode {

	/** 配置信息错误 */
	int CONFIG_ERROR = FightResult.CONFIG_ERROR;

	/** 参数信息错误 */
	int ARGUMENT_ERROR = FightResult.ARGUMENT_ERROR;

	/** 战斗不存在 */
	int BATTLE_NOT_FOUND = FightResult.BATTLE_NOT_FOUND;

	/** 战斗状态错误 */
	int STATE_ERROR = FightResult.STATE_ERROR;

	/** 选择的技能不存在 */
	int SKILL_NOT_FOUND = FightResult.SKILL_NOT_FOUND;

	/** 技能无法选中 */
	int SKILL_CANNOT_CHOSE = FightResult.SKILL_CANNOT_CHOSE;

	/** 战斗不能不等待及时返回战报 */
	int BATTLE_CANNOT_NOWAIT = FightResult.BATTLE_CANNOT_NOWAIT;

	/** 攻击方战斗单位无法构建 */
	int ATTACKER_NOT_FOUND = FightResult.ATTACKER_NOT_FOUND;

	/** 防守方战斗单位无法构建 */
	int DEFENDER_NOT_FOUND = FightResult.DEFENDER_NOT_FOUND;

	/** 玩家已经在战斗中 */
	int PLAYER_ALREADY_IN_BATTLE = FightResult.PLAYER_ALREADY_IN_BATTLE;

	/** 战斗过程中发生错误 */
	int PROCESS_ERROR = FightResult.PROCESS_ERROR;

	/** 战斗存在没有死亡的单位 */
	int UNIT_NOT_DEAD = FightResult.UNIT_NOT_DEAD;

}
