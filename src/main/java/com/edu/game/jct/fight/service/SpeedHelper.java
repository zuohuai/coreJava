package com.edu.game.jct.fight.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.edu.game.jct.fight.service.config.SpeedType;
import com.edu.game.jct.fight.service.core.Fighter;
import com.edu.game.jct.fight.service.core.Unit;

/**
 * 出手速度选择处理类
 * @author qu.yy
 */
public final class SpeedHelper {

	public static List<Unit> select(SpeedType type, Fighter attacker, Fighter defender) {
		List<Unit> result = new ArrayList<Unit>();
		switch (type) {
		case PVE:
			Collections.addAll(result, attacker.getAllLive().toArray(new Unit[0]));
			Collections.addAll(result, defender.getAllLive().toArray(new Unit[0]));
			Collections.sort(result, Unit.COMPARATOR_SPEED);
			return result;
		case PVP:
			// 攻防速度排序和守方速度排序，然后根据攻防开始穿插
			List<Unit> attacks = new ArrayList<Unit>(attacker.getAllLive());
			Collections.sort(attacks, Unit.COMPARATOR_SPEED);
			List<Unit> defends = new ArrayList<Unit>(defender.getAllLive());
			Collections.sort(defends, Unit.COMPARATOR_SPEED);
			while (true) {
				if (attacks.size() > 0) {
					result.add(attacks.remove(0));
				}
				if (defends.size() > 0) {
					result.add(defends.remove(0));
				}
				if (attacks.size() == 0 && defends.size() == 0) {
					break;
				}
			}
			return result;
		default:
			throw new IllegalArgumentException("[" + type.name() + "]的出手类型不支持！");
		}
	}

}
