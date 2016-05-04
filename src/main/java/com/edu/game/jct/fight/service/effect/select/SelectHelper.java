package com.edu.game.jct.fight.service.effect.select;

import static com.edu.game.jct.fight.service.effect.select.SelectType.ENEMY_ALL;
import static com.edu.game.jct.fight.service.effect.select.SelectType.ENEMY_BACK_ALL;
import static com.edu.game.jct.fight.service.effect.select.SelectType.ENEMY_BACK_ONE;
import static com.edu.game.jct.fight.service.effect.select.SelectType.ENEMY_DEAD_ALL;
import static com.edu.game.jct.fight.service.effect.select.SelectType.ENEMY_DEAD_ONE;
import static com.edu.game.jct.fight.service.effect.select.SelectType.ENEMY_FIVE;
import static com.edu.game.jct.fight.service.effect.select.SelectType.ENEMY_FOUR;
import static com.edu.game.jct.fight.service.effect.select.SelectType.ENEMY_FRONT_ALL;
import static com.edu.game.jct.fight.service.effect.select.SelectType.ENEMY_FRONT_ONE;
import static com.edu.game.jct.fight.service.effect.select.SelectType.ENEMY_HP_MAX;
import static com.edu.game.jct.fight.service.effect.select.SelectType.ENEMY_HP_MIN;
import static com.edu.game.jct.fight.service.effect.select.SelectType.ENEMY_HP_SCALE_MAX;
import static com.edu.game.jct.fight.service.effect.select.SelectType.ENEMY_HP_SCALE_MIN;
import static com.edu.game.jct.fight.service.effect.select.SelectType.ENEMY_HP_SCALE_MIN_TWO;
import static com.edu.game.jct.fight.service.effect.select.SelectType.ENEMY_HP_TWO;
import static com.edu.game.jct.fight.service.effect.select.SelectType.ENEMY_ONE;
import static com.edu.game.jct.fight.service.effect.select.SelectType.ENEMY_PLAYER;
import static com.edu.game.jct.fight.service.effect.select.SelectType.ENEMY_ROW;
import static com.edu.game.jct.fight.service.effect.select.SelectType.ENEMY_SPEED_MAX;
import static com.edu.game.jct.fight.service.effect.select.SelectType.ENEMY_SPEED_MIN;
import static com.edu.game.jct.fight.service.effect.select.SelectType.ENEMY_THREE;
import static com.edu.game.jct.fight.service.effect.select.SelectType.ENEMY_TWO;
import static com.edu.game.jct.fight.service.effect.select.SelectType.FRIEND_ALL;
import static com.edu.game.jct.fight.service.effect.select.SelectType.FRIEND_BACK_ALL;
import static com.edu.game.jct.fight.service.effect.select.SelectType.FRIEND_BACK_ONE;
import static com.edu.game.jct.fight.service.effect.select.SelectType.FRIEND_DEAD_ALL;
import static com.edu.game.jct.fight.service.effect.select.SelectType.FRIEND_DEAD_ONE;
import static com.edu.game.jct.fight.service.effect.select.SelectType.FRIEND_FIVE;
import static com.edu.game.jct.fight.service.effect.select.SelectType.FRIEND_FOUR;
import static com.edu.game.jct.fight.service.effect.select.SelectType.FRIEND_FRONT_ALL;
import static com.edu.game.jct.fight.service.effect.select.SelectType.FRIEND_FRONT_ONE;
import static com.edu.game.jct.fight.service.effect.select.SelectType.FRIEND_HP_MAX;
import static com.edu.game.jct.fight.service.effect.select.SelectType.FRIEND_HP_MIN;
import static com.edu.game.jct.fight.service.effect.select.SelectType.FRIEND_HP_SCALE_MAX;
import static com.edu.game.jct.fight.service.effect.select.SelectType.FRIEND_HP_SCALE_MIN;
import static com.edu.game.jct.fight.service.effect.select.SelectType.FRIEND_HP_SCALE_MIN_TWO;
import static com.edu.game.jct.fight.service.effect.select.SelectType.FRIEND_HP_TWO;
import static com.edu.game.jct.fight.service.effect.select.SelectType.FRIEND_ONE;
import static com.edu.game.jct.fight.service.effect.select.SelectType.FRIEND_PLAYER;
import static com.edu.game.jct.fight.service.effect.select.SelectType.FRIEND_ROW;
import static com.edu.game.jct.fight.service.effect.select.SelectType.FRIEND_SPEED_MAX;
import static com.edu.game.jct.fight.service.effect.select.SelectType.FRIEND_SPEED_MIN;
import static com.edu.game.jct.fight.service.effect.select.SelectType.FRIEND_THREE;
import static com.edu.game.jct.fight.service.effect.select.SelectType.FRIEND_TWO;
import static com.edu.game.jct.fight.service.effect.select.SelectType.MYSELF;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edu.game.Player;
import com.edu.game.jct.fight.model.Position;
import com.edu.game.jct.fight.model.UnitState;
import com.edu.game.jct.fight.model.UnitValue;
import com.edu.game.jct.fight.service.core.Fighter;
import com.edu.game.jct.fight.service.core.Unit;
import com.edu.utils.RandomUtils;

/**
 * 目标选择帮助类
 * @author administrator
 */
@SuppressWarnings("unchecked")
public final class SelectHelper {

	public final static Logger logger = LoggerFactory.getLogger(SelectHelper.class);

	/** 反转目标关系 */
	private static final Map<SelectType, SelectType> REVERSE_TYPE = new HashMap<SelectType, SelectType>(
			SelectType.values().length);

	static {
		REVERSE_TYPE.put(ENEMY_ONE, FRIEND_ONE);
		REVERSE_TYPE.put(ENEMY_ALL, FRIEND_ALL);
		REVERSE_TYPE.put(ENEMY_FRONT_ONE, FRIEND_FRONT_ONE);
		REVERSE_TYPE.put(ENEMY_FRONT_ALL, FRIEND_FRONT_ALL);
		REVERSE_TYPE.put(ENEMY_BACK_ONE, FRIEND_BACK_ONE);
		REVERSE_TYPE.put(ENEMY_BACK_ALL, FRIEND_BACK_ALL);
		REVERSE_TYPE.put(ENEMY_ROW, FRIEND_ROW);
		REVERSE_TYPE.put(ENEMY_HP_MIN, FRIEND_HP_MIN);
		REVERSE_TYPE.put(ENEMY_HP_TWO, FRIEND_HP_TWO);
		REVERSE_TYPE.put(ENEMY_HP_MAX, FRIEND_HP_MAX);
		REVERSE_TYPE.put(ENEMY_HP_SCALE_MIN, FRIEND_HP_SCALE_MIN);
		REVERSE_TYPE.put(ENEMY_HP_SCALE_MIN_TWO, FRIEND_HP_SCALE_MIN_TWO);
		REVERSE_TYPE.put(ENEMY_HP_SCALE_MAX, FRIEND_HP_SCALE_MAX);
		REVERSE_TYPE.put(ENEMY_SPEED_MIN, FRIEND_SPEED_MIN);
		REVERSE_TYPE.put(ENEMY_SPEED_MAX, FRIEND_SPEED_MAX);
		REVERSE_TYPE.put(ENEMY_DEAD_ONE, FRIEND_DEAD_ONE);
		REVERSE_TYPE.put(ENEMY_DEAD_ALL, FRIEND_DEAD_ALL);
		REVERSE_TYPE.put(ENEMY_PLAYER, FRIEND_PLAYER);
		REVERSE_TYPE.put(ENEMY_TWO, FRIEND_TWO);
		REVERSE_TYPE.put(ENEMY_THREE, FRIEND_THREE);
		REVERSE_TYPE.put(ENEMY_FOUR, FRIEND_FOUR);
		REVERSE_TYPE.put(ENEMY_FIVE, FRIEND_FIVE);
		//
		REVERSE_TYPE.put(FRIEND_ONE, ENEMY_ONE);
		REVERSE_TYPE.put(FRIEND_ALL, ENEMY_ALL);
		REVERSE_TYPE.put(FRIEND_FRONT_ONE, ENEMY_FRONT_ONE);
		REVERSE_TYPE.put(FRIEND_FRONT_ALL, ENEMY_FRONT_ALL);
		REVERSE_TYPE.put(FRIEND_BACK_ONE, ENEMY_BACK_ONE);
		REVERSE_TYPE.put(FRIEND_BACK_ALL, ENEMY_BACK_ALL);
		REVERSE_TYPE.put(FRIEND_ROW, ENEMY_ROW);
		REVERSE_TYPE.put(FRIEND_HP_MIN, ENEMY_HP_MIN);
		REVERSE_TYPE.put(FRIEND_HP_TWO, ENEMY_HP_TWO);
		REVERSE_TYPE.put(FRIEND_HP_MAX, ENEMY_HP_MAX);
		REVERSE_TYPE.put(FRIEND_HP_SCALE_MIN, ENEMY_HP_SCALE_MIN);
		REVERSE_TYPE.put(FRIEND_HP_SCALE_MIN_TWO, ENEMY_HP_SCALE_MIN_TWO);
		REVERSE_TYPE.put(FRIEND_HP_SCALE_MAX, ENEMY_HP_SCALE_MAX);
		REVERSE_TYPE.put(FRIEND_SPEED_MIN, ENEMY_SPEED_MIN);
		REVERSE_TYPE.put(FRIEND_SPEED_MAX, ENEMY_SPEED_MAX);
		REVERSE_TYPE.put(FRIEND_DEAD_ONE, ENEMY_DEAD_ONE);
		REVERSE_TYPE.put(FRIEND_DEAD_ALL, ENEMY_DEAD_ALL);
		REVERSE_TYPE.put(FRIEND_PLAYER, ENEMY_PLAYER);
		REVERSE_TYPE.put(MYSELF, ENEMY_ONE);
		REVERSE_TYPE.put(FRIEND_TWO, ENEMY_TWO);
		REVERSE_TYPE.put(FRIEND_THREE, ENEMY_THREE);
		REVERSE_TYPE.put(FRIEND_FOUR, ENEMY_FOUR);
		REVERSE_TYPE.put(FRIEND_FIVE, ENEMY_FIVE);

	}

	private SelectHelper() {
	}

	/**
	 * 获取选择目标的反目标
	 * @param type 选择目标
	 * @return
	 */
	public static SelectType getReverseType(SelectType type) {
		return REVERSE_TYPE.get(type);
	}

	/**
	 * 执行目标选择
	 * @param type 选择方式
	 * @param owner 技能所有者
	 * @param friend 友方
	 * @param enemy 敌方
	 * @return
	 */
	public static List<Unit> select(SelectType type, Unit owner, Fighter friend, Fighter enemy) {
		switch (type) {
		case ENEMY_ONE:
			return selectAnyOne(selectAll(enemy, UnitState.DEAD, UnitState.UNVISUAL));
		case FRIEND_ONE:
			return selectAnyOne(selectAll(friend, UnitState.DEAD, UnitState.UNVISUAL));
		case ENEMY_ALL:
			return selectAll(enemy, UnitState.DEAD, UnitState.UNVISUAL);
		case FRIEND_ALL:
			return selectAll(friend, UnitState.DEAD, UnitState.UNVISUAL);
		case ENEMY_FRONT_ONE:
			return selectAnyOne(selectByColumn(enemy, true, UnitState.DEAD, UnitState.UNVISUAL));
		case FRIEND_FRONT_ONE:
			return selectAnyOne(selectByColumn(friend, true, UnitState.DEAD, UnitState.UNVISUAL));
		case ENEMY_FRONT_ALL:
			return selectByColumn(enemy, true, UnitState.DEAD, UnitState.UNVISUAL);
		case FRIEND_FRONT_ALL:
			return selectByColumn(friend, true, UnitState.DEAD, UnitState.UNVISUAL);
		case ENEMY_BACK_ONE:
			return selectAnyOne(selectByColumn(enemy, false, UnitState.DEAD, UnitState.UNVISUAL));
		case FRIEND_BACK_ONE:
			return selectAnyOne(selectByColumn(friend, false, UnitState.DEAD, UnitState.UNVISUAL));
		case ENEMY_BACK_ALL:
			return selectByColumn(enemy, false, UnitState.DEAD, UnitState.UNVISUAL);
		case FRIEND_BACK_ALL:
			return selectByColumn(friend, false, UnitState.DEAD, UnitState.UNVISUAL);
		case ENEMY_ROW:
			return selectByRow(enemy, UnitState.DEAD, UnitState.UNVISUAL);
		case FRIEND_ROW:
			return selectByRow(friend, UnitState.DEAD, UnitState.UNVISUAL);
		case ENEMY_HP_MIN:
			return selectByValue(enemy, UnitValue.HP, true, UnitState.DEAD, UnitState.UNVISUAL);
		case ENEMY_HP_TWO:
			return selectByValueTwo(enemy, UnitValue.HP, true, UnitState.DEAD, UnitState.UNVISUAL);
		case FRIEND_HP_MIN:
			return selectByValue(friend, UnitValue.HP, true, UnitState.DEAD, UnitState.UNVISUAL);
		case FRIEND_HP_TWO:
			return selectByValueTwo(friend, UnitValue.HP, true, UnitState.DEAD, UnitState.UNVISUAL);
		case ENEMY_HP_MAX:
			return selectByValue(enemy, UnitValue.HP, false, UnitState.DEAD, UnitState.UNVISUAL);
		case FRIEND_HP_MAX:
			return selectByValue(friend, UnitValue.HP, false, UnitState.DEAD, UnitState.UNVISUAL);
		case ENEMY_HP_SCALE_MIN:
			return selectByHpScale(enemy, true, UnitState.DEAD, UnitState.UNVISUAL);
		case FRIEND_HP_SCALE_MIN:
			return selectByHpScale(friend, true, UnitState.DEAD, UnitState.UNVISUAL);
		case ENEMY_HP_SCALE_MIN_TWO:
			return selectByHpScaleTwo(enemy, true, UnitState.DEAD, UnitState.UNVISUAL);
		case FRIEND_HP_SCALE_MIN_TWO:
			return selectByHpScaleTwo(friend, true, UnitState.DEAD, UnitState.UNVISUAL);
		case ENEMY_HP_SCALE_MAX:
			return selectByHpScale(enemy, false, UnitState.DEAD, UnitState.UNVISUAL);
		case FRIEND_HP_SCALE_MAX:
			return selectByHpScale(friend, false, UnitState.DEAD, UnitState.UNVISUAL);
		case ENEMY_SPEED_MIN:
			return selectByValue(enemy, UnitValue.SPEED, true, UnitState.DEAD, UnitState.UNVISUAL);
		case FRIEND_SPEED_MIN:
			return selectByValue(friend, UnitValue.SPEED, true, UnitState.DEAD, UnitState.UNVISUAL);
		case ENEMY_SPEED_MAX:
			return selectByValue(enemy, UnitValue.SPEED, false, UnitState.DEAD, UnitState.UNVISUAL);
		case FRIEND_SPEED_MAX:
			return selectByValue(friend, UnitValue.SPEED, false, UnitState.DEAD, UnitState.UNVISUAL);
		case ENEMY_DEAD_ALL:
			return selectByState(enemy, UnitState.DEAD);
		case FRIEND_DEAD_ALL:
			return selectByState(friend, UnitState.DEAD);
		case ENEMY_DEAD_ONE:
			return selectAnyOne(selectByState(enemy, UnitState.DEAD));
		case FRIEND_DEAD_ONE:
			return selectAnyOne(selectByState(friend, UnitState.DEAD));
		case ENEMY_PLAYER:
			return selectPlayer(enemy, UnitState.DEAD, UnitState.UNVISUAL);
		case FRIEND_PLAYER:
			return selectPlayer(friend, UnitState.DEAD, UnitState.UNVISUAL);
		case MYSELF:
			List<Unit> result = new ArrayList<Unit>(1);
			result.add(owner);
			return result;
		case ENEMY_TWO:
			return selectAnyMore(selectAll(enemy, UnitState.DEAD, UnitState.UNVISUAL), 2);
		case FRIEND_TWO:
			return selectAnyMore(selectAll(friend, UnitState.DEAD, UnitState.UNVISUAL), 2);
		case ENEMY_THREE:
			return selectAnyMore(selectAll(enemy, UnitState.DEAD, UnitState.UNVISUAL), 3);
		case FRIEND_THREE:
			return selectAnyMore(selectAll(friend, UnitState.DEAD, UnitState.UNVISUAL), 3);
		case ENEMY_FOUR:
			return selectAnyMore(selectAll(enemy, UnitState.DEAD, UnitState.UNVISUAL), 4);
		case FRIEND_FOUR:
			return selectAnyMore(selectAll(friend, UnitState.DEAD, UnitState.UNVISUAL), 4);
		case ENEMY_FIVE:
			return selectAnyMore(selectAll(enemy, UnitState.DEAD, UnitState.UNVISUAL), 5);
		case FRIEND_FIVE:
			return selectAnyMore(selectAll(friend, UnitState.DEAD, UnitState.UNVISUAL), 5);
		default:
			return Collections.EMPTY_LIST;
		}
	}

	private static List<Unit> selectPlayer(Fighter fighter, int... ignoreStates) {
		Map<Player, Unit> owners = fighter.getOwners();
		if (owners.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		List<Unit> result = new ArrayList<Unit>(owners.size());
		OUTER:
		for (Unit unit : owners.values()) {
			for (int state : ignoreStates) {
				if (unit.hasState(state)) {
					continue OUTER;
				}
			}
			result.add(unit);
		}
		return result;
	}

	private static List<Unit> selectByState(Fighter fighter, int state) {
		List<Unit> result = new ArrayList<Unit>();
		Unit[][] array = fighter.getCurrents();
		for (Unit[] us : array) {
			for (Unit unit : us) {
				if (unit == null) {
					continue;
				}
				if (unit.hasState(state)) {
					result.add(unit);
				}
			}
		}
		return result;
	}

	private static List<Unit> selectByValue(Fighter fighter, UnitValue type, boolean isMin, int... ignoreStates) {
		Unit target = null;
		Integer current = null;
		Unit[][] array = fighter.getCurrents();
		for (int r = 0; r < Position.ROW_SIZE; r++) {
			OUTER:
			for (int c = 0; c < Position.COLUMN_SIZE; c++) {
				Unit unit = array[r][c];
				if (unit == null) {
					continue;
				}
				for (int state : ignoreStates) {
					if (unit.hasState(state)) {
						continue OUTER;
					}
				}
				if (target != null) {
					int value = unit.getValue(type);
					if (isMin && value > current) {
						continue;
					} else if (!isMin && value < current) {
						continue;
					}
				}
				target = unit;
				current = unit.getValue(type);
			}
		}
		if (target == null) {
			return Collections.EMPTY_LIST;
		}
		List<Unit> result = new ArrayList<Unit>(1);
		result.add(target);
		return result;
	}

	private static List<Unit> selectByValueTwo(Fighter fighter, UnitValue type, boolean isMin, int... ignoreStates) {
		Unit target = null;
		Integer current = null;
		Unit[][] array = fighter.getCurrents();
		for (int r = 0; r < Position.ROW_SIZE; r++) {
			OUTER:
			for (int c = 0; c < Position.COLUMN_SIZE; c++) {
				Unit unit = array[r][c];
				if (unit == null) {
					continue;
				}
				for (int state : ignoreStates) {
					if (unit.hasState(state)) {
						continue OUTER;
					}
				}
				if (target != null) {
					int value = unit.getValue(type);
					if (isMin && value > current) {
						continue;
					} else if (!isMin && value < current) {
						continue;
					}
				}
				target = unit;
				current = unit.getValue(type);
			}
		}
		if (target == null) {
			return Collections.EMPTY_LIST;
		}
		// 二次选择
		Unit twoTarget = null;
		for (int r = 0; r < Position.ROW_SIZE; r++) {
			OUTER:
			for (int c = 0; c < Position.COLUMN_SIZE; c++) {
				Unit unit = array[r][c];
				if (unit == null) {
					continue;
				}
				for (int state : ignoreStates) {
					if (unit.hasState(state)) {
						continue OUTER;
					}
				}
				// 排除掉第一次被选中的目标
				if (target.getId().equals(unit.getId())) {
					continue;
				}
				if (twoTarget != null) {
					int value = unit.getValue(type);
					if (isMin && value > current) {
						continue;
					} else if (!isMin && value < current) {
						continue;
					}
				}
				twoTarget = unit;
				current = unit.getValue(type);
			}
		}
		List<Unit> result = new ArrayList<Unit>(1);
		result.add(target);
		if (twoTarget != null) {
			result.add(twoTarget);
		}
		return result;
	}

	private static List<Unit> selectByHpScale(Fighter fighter, boolean isMin, int... ignoreStates) {
		Unit target = null;
		double current = 0;
		Unit[][] array = fighter.getCurrents();
		for (int r = 0; r < Position.ROW_SIZE; r++) {
			OUTER:
			for (int c = 0; c < Position.COLUMN_SIZE; c++) {
				Unit unit = array[r][c];
				if (unit == null) {
					continue;
				}
				for (int state : ignoreStates) {
					if (unit.hasState(state)) {
						continue OUTER;
					}
				}
				double value = (double) unit.getValue(UnitValue.HP) / unit.getValue(UnitValue.HP_MAX);
				if (target != null) {
					if (isMin && value > current) {
						continue;
					} else if (!isMin && value < current) {
						continue;
					}
				}
				target = unit;
				current = value;
			}
		}
		if (target == null) {
			return Collections.EMPTY_LIST;
		}
		List<Unit> result = new ArrayList<Unit>(1);
		result.add(target);
		return result;
	}

	private static List<Unit> selectByHpScaleTwo(Fighter fighter, boolean isMin, int... ignoreStates) {
		Unit target = null;
		double current = 0;
		Unit[][] array = fighter.getCurrents();
		for (int r = 0; r < Position.ROW_SIZE; r++) {
			OUTER:
			for (int c = 0; c < Position.COLUMN_SIZE; c++) {
				Unit unit = array[r][c];
				if (unit == null) {
					continue;
				}
				for (int state : ignoreStates) {
					if (unit.hasState(state)) {
						continue OUTER;
					}
				}
				double value = (double) unit.getValue(UnitValue.HP) / unit.getValue(UnitValue.HP_MAX);
				if (target != null) {
					if (isMin && value > current) {
						continue;
					} else if (!isMin && value < current) {
						continue;
					}
				}
				target = unit;
				current = value;
			}
		}
		if (target == null) {
			return Collections.EMPTY_LIST;
		}
		// 二次选择
		Unit twoTarget = null;
		for (int r = 0; r < Position.ROW_SIZE; r++) {
			OUTER:
			for (int c = 0; c < Position.COLUMN_SIZE; c++) {
				Unit unit = array[r][c];
				if (unit == null) {
					continue;
				}
				// 排除掉第一次被选中的目标
				if (target.getId().equals(unit.getId())) {
					continue;
				}
				for (int state : ignoreStates) {
					if (unit.hasState(state)) {
						continue OUTER;
					}
				}
				double value = (double) unit.getValue(UnitValue.HP) / unit.getValue(UnitValue.HP_MAX);
				if (twoTarget != null) {
					if (isMin && value > current) {
						continue;
					} else if (!isMin && value < current) {
						continue;
					}
				}
				twoTarget = unit;
				current = value;
			}
		}

		List<Unit> result = new ArrayList<Unit>(1);
		result.add(target);
		if (twoTarget != null) {
			result.add(twoTarget);
		}
		return result;
	}

	private static List<Unit> selectByRow(Fighter fighter, int... ignoreStates) {
		Unit[][] array = fighter.getCurrents();
		Map<Integer, Integer> counts = new HashMap<Integer, Integer>(Position.ROW_SIZE);
		int max = 0;
		for (int r = 0; r < Position.ROW_SIZE; r++) {
			int total = 0;
			OUTER:
			for (int c = 0; c < Position.COLUMN_SIZE; c++) {
				Unit unit = array[r][c];
				if (unit == null) {
					continue;
				}
				for (int state : ignoreStates) {
					if (unit.hasState(state)) {
						continue OUTER;
					}
				}
				total++;
			}
			counts.put(r, total);
			if (total > max) {
				max = total;
			}
		}
		if (max == 0) {
			return Collections.EMPTY_LIST;
		}

		List<Integer> maxs = new ArrayList<Integer>(Position.ROW_SIZE);
		for (Entry<Integer, Integer> entry : counts.entrySet()) {
			if (entry.getValue() == max) {
				maxs.add(entry.getKey());
			}
		}
		int idx = RandomUtils.betweenInt(0, maxs.size() - 1, true);
		int row = maxs.get(idx);
		List<Unit> result = new ArrayList<Unit>(Position.COLUMN_SIZE);
		OUTER:
		for (int c = 0; c < Position.COLUMN_SIZE; c++) {
			Unit unit = array[row][c];
			if (unit == null) {
				continue;
			}
			for (int state : ignoreStates) {
				if (unit.hasState(state)) {
					continue OUTER;
				}
			}
			result.add(unit);
		}
		return result;
	}

	private static List<Unit> selectByColumn(Fighter fighter, boolean forward, int... ignoreStates) {
		List<Unit> result = new ArrayList<Unit>();
		Unit[][] array = fighter.getCurrents();
		if (forward) {
			for (int c = 0; c < Position.COLUMN_SIZE; c++) {
				OUTER:
				for (int r = 0; r < Position.ROW_SIZE; r++) {
					Unit unit = array[r][c];
					if (unit == null) {
						continue;
					}
					for (int state : ignoreStates) {
						if (unit.hasState(state)) {
							continue OUTER;
						}
					}
					result.add(unit);
				}
				if (!result.isEmpty()) {
					break;
				}
			}
		} else {
			for (int c = Position.COLUMN_MAX; c >= Position.COLUMN_MIN; c--) {
				OUTER:
				for (int r = 0; r < Position.ROW_SIZE; r++) {
					Unit unit = array[r][c];
					if (unit == null) {
						continue;
					}
					for (int state : ignoreStates) {
						if (unit.hasState(state)) {
							continue OUTER;
						}
					}
					result.add(unit);
				}
				if (!result.isEmpty()) {
					break;
				}
			}
		}
		return result;
	}

	private static List<Unit> selectAll(Fighter fighter, int... ignoreStates) {
		List<Unit> result = new ArrayList<Unit>();
		for (Unit[] us : fighter.getCurrents()) {
			OUTER:
			for (Unit u : us) {
				if (u == null) {
					continue;
				}
				for (int state : ignoreStates) {
					if (u.hasState(state)) {
						continue OUTER;
					}
				}
				result.add(u);
			}
		}
		return result;
	}

	private static List<Unit> selectAnyOne(List<Unit> sources) {
		switch (sources.size()) {
		case 0:
			return Collections.EMPTY_LIST;
		case 1:
			return sources;
		default:
			List<Unit> result = new ArrayList<Unit>(1);
			int idx = RandomUtils.betweenInt(0, sources.size() - 1, true);
			result.add(sources.get(idx));
			return result;
		}
	}

	private static List<Unit> selectAnyMore(List<Unit> sources, int limit) {
		int size = sources.size();
		if (size == 0) {
			return Collections.EMPTY_LIST;
		}
		if (size <= limit) {
			return sources;
		}
		List<Unit> result = new ArrayList<Unit>(limit);
		for (int i = 0; i < limit; i++) {
			int idx = RandomUtils.betweenInt(0, sources.size() - 1, true);
			result.add(sources.remove(idx));
		}
		return result;
	}

}
