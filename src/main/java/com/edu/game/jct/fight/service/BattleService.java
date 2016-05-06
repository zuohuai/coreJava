package com.edu.game.jct.fight.service;

import com.edu.game.Player;
import com.edu.game.jct.fight.model.BattleInfo;
import com.edu.game.jct.fight.model.report.RoundReport;
import com.edu.game.jct.fight.service.config.BattleType;
import com.edu.game.jct.fight.service.core.Fighter;

/**
 * 战斗服务接口
 * @author frank
 */
public interface BattleService {

	/**
	 * 获取玩家当前的战斗标识
	 * @param player 玩家对象
	 * @return 不存在会返回null
	 */
	public Integer getCurrentBattle(Player player);

	/**
	 * 获取玩家当前的战斗信息
	 * @param player
	 * @return
	 */
	public BattleInfo getBattleInfo(Player player);

	/**
	 * 开始战斗
	 * @param type 战斗类型
	 * @param attacker 攻击方
	 * @param defender 防守方
	 * @param callback 战斗回调
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public BattleInfo start(BattleType type, Battler attacker, Battler defender, BattleCallback callback);

	/**
	 * 立即完成战斗
	 * @param type 战斗类型
	 * @param attacker 攻击方
	 * @param defender 防守方
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Battle immediate(BattleType type, Battler attacker, Battler defender);

	/**
	 * 恢复战斗
	 * @param battleId 战斗标识
	 * @param attacker 替换的攻击方(允许为null)
	 * @param defender 替换的防守方(允许为null)
	 * @return
	 */
	public BattleInfo restore(int battleId, Fighter attacker, Fighter defender);

	/**
	 * 玩家设置自己在战斗中主将使用的技能
	 * @param owner 玩家对象
	 * @param skill 被设置的技能标识
	 * @param noWait 是否不等待回合时间，立即返回
	 */
	public RoundReport choseMajorSkill(Player owner, String skill, boolean noWait);

	/**
	 * 选择回合技能
	 * @param owner
	 * @param skill
	 */
	public void choseRoundSkill(Player owner, String skill);

	/**
	 * 进行自动战斗并获取当前回合的战报
	 * @param owner
	 * @return
	 */
	public RoundReport autoChose(Player owner);

	/**
	 * 取消指定玩家当前进行中的战斗<br/>
	 * 如果玩家当前没有战斗，该方法相当于什么都不做
	 * @param player 玩家对象
	 */
	public void cancel(Player player);

	/**
	 * 忽略战斗过程的战报直接完成玩家当前进行中的战斗<br/>
	 * 如果玩家当前没有战斗，该方法相当于什么都不做
	 * @param player 玩家对象
	 */
	public void skip(Player player);

	/**
	 * 复活战斗单位
	 * @param battleId 战斗单位ID
	 * @param isAttack 是否复活攻击方，否则就是防守方
	 * @return
	 */
	public BattleInfo revive(Player player, boolean isAttacker, ReviveCallback callback);

	/**
	 * 转换指定的战斗单位对象
	 * @param battler 作战单位标识
	 * @param isAttacker 是否攻击方
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Fighter transform(Battler battler, boolean isAttacker, BattleType battleType);
}
