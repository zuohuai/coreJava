package com.edu.game.jct.fight.model;

import java.util.LinkedList;
import java.util.List;

import com.edu.game.jct.fight.model.report.InitReport;
import com.edu.game.jct.fight.service.Battle;
import com.edu.game.jct.fight.service.config.BattleType;

/**
 * 战斗信息
 * @author administrator
 */
public class BattleInfo {
	/** 战斗类型 */
	private BattleType type;
	/** 攻击方信息 */
	private FighterInfo attacker;
	/** 防守方信息 */
	private FighterInfo defender;
	/** 回合数 */
	private int round;
	/** 初始化效果信息 */
	private List<InitReport> inits;

	// Getter and Setter ...

	public BattleType getType() {
		return type;
	}

	public void setType(BattleType type) {
		this.type = type;
	}

	public FighterInfo getAttacker() {
		return attacker;
	}

	public void setAttacker(FighterInfo attacker) {
		this.attacker = attacker;
	}

	public FighterInfo getDefender() {
		return defender;
	}

	public void setDefender(FighterInfo defender) {
		this.defender = defender;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public List<InitReport> getInits() {
		return inits;
	}

	public void setInits(List<InitReport> inits) {
		this.inits = inits;
	}

	// Static's Method

	/** 构造方法 */
	public static BattleInfo valueOf(Battle battle) {
		BattleInfo result = new BattleInfo();
		result.type = battle.getType();
		result.attacker = battle.getAttacker().createFighterInfo();
		result.defender = battle.getDefender().createFighterInfo();
		result.round = battle.getRound();
		return result;
	}

	public static BattleInfo valueOf(BattleType type, FighterInfo attacker, FighterInfo defender, int round) {
		BattleInfo result = new BattleInfo();
		result.type = type;
		result.attacker = attacker;
		result.defender = defender;
		result.round = round;
		result.inits = new LinkedList<InitReport>();
		return result;
	}

}
