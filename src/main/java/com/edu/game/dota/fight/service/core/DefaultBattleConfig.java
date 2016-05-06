package com.eyu.snm.module.fight.service.core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.eyu.snm.module.fight.model.BattleResult;
import com.eyu.snm.module.fight.model.ResultType;
import com.eyu.snm.module.fight.service.config.BattleConfig;

/**
 * 默认战斗配置
 * @author shenlong
 */
@Component
public abstract class DefaultBattleConfig implements BattleConfig {

	@Override
	public int getBattleOvertime() {
		return 3 * 60 * 1000;
	}

	@Override
	public int getVerifyTime() {
		return 5 * 60 * 1000;
	}

	@Override
	public void reset(Battle battle) {
		battle.reset();
	}

	@Override
	public boolean judgeResult(Battle battle, BattleResult battleResult) {
		List<Fighter> attackers = battle.getAttackers();
		List<Fighter> defenders = battle.getDefenders();
		Fighter attacker = battle.getAttacker();
		Fighter defender = battle.getDefender();
		boolean result = false;
		if (attackers.isEmpty() && defenders.isEmpty() && attacker.isEmpty() && defender.isEmpty()) {
			battleResult.setResult(ResultType.ALL_DEAD);
			result = true;
		}

		if (attackers.isEmpty() && attacker.isEmpty()) {
			battleResult.setResult(ResultType.DEFENDER);
			result = true;
		} else if (defenders.isEmpty() && defender.isEmpty()) {
			battleResult.setResult(ResultType.ATTACKER);
			result = true;
		}

		// 配置剩余血量
		List<Fighter> allAttacker = new ArrayList<>();
		allAttacker.add(attacker);
		allAttacker.addAll(attackers);
		List<Fighter> allDefender = new ArrayList<>();
		allDefender.add(defender);
		allDefender.addAll(defenders);
		if (result) {
			battleResult.setEnd(allAttacker, allDefender);
		}
		return result;
	}

	@Override
	public void timeOut(Battle battle) {
		Fighter attacker = battle.getAttacker();
		Fighter defender = battle.getDefender();
		List<Fighter> attackers = battle.getAttackers();
		List<Fighter> defenders = battle.getDefenders();
		// 配置剩余血量
		List<Fighter> allAttacker = new ArrayList<>();
		allAttacker.add(attacker);
		allAttacker.addAll(attackers);
		List<Fighter> allDefender = new ArrayList<>();
		allDefender.add(defender);
		allDefender.addAll(defenders);
		// 设置战斗结果
		BattleResult battleResult = battle.getBattleResult();
		battleResult.setEnd(allAttacker, allDefender);
		battleResult.setResult(ResultType.TIME_OUT);
	}

	@Override
	public boolean isManual() {
		return false;
	}

}
