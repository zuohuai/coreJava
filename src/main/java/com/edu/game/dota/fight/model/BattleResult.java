package com.edu.game.dota.fight.model;

import java.util.List;

import com.eyu.snm.module.fight.service.core.Fighter;

/**
 * 战斗结果
 * @author shenlong
 */
public class BattleResult {

	/** 战斗胜负结果 */
	private ResultType result;
	/** 战斗血量初始值 [攻击方,防守方] */
	private int[] initHps;
	/** 战斗血量初始最大值 [攻击方,防守方] */
	private int[] initHpMaxes;
	/** 战斗血量结束值 [攻击方,防守方] */
	private int[] endHps;
	/** 战斗开始双方战斗单元数量 [攻击方,防守方] */
	private int[] initLiveNum;
	/** 战斗结束数量(不含战斗过程产生的战斗单元) [攻击方,防守方] */
	private int[] endLiveNum;

	/** 获取本场战斗变更的血量值 */
	public int[] getChangedHp() {
		int[] result = new int[2];
		result[0] = endHps[0] - initHps[0];
		result[1] = endHps[1] - initHps[1];
		return result;
	}

	/** 获取本场战斗死亡的战斗单元数量 */
	public int[] getDeadNum() {
		int[] result = new int[2];
		result[0] = initLiveNum[0] - endLiveNum[0];
		result[1] = initLiveNum[1] - endLiveNum[1];
		return result;
	}

	/** 配置战斗结束时数据 */
	public void setEnd(List<Fighter> attackers, List<Fighter> defenders) {
		endHps = countHp(attackers, defenders);
		endLiveNum = countLiveNum(attackers, defenders);
	}

	public ResultType getResult() {
		return result;
	}

	public int[] getInitHps() {
		return initHps;
	}

	public void setInitHps(int[] initHps) {
		this.initHps = initHps;
	}

	public int[] getEndHps() {
		return endHps;
	}

	public void setEndHps(int[] endHps) {
		this.endHps = endHps;
	}

	public int[] getInitLiveNum() {
		return initLiveNum;
	}

	public void setInitLiveNum(int[] initLiveNum) {
		this.initLiveNum = initLiveNum;
	}

	public int[] getEndLiveNum() {
		return endLiveNum;
	}

	public void setEndLiveNum(int[] endLiveNum) {
		this.endLiveNum = endLiveNum;
	}

	public void setResult(ResultType type) {
		this.result = type;
	}

	public int[] getInitHpMaxes() {
		return initHpMaxes;
	}

	public void setInitHpMaxes(int[] initHpMaxes) {
		this.initHpMaxes = initHpMaxes;
	}

	/** 统计血量 */
	public static int[] countHp(List<Fighter> attackers, List<Fighter> defenders) {
		int[] result = new int[2];
		int initHp = 0;
		for (Fighter attacker : attackers) {
			initHp += attacker.getTotalHp();
		}
		result[0] = initHp;
		initHp = 0;
		for (Fighter defender : defenders) {
			initHp += defender.getTotalHp();
		}
		result[1] = initHp;
		return result;
	}

	/** 统计最大血量 */
	public static int[] countHpMax(List<Fighter> attackers, List<Fighter> defenders) {
		int[] result = new int[2];
		int initHpMax = 0;
		for (Fighter attacker : attackers) {
			initHpMax += attacker.getTotalHpMax();
		}
		result[0] = initHpMax;
		initHpMax = 0;
		for (Fighter defender : defenders) {
			initHpMax += defender.getTotalHpMax();
		}
		result[1] = initHpMax;
		return result;
	}

	/** 统计存活单位数量 */
	public static int[] countLiveNum(List<Fighter> attackers, List<Fighter> defenders) {
		int[] result = new int[2];
		int liveNum = 0;
		for (Fighter attacker : attackers) {
			liveNum += attacker.getLive();
		}
		result[0] = liveNum;
		liveNum = 0;
		for (Fighter defender : defenders) {
			liveNum += defender.getLive();
		}
		result[1] = liveNum;
		return result;
	}

	public static BattleResult valueOf(List<Fighter> attackers, List<Fighter> defenders) {
		BattleResult result = new BattleResult();
		result.initHps = countHp(attackers, defenders);
		result.initHpMaxes = countHpMax(attackers, defenders);
		result.initLiveNum = countLiveNum(attackers, defenders);
		return result;
	}
}
