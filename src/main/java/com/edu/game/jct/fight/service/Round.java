package com.edu.game.jct.fight.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edu.game.jct.fight.model.FighterInfo;
import com.edu.game.jct.fight.model.UnitState;
import com.edu.game.jct.fight.model.report.CdInfo;
import com.edu.game.jct.fight.model.report.EndReport;
import com.edu.game.jct.fight.model.report.RoundReport;
import com.edu.game.jct.fight.model.report.SkillReport;
import com.edu.game.jct.fight.model.report.StartReport;
import com.edu.game.jct.fight.model.report.UltimateReport;
import com.edu.game.jct.fight.service.core.EndAction;
import com.edu.game.jct.fight.service.core.Fighter;
import com.edu.game.jct.fight.service.core.Phase;
import com.edu.game.jct.fight.service.core.SkillAction;
import com.edu.game.jct.fight.service.core.StartAction;
import com.edu.game.jct.fight.service.core.Unit;
import com.edu.game.jct.fight.service.effect.passive.PassiveState;
import com.edu.game.jct.fight.service.effect.round.RoundSkill;
import com.edu.game.jct.fight.service.effect.round.RoundSkillFactory;
import com.edu.game.jct.fight.service.effect.round.RoundSkillState;
import com.edu.game.jct.fight.service.effect.skill.SkillState;

/**
 * 回合对象
 * @author administrator
 */
public class Round {

	private static final Logger logger = LoggerFactory.getLogger(Round.class);

	/** 所属战斗 */
	private Battle battle;
	/** 当前回合数 */
	private int round;
	/** 超时处理信息 */
	private OvertimeWork otw;
	/** 该回合的战报 */
	private RoundReport report;

	/** 构造器 */
	private Round(Battle battle) {
		this.battle = battle;
		this.round = battle.getRound();
		BattleConfig config = battle.getConfig();
		Date overtime = config.getRoundOvertime();
		if (overtime != null) {
			this.otw = OvertimeWork.valueOfRound(battle.getId(), this.round, overtime);
		}
		this.report = RoundReport.valueOf(this.round);
	}

	/**
	 * 执行当前回合的战斗运算
	 */
	public void execute() {
		BattleCallback callback = battle.getCallback();
		Object obj = callback.getLockObject();
		if (obj != null) {
			// 带锁的处理,需要同步战斗单元与做战斗结束判断(对世界BOSS这类玩法的特别处理)
			synchronized (obj) {
				Fighter defender = battle.getDefender();
				Collection<Unit> refreshs = callback.startRefresh(defender);
				for (Unit unit : refreshs) {
					report.addRefresh(unit);
				}
				// 如果已经死光就不处理了
				if (defender.isAllDead()) {
					return;
				}
				// 没死光按正常流程处理
				boolean flag = doRoundStart();
				if (flag) {
					doActions();
					doRoundEnd();
				}
				callback.endRefresh(defender, battle);
			}
		} else {
			// 不带锁的处理
			boolean flag = doRoundStart();
			if (flag) {
				doActions();
				doRoundEnd();
			}
		}
	}

	/** 回合开始阶段(该阶段用于执行回合技能，先攻击方再到防守方) */
	private boolean doRoundStart() {
		RoundSkillFactory factory = RoundSkillFactory.getInstance();
		// 先执行攻击方的回合技能
		Fighter owner = battle.getAttacker();
		if (owner.getSkillStates() != null) {
			for (Entry<String, RoundSkillState> entry : owner.getSkillStates().entrySet()) {
				String id = entry.getKey();
				RoundSkillState state = entry.getValue();
				RoundSkill skill = factory.getRoundSkill(id);
				if (!skill.isVaild(state, this)) {
					// 无效技能直接跳过
					continue;
				}
				// 执行回合技能
				UltimateReport roundSkillReport = skill.execute(state, owner, this);
				if (roundSkillReport != null) {
					report.addUltimate(roundSkillReport);
				}
				// 添加结束标识
				state.endRound();
			}
		}

		// 再到防守方的回合技能
		owner = battle.getDefender();
		if (owner.isAllDead()) {
			// 如果防守方全部死亡，则直接返回
			return false;
		}
		if (owner.getSkillStates() != null) {
			for (Entry<String, RoundSkillState> entry : owner.getSkillStates().entrySet()) {
				String id = entry.getKey();
				RoundSkillState state = entry.getValue();
				RoundSkill skill = factory.getRoundSkill(id);
				if (!skill.isVaild(state, this)) {
					// 无效技能直接跳过
					continue;
				}
				// 执行回合技能
				UltimateReport roundSkillReport = skill.execute(state, owner, this);
				if (roundSkillReport != null) {
					report.addUltimate(roundSkillReport);
				}
				// 添加结束标识
				state.endRound();
			}
		}
		if (battle.getAttacker().isAllDead()) {
			// 如果攻击方全部死亡，则直接返回
			return false;
		}
		// 执行所有活人的回合开始被动效果
		for (Unit unit : battle.getAllLive()) {
			List<PassiveState> states = unit.getPassiveState(Phase.ROUND_START);
			if (states.isEmpty()) {
				continue;
			}
			StartAction action = StartAction.valueOf(unit, states);
			StartReport startReport = action.execute();
			if (startReport != null) {
				report.addStart(startReport);
			}
		}
		return true;
	}

	/** 执行全部活着的出战单元的行动 */
	private void doActions() {
		// 获取全部活着的出战单元，并进行速度排序
		List<Unit> units = SpeedHelper.select(battle.getConfig().getSeepType(), battle.getAttacker(),
				battle.getDefender());
		// 执行每个出战单元的主动技能
		for (Unit u : units) {
			// 跳过已经死亡或禁止行动的出战单元
			if (u.isDead() || u.hasState(UnitState.DISABLE)) {
				continue;
			}
			// 对未死亡的出战单元，执行技能运算
			Fighter friend = battle.getFriend(u);
			Fighter enemy = battle.getEnemy(u);
			SkillState state = u.getCurrentSkill(friend, enemy);
			// 没有可执行的技能直接跳过
			if (state == null) {
				continue;
			}
			// 构建对应的行动对象，并执行行动
			SkillAction action = SkillAction.valueOf(u, state, friend, enemy);
			SkillReport actionReport = action.execute(battle.getType());
			if (actionReport != null) {
				report.addAction(actionReport);
			}
		}
	}

	/** 补充成员与BUFF运算阶段(按出手顺序进行战斗单元上的BUFF运算) */
	private void doRoundEnd() {
		Fighter attacker = battle.getAttacker();
		Fighter defender = battle.getDefender();
		// 刷新技能CD信息
		Collection<CdInfo> cdInfos = attacker.refreshSkillCd();
		report.addCdInfo(cdInfos);
		cdInfos = defender.refreshSkillCd();
		report.addCdInfo(cdInfos);
		// 进行补员处理
		if (attacker.isAllDead() && attacker.hasRelief()) {
			attacker.next();
			FighterInfo relief = attacker.createFighterInfo();
			report.addAttackerRelief(relief);
		}
		if (defender.isAllDead() && defender.hasRelief()) {
			defender.next();
			FighterInfo relief = defender.createFighterInfo();
			report.addDefenderRelief(relief);
		}
		// 如果任意一方全体死亡，则跳过BUFF运算阶段
		if (attacker.isAllDead() || defender.isAllDead()) {
			return;
		}

		executeRoundEnd();

		// 进行补员处理
		if (attacker.isAllDead() && attacker.hasRelief()) {
			attacker.next();
			FighterInfo relief = attacker.createFighterInfo();
			report.addAttackerRelief(relief);
		}
		if (defender.isAllDead() && defender.hasRelief()) {
			defender.next();
			FighterInfo relief = defender.createFighterInfo();
			report.addDefenderRelief(relief);
		}
	}

	/** 执行回合结束的行动 */
	void executeRoundEnd() {
		// 进行全体活人的BUFF运算
		for (Unit unit : battle.getAllUnits()) {
			// 跳过没有BUFF的单元
			if (!unit.hasBuff() && !unit.hasPassive(Phase.ROUND_END)) {
				continue;
			}
			// 跳过死了没有复活的单元
			if (unit.isDead() && !unit.hasPassive(Phase.ROUND_END)) {
				continue;
			}
			EndAction action = EndAction.valueOf(unit);
			EndReport endReport = action.execute();
			if (endReport != null) {
				report.addEnd(endReport);
			}
		}
	}

	/**
	 * 获取回合超时处理对象
	 * @return 不存在返回null
	 */
	public OvertimeWork getOtw() {
		return otw;
	}

	// Getter and Setter ...

	public Battle getBattle() {
		return battle;
	}

	public int getRound() {
		return round;
	}

	public RoundReport getReport() {
		return report;
	}

	public void setReport(RoundReport report) {
		this.report = report;
	}

	public void setBattle(Battle battle) {
		this.battle = battle;
	}

	public void setRound(int round) {
		this.round = round;
	}

	// Static Method's ...

	/** 构造方法 */
	public static Round valueOf(Battle battle) {
		Round round = new Round(battle);
		return round;
	}
}
