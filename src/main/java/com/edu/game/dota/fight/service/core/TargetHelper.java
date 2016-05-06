package com.eyu.snm.module.fight.service.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eyu.snm.module.fight.model.UnitState;
import com.eyu.snm.module.fight.model.UnitType;
import com.eyu.snm.module.fight.model.UnitValue;
import com.eyu.snm.module.fight.model.report.seed.Random;
import com.eyu.snm.utils.ExpressionHelper;

/**
 * 目标选择帮助类
 * @author Frank
 */
public class TargetHelper {

	private static final Logger logger = LoggerFactory.getLogger(TargetHelper.class);

	/** X轴查找序列(攻击方) */
	public static Integer[][] XSA = new Integer[Area.X_SIZE][];
	/** X轴查找序列(防守方) */
	public static Integer[][] XSD = new Integer[Area.X_SIZE][];
	/** Y轴查找序列 */
	public static Integer[][] YS = new Integer[Area.Y_SIZE][];

	static {
		// 生成X轴查找序列
		for (int x = 0; x < Area.X_SIZE; x++) {
			List<Integer> alist = new ArrayList<Integer>();
			List<Integer> dlist = new ArrayList<Integer>();
			alist.add(x);
			dlist.add(x);
			for (int i = 1; i < Area.X_SIZE; i++) {
				int v1 = x + i;
				int v2 = x - i;
				if (v2 >= 0) {
					dlist.add(v2);
				}
				if (v1 <= Area.X_MAX) {
					alist.add(v1);
					dlist.add(v1);
				}
				if (v2 >= 0) {
					alist.add(v2);
				}
			}
			XSA[x] = alist.toArray(new Integer[0]);
			XSD[x] = dlist.toArray(new Integer[0]);
		}
		// 生成Y轴查找序列
		for (int y = 0; y < Area.Y_SIZE; y++) {
			List<Integer> list = new ArrayList<Integer>();
			list.add(y);
			for (int i = 1; i < Area.Y_SIZE; i++) {
				int v = y - i;
				if (v >= 0) {
					list.add(v);
				}
				v = y + i;
				if (v <= Area.Y_MAX) {
					list.add(v);
				}
			}
			YS[y] = list.toArray(new Integer[0]);
		}
	}

	public static List<Unit> select(SkillOwner owner, List<Unit> targets, Selector[] selectors, Position... positions) {
		for (Selector selector : selectors) {
			targets = select(owner, targets, selector, positions);
			if (targets.isEmpty()) {
				break;
			}
		}
		return targets;
	}

	private static List<Unit> select(SkillOwner owner, List<Unit> targets, Selector selector, Position... positions) {
		switch (selector.getType()) {
		case CURRENT:
			return current(owner, selector);
		case TARGET:
			return target(owner, targets, selector);
		case MYSELF:
			return myself(owner, targets, selector);
		case OUR:
			return our(owner, targets, selector);
		case ENEMY:
			return enemy(owner, targets, selector);
		case RANGE:
			return range(owner, targets, selector);
		case CLOSEST:
			return closest(owner, targets, selector);
		case FURTHEST:
			return furthest(owner, targets, selector);
		case RANDOM:
			return random(owner, targets, selector);
		case VALUE:
			return value(owner, targets, selector);
		case SORTER:
			return sorter(owner, targets, selector);
		case SELF_AROUND:
			return selfAround(owner, targets, selector);
		case TARGET_AROUND_REPEAT:
			return targetAroundRepeat(owner, targets, selector);
		case STATE_FILTER:
			return stateFilter(owner, targets, selector);
		case STATE:
			return state(owner, targets, selector);
		case UNIT_TYPE:
			return unitType(owner, targets, selector);
		case TARGET_POSITION:
			return targetPosition(owner, selector, positions);
		case EACH_POSITION_REPEAT:
			return eachPositionRepeat(owner, selector, positions);
		case ALIVE:
			return alive(owner, targets, selector);
		case AMOUNT:
			return amount(owner, targets, selector);
		case REPEAT_RANDOM:
			return repeatRandom(owner, targets, selector);
		default:
			logger.error("目标选择类型[{}]未被处理", selector.getType());
			return Collections.emptyList();
		}
	}

	static List<Unit> current(SkillOwner owner, Selector selector) {
		Battle battle = owner.getBattle();
		Fighter attacker = battle.getAttacker();
		Fighter defender = battle.getDefender();

		List<Unit> all = new LinkedList<>();
		all.addAll(attacker.getUnits());
		all.addAll(defender.getUnits());
		return all;
	}

	static List<Unit> target(SkillOwner owner, List<Unit> targets, Selector selector) {
		return targets;
	}

	/** 选择自身 */
	static List<Unit> myself(SkillOwner owner, List<Unit> targets, Selector selector) {
		if (owner instanceof Unit) {
			return Arrays.asList((Unit) owner);
		}
		throw new IllegalArgumentException("目标选择器类型" + selector.getType() + "仅适用于Unit");
	}

	/** 选择我方目标 */
	static List<Unit> our(SkillOwner owner, List<Unit> targets, Selector selector) {
		boolean attacker = isAttacker(owner);
		Iterator<Unit> iter = targets.iterator();
		while (iter.hasNext()) {
			Unit unit = iter.next();
			if (unit.isAttacker() != attacker) {
				iter.remove();
			}
		}

		return targets;
	}

	/** 选择敌方目标 */
	static List<Unit> enemy(SkillOwner owner, List<Unit> targets, Selector selector) {
		boolean attacker = isAttacker(owner);
		Iterator<Unit> iter = targets.iterator();
		while (iter.hasNext()) {
			Unit unit = iter.next();
			if (owner instanceof Unit) {
				if (unit.getId() == ((Unit) owner).getId()) {
					iter.remove();
					continue;
				}
			}
			if (unit.isAttacker() == attacker) {
				iter.remove();
			}
		}

		return targets;
	}

	/** 选择范围内的目标 */
	static List<Unit> range(SkillOwner owner, List<Unit> targets, Selector selector) {
		if (!(owner instanceof Unit)) {
			throw new IllegalArgumentException("目标选择器类型" + selector.getType() + "仅适用于Unit");
		}
		Integer range = selector.getContent(Integer.class);
		Position pos = ((Unit) owner).getPosition();

		Iterator<Unit> iter = targets.iterator();
		while (iter.hasNext()) {
			Unit unit = iter.next();
			int xDistance = pos.getX() - unit.getPosition().getX();
			int yDistance = pos.getY() - unit.getPosition().getY();
			if (Math.abs(xDistance) > range || Math.abs(yDistance) > range) {
				iter.remove();
			}
		}

		return targets;
	}

	/** 选择最近的目标 */
	static List<Unit> closest(SkillOwner owner, List<Unit> targets, Selector selector) {
		if (!(owner instanceof Unit)) {
			throw new IllegalArgumentException("目标选择器类型" + selector.getType() + "仅适用于Unit");
		}
		Integer num = selector.getContent(Integer.class);
		if (num == null) {
			num = 1;
		}
		List<Unit> units = new ArrayList<>();
		Position pos = ((Unit) owner).getPosition();
		Area area = owner.getBattle().getArea();
		boolean attacker = isAttacker(owner);

		int x, y;
		// x修正
		x = pos.getX();
		if (x < 0) {
			x = 0;
		} else if (x > Area.X_MAX) {
			x = Area.X_MAX;
		}

		// 获取X轴检查序列
		Integer[] xs = attacker ? XSA[x] : XSD[x];
		Integer[] ys = YS[pos.getY()];
		int count = 0;
		for (int i = 0; i < xs.length; i++) {
			x = xs[i];
			for (int j = 0; j < ys.length; j++) {
				y = ys[j];
				for (Unit u : area.getUnits(x, y)) {
					if (u == owner) {
						continue;
					}
					// 异常状态判断
					// if (u.hasState(UnitState.UNVISUAL) || u.hasState(UnitState.DEAD)) {
					// continue;
					// }
					// 不在选择范围内的移除
					if (!targets.contains(u)) {
						continue;
					}
					units.add(u);
					if (++count == num) {
						return units;
					}
				}
			}
		}

		// for(int i = 0; i<xs.length;i++){
		//
		// }

		return units;
	}

	/** 选择最远的目标 */
	static List<Unit> furthest(SkillOwner owner, List<Unit> targets, Selector selector) {

		List<Unit> units = new ArrayList<>();
		Position pos = ((Unit) owner).getPosition();
		Area area = owner.getBattle().getArea();
		boolean attacker = isAttacker(owner);

		int x, y;
		// x修正
		x = pos.getX();
		if (x < 0) {
			x = 0;
		} else if (x > Area.X_MAX) {
			x = Area.X_MAX;
		}

		// 获取X轴检查序列
		Integer[] xs = attacker ? XSA[x] : XSD[x];
		Integer[] ys = YS[pos.getY()];
		for (int i = xs.length - 1; i >= 0; --i) {
			x = xs[i];
			for (int j = ys.length - 1; j >= 0; --j) {
				y = ys[j];
				for (Unit u : area.getUnits(x, y)) {
					if (u == owner) {
						continue;
					}
					// 异常状态判断
					if (u.hasState(UnitState.UNVISUAL) || u.hasState(UnitState.DEAD)) {
						continue;
					}
					// 不在选择范围内的移除
					if (!targets.contains(u)) {
						continue;
					}
					units.add(u);
				}
			}
		}
		return units;
	}

	/** 根据最近的目标单位作为方向,选择自身附近区域的目标 */
	static List<Unit> selfAround(SkillOwner owner, List<Unit> targets, Selector selector) {
		if (!(owner instanceof Unit)) {
			throw new IllegalArgumentException("目标选择器类型" + selector.getType() + "仅适用于Unit");
		}
		if (targets.isEmpty()) {
			return targets;
		}
		Integer[] positionLimit = selector.getArrayContent(Integer.class);
		int x = ((Unit) owner).getPosition().getX();
		// 自身确定方向
		boolean direct = isAttacker(owner);
		Unit closestUnit = getClosestUnit((Unit) owner, targets);
		if (closestUnit == null) {
			return new ArrayList<>();
		}
		if (closestUnit.getPosition().getX() > ((Unit) owner).getPosition().getX()) {
			direct = true;
		} else if (closestUnit.getPosition().getX() < ((Unit) owner).getPosition().getX()) {
			direct = false;
		}

		int xMax = x;
		int xMin = x;
		if (direct) {
			xMax = x + positionLimit[1];
			if (xMax > Area.X_MAX) {
				xMax = Area.X_MAX;
			}
			xMin = x + positionLimit[0];
			if (xMin < 0) {
				xMin = 0;
			}
		} else {
			xMin = x - positionLimit[1];
			if (xMin < 0) {
				xMin = 0;
			}
			xMax = x - positionLimit[0];
			if (xMax > Area.X_MAX) {
				xMax = Area.X_MAX;
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("技能选取目标坐标范围[{}]-[{}]", xMin, xMax);
		}
		Iterator<Unit> iter = targets.iterator();
		while (iter.hasNext()) {
			Unit unit = iter.next();
			Position position = unit.getPosition();
			if (position.getX() >= xMin && position.getX() <= xMax) {
				continue;
			}
			iter.remove();
		}
		return targets;
	}

	/** 选择目标区域的附近单位 */
	static List<Unit> targetAroundRepeat(SkillOwner owner, List<Unit> targets, Selector selector) {
		List<Unit> result = new ArrayList<>();
		if (targets.isEmpty()) {
			return result;
		}
		Area area = targets.get(0).getArea();
		int range = selector.getContent(Integer.class);
		for (int i = 0; i < targets.size(); i++) {
			Unit target = targets.get(i);
			Position targetPosition = target.getPosition();
			int xMin = targetPosition.getX() - range;
			int xMax = targetPosition.getX() + range;
			if (xMax > Area.X_MAX) {
				xMax = Area.X_MAX;
			}
			if (xMin < 0) {
				xMin = 0;
			}
			for (int x = xMin; x <= xMax; x++) {
				for (int y = 0; y < Area.Y_SIZE; y++) {
					List<Unit> areaUnit = area.getUnits(x, y);
					result.addAll(areaUnit);
				}
			}
		}

		return result;
	}

	/** 过滤掉具有指定状态的单位 */
	static List<Unit> stateFilter(SkillOwner owner, List<Unit> targets, Selector selector) {
		Integer state = selector.getContent(Integer.class);
		Iterator<Unit> iter = targets.iterator();
		while (iter.hasNext()) {
			Unit unit = iter.next();
			if (unit.hasState(state)) {
				iter.remove();
			}
		}
		return targets;
	}

	/** 获取具有指定状态的单位 */
	static List<Unit> state(SkillOwner owner, List<Unit> targets, Selector selector) {
		Integer state = selector.getContent(Integer.class);
		Iterator<Unit> iter = targets.iterator();
		while (iter.hasNext()) {
			Unit unit = iter.next();
			if (!unit.hasState(state)) {
				iter.remove();
			}
		}
		return targets;
	}

	/** 选择活着的单位 */
	static List<Unit> alive(SkillOwner owner, List<Unit> targets, Selector selector) {
		Iterator<Unit> iter = targets.iterator();
		while (iter.hasNext()) {
			Unit unit = iter.next();
			if (unit.isDead()) {
				iter.remove();
			}
		}
		return targets;
	}

	/** 获取指定位置上的战斗单位 */
	static List<Unit> targetPosition(SkillOwner owner, Selector selector, Position... positions) {
		if (!(owner instanceof Unit)) {
			throw new IllegalArgumentException("目标选择器类型" + selector.getType() + "仅适用于Unit");
		}
		Area area = ((Unit) owner).getArea();
		List<Unit> all = new LinkedList<>();
		for (Position position : positions) {
			if (position.getX() < 0 || position.getX() > Area.X_MAX) {
				// logger.error("目标选择器类型" + selector.getType() + "不适用与未入场单位");
				continue;
			}
			List<Unit> units = area.getUnits(position.getX(), position.getY());
			all.addAll(units);
		}
		return all;
	}

	/** 目标位置重复选择指定目标 */
	static List<Unit> eachPositionRepeat(SkillOwner owner, Selector selector, Position... positions) {
		if (!(owner instanceof Unit)) {
			throw new IllegalArgumentException("目标选择器类型" + selector.getType() + "仅适用于Unit");
		}
		Area area = ((Unit) owner).getArea();
		Integer num = selector.getContent(Integer.class);
		List<Unit> result = new ArrayList<>(num);
		if (positions == null) {
			return result;
		}
		// 构造每个位置的随机命中结果
		Map<Integer, Integer> randomResult = new HashMap<>();
		Random random = owner.getBattle().getRandom();
		for (int i = 0; i < num; i++) {
			int randomNum = random.nextInt(positions.length);
			Integer value = randomResult.get(randomNum);
			if (value == null) {
				value = 0;
			}
			value++;
			randomResult.put(randomNum, value);
		}
		// 获得每个位置命中的战斗单元
		for (int i = 0; i < positions.length; i++) {
			if (positions[i].getX() < 0 || positions[i].getX() > Area.X_MAX) {
				// logger.error("目标选择器类型" + selector.getType() + "不适用与未入场单位");
				continue;
			}
			List<Unit> units = area.getUnits(positions[i].getX(), positions[i].getY());
			if (units.isEmpty()) {
				continue;
			}
			Integer repeatNum = randomResult.get(i);
			if (repeatNum == null) {
				continue;
			}
			int index = random.nextInt(units.size());
			for (int z = 0; z < repeatNum; z++) {
				result.add(units.get(index));
			}
		}
		return result;
	}

	/** 获取指定战斗单元的位置 */
	static List<Position> getPositions(SkillOwner owner, List<Unit> targets, Selector[] selectors) {
		List<Position> positions = new ArrayList<Position>();
		for (Unit target : targets) {
			positions.add(target.getPosition());
		}
		return positions;
	}

	/** 选择随机N个目标 */
	static List<Unit> random(SkillOwner owner, List<Unit> targets, Selector selector) {
		Integer amount = selector.getContent(Integer.class, 1);
		int length = Math.min(amount, targets.size());
		shuffle(targets, owner.getBattle().getRandom());
		return targets.subList(0, length);
	}

	/** 选择属性符合要求的目标 */
	static List<Unit> value(SkillOwner owner, List<Unit> targets, Selector selector) {
		String expression = selector.getContent();
		Iterator<Unit> iter = targets.iterator();
		while (iter.hasNext()) {
			Unit u = iter.next();
			boolean flag = ExpressionHelper.invoke(expression, Boolean.class, u);
			if (!flag) {
				iter.remove();
			}
		}
		return targets;
	}

	/** 选择属性排序后的目标 */
	static List<Unit> sorter(SkillOwner owner, List<Unit> targets, Selector selector) {
		final LinkedHashMap<UnitValue, Boolean> uvs = selector.getMapContent(UnitValue.class, Boolean.class);
		Collections.sort(targets, new Comparator<Unit>() {
			@Override
			public int compare(Unit u1, Unit u2) {
				CompareToBuilder builder = new CompareToBuilder();
				for (Entry<UnitValue, Boolean> entry : uvs.entrySet()) {
					if (entry.getValue()) {
						builder.append(u1.getValue(entry.getKey()), u2.getValue(entry.getKey()));
					} else {
						builder.append(u2.getValue(entry.getKey()), u1.getValue(entry.getKey()));
					}
				}
				return builder.toComparison();
			}
		});
		return targets;
	}

	/** 选择指定数量的目标 */
	static List<Unit> amount(SkillOwner owner, List<Unit> targets, Selector selector) {
		Integer amount = selector.getContent(Integer.class);
		int length = Math.min(targets.size(), amount);
		return targets.subList(0, length);
	}

	/** 选择指定的前中后排 */
	static List<Unit> unitType(SkillOwner owner, List<Unit> targets, Selector selector) {
		UnitType[] types = selector.getContent(UnitType[].class);
		List<Unit> ret = new ArrayList<>();
		for (UnitType type : types) {
			for (Unit u : targets) {
				if (u.getModel().getType() == type) {
					ret.add(u);
				}
			}
			if (!ret.isEmpty()) {
				return ret;
			}
		}
		return ret;
	}

	/** 随机可重复选择目标 */
	static List<Unit> repeatRandom(SkillOwner owner, List<Unit> targets, Selector selector) {
		if (targets.isEmpty()) {
			return targets;
		}

		int amount = selector.getContent(Integer.class);
		List<Unit> units = new ArrayList<>(amount);

		for (int i = 0; i < amount; ++i) {
			int index = owner.getBattle().getRandom().nextInt(targets.size());
			Unit unit = targets.get(index);
			units.add(unit);
		}

		return units;
	}

	private static boolean isAttacker(SkillOwner owner) {
		boolean attacker = owner.isAttacker();
		if (owner instanceof Unit) {
			if (((Unit) owner).hasState(UnitState.CHAOS)) {
				attacker = !attacker;
			}
		}
		return attacker;
	}

	private static Unit getClosestUnit(Unit owner, List<Unit> targets) {
		Position pos = owner.getPosition();
		Area area = owner.getBattle().getArea();
		boolean attacker = isAttacker(owner);

		int x, y;
		// x修正
		x = pos.getX();
		if (x < 0) {
			x = 0;
		} else if (x > Area.X_MAX) {
			x = Area.X_MAX;
		}

		// 获取X轴检查序列
		Integer[] xs = attacker ? XSA[x] : XSD[x];
		Integer[] ys = YS[pos.getY()];
		for (int i = 0; i < xs.length; i++) {
			x = xs[i];
			for (int j = 0; j < ys.length; j++) {
				y = ys[j];
				for (Unit u : area.getUnits(x, y)) {
					if (u == owner) {
						continue;
					}
					// 不在选择范围内的移除
					if (!targets.contains(u)) {
						continue;
					}
					return u;
				}
			}
		}
		return null;
	}

	/** 洗牌 */
	private static void shuffle(List<?> list, Random rnd) {
		int size = list.size();
		for (int i = size; i > 1; i--)
			swap(list, i - 1, rnd.nextInt(size));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void swap(List<?> list, int i, int j) {
		final List l = list;
		l.set(i, l.set(j, l.get(i)));
	}

}
