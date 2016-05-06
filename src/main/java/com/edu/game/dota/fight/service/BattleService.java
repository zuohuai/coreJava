package com.edu.game.dota.fight.service;

import java.util.List;

import com.eyu.snm.module.fight.service.core.Battle;
import com.eyu.snm.module.fight.model.VerifyResult;
import com.eyu.snm.module.fight.model.info.BattleInfo;
import com.eyu.snm.module.fight.model.report.seed.OperationData;
import com.eyu.snm.module.fight.service.BattleType;
import com.eyu.snm.module.fight.service.FighterId;
import com.eyu.snm.module.fight.service.RandomCallBack;
import com.eyu.snm.module.player.service.Player;

/**
 * 战斗服务接口
 * @author frank
 */
public interface BattleService {

	/**
	 * 立即完成战斗
	 * @param type 战斗类型
	 * @param attacker 攻击方
	 * @param defender 防守方
	 * @param 战斗回调(为空则不进行回调)
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Battle immediate(BattleType type, FighterId attacker, FighterId defender, BattleCallback callback);

	// ===================================== 随机种子用,这是友情的分割线========================================
	/**
	 * 开始战斗
	 * @param type
	 * @param attacker
	 * @param deFighterId
	 * @param callback
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public BattleInfo start(Player player, BattleType type, FighterId aid, FighterId fid, RandomCallBack callback);

	/**
	 * 验证手操数据
	 * @param battleId
	 * @return
	 */
	public VerifyResult verify(Player player, int battleId, List<OperationData>[] opData);

}
