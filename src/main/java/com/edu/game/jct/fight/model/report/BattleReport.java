package com.edu.game.jct.fight.model.report;

import java.util.ArrayList;
import java.util.List;

import com.edu.game.jct.fight.model.BattleInfo;
import com.edu.game.jct.fight.model.FighterInfo;
import com.edu.game.jct.fight.service.config.BattleType;
import com.edu.game.jct.fight.service.core.Fighter;
import com.edu.utils.json.JsonUtils;


/**
 * 战报对象
 * @author Frank
 */
public class BattleReport {

	/** 初始化战斗时的 */
	private BattleInfo battleInfo;
	/** 各回合的战报 */
	private List<RoundReport> rounds = new ArrayList<RoundReport>();

	/** 获取初始化战斗单位 */
	public FighterInfo[] getInitFighters() {
		return new FighterInfo[] { battleInfo.getAttacker(), battleInfo.getDefender() };
	}

	/** 添加初始化战报 */
	public void addInit(InitReport report) {
		battleInfo.getInits().add(report);
	}

	/** 添加回合战报信息 */
	public void addRound(RoundReport report) {
		rounds.add(report);
	}

	// Getter and Setter ...

	public List<RoundReport> getRounds() {
		return rounds;
	}

	public BattleInfo getBattleInfo() {
		return battleInfo;
	}

	public void setBattleInfo(BattleInfo battleInfo) {
		this.battleInfo = battleInfo;
	}

	protected void setRounds(List<RoundReport> rounds) {
		this.rounds = rounds;
	}

	@Override
	public String toString() {
		return JsonUtils.object2String(this);
	}

	// Static Method's ...
	/** 构造方法 */
	public static BattleReport valueOf(Fighter attacker, Fighter defender, BattleType type) {
		BattleReport result = new BattleReport();
		result.battleInfo = BattleInfo.valueOf(type, attacker.createFighterInfo(), defender.createFighterInfo(), 0);
		return result;
	}

}
