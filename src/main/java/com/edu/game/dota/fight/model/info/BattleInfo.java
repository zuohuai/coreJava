package com.edu.game.dota.fight.model.info;

import java.util.ArrayList;
import java.util.List;

import com.edu.game.dota.fight.service.core.Fighter;

/**
 * 战斗初始化信息
 * @author shenlong
 */
public class BattleInfo {

	/** 战斗编号 */
	private int battleId;
	/** 种子 */
	private int seed;
	/** 攻击方列表 */
	private List<FighterVo> attackers = new ArrayList<>();
	/** 防守方列表 */
	private List<FighterVo> defenders = new ArrayList<>();

	public static BattleInfo valueOf(int battleId, int seed, List<Fighter> attackers, List<Fighter> defenders) {
		BattleInfo result = new BattleInfo();
		result.battleId = battleId;
		result.seed = seed;
		for (Fighter fighter : attackers) {
			result.attackers.add(FighterVo.valueOf(fighter));
		}
		for (Fighter fighter : defenders) {
			result.defenders.add(FighterVo.valueOf(fighter));
		}
		return result;

	}

	public int getSeed() {
		return seed;
	}

	public void setSeed(int seed) {
		this.seed = seed;
	}

	public List<FighterVo> getAttackers() {
		return attackers;
	}

	public void setAttackers(List<FighterVo> attackers) {
		this.attackers = attackers;
	}

	public List<FighterVo> getDefenders() {
		return defenders;
	}

	public void setDefenders(List<FighterVo> defenders) {
		this.defenders = defenders;
	}

	public int getBattleId() {
		return battleId;
	}

	public void setBattleId(int battleId) {
		this.battleId = battleId;
	}

}
