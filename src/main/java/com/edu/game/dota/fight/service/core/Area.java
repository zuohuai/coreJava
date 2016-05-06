package com.eyu.snm.module.fight.service.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.eyu.snm.module.fight.model.UnitType;
import com.eyu.snm.module.fight.model.report.Report;

/**
 * 区域
 * @author Frank
 */
@SuppressWarnings("unchecked")
public class Area {

	/** 场景宽度最大值 */
	public static final int X_MAX = 7;
	/** 场景宽度尺寸 */
	public static final int X_SIZE = 8;
	/** 场景高度最大值 */
	public static final int Y_MAX = 4;
	/** 场景高度尺寸 */
	public static final int Y_SIZE = 5;

	/**
	 * 构造方法
	 * @param owner 区域所有者
	 * @param attacker 攻击方
	 * @param defender 防守方
	 * @return
	 */
	public static Area valueOf(Battle owner, Fighter attacker, Fighter defender) {
		Area ret = new Area();
		ret.owner = owner;
		ret.initialize(attacker, defender);
		return ret;
	}

	/** 区域归属者 */
	private Battle owner;
	/** 区域内的战斗单位 */
	private ArrayList<Unit>[][] units = new ArrayList[X_SIZE][Y_SIZE];
	/** 区域内的场景元素 */
	private ArrayList<Element>[][] floor = new ArrayList[X_SIZE][Y_SIZE];

	public Area() {
		for (int x = 0; x < X_SIZE; x++) {
			for (int y = 0; y < Y_SIZE; y++) {
				units[x][y] = new ArrayList<Unit>(1);
				floor[x][y] = new ArrayList<Element>(1);
			}
		}
	}

	/**
	 * 初始化战场区域并确定攻防双方站位
	 * @param attacker 攻击方
	 * @param defender 防守方
	 */
	public void initialize(Fighter attacker, Fighter defender) {
		enter(attacker, true);
		enter(defender, false);
	}

	/**
	 * 获取指定位置上的全部战斗单位
	 * @param x
	 * @param y
	 * @return
	 */
	public List<Unit> getUnits(int x, int y) {
		return units[x][y];
	}

	/**
	 * 将指定战斗单位移动到指定位置
	 * @param unit
	 * @param x
	 * @param y
	 * @param updateTiming
	 */
	public void move(Unit unit, int x, int y, boolean updateTiming) {
		Position prev = unit.getPosition();
		if (prev.getX() >= 0 && prev.getX() <= X_MAX) {
			units[prev.getX()][prev.getY()].remove(unit);
		}
		// 修正范围值
		if (x < 0) {
			x = 0;
		} else if (x > X_MAX) {
			x = X_MAX;
		}
		units[x][y].add(unit);
		unit.move(x, y, updateTiming);
	}

	/**
	 * 战斗单位进入战场区域
	 * @param map
	 * @param attacker true:攻击方,false:防守方
	 */
	private void enter(Fighter fighter, boolean attacker) {
		int x = attacker ? -1 : X_SIZE;
		Map<UnitType, TreeSet<Unit>> map = sortUnits(fighter.getUnits());
		int center = Y_MAX / 2;
		long enterTime = owner.getDuration() + Constant.FIRST_ENTER;
		for (UnitType type : UnitType.values()) {

			// 让战斗单位从场景外走入的处理 根据前端变现要求 现改为第一个单位入场2秒其他单位入场间隔500ms
			TreeSet<Unit> set = map.get(type);
			int index = 0;
			for (Unit unit : set) {
				int y = center;
				int distance = (index + 1) / 2;
				if (index % 2 == 0) {
					y += distance;
				} else {
					y -= distance;
				}
				unit.enter(x, y, enterTime);
				enterTime += Constant.INTERVAL_ENTER;
				index++;
			}

		}
	}

	/**
	 * 对战斗单位进行分类排序
	 * @param units 某方的全部战斗单位
	 * @return
	 */
	private Map<UnitType, TreeSet<Unit>> sortUnits(Collection<Unit> units) {
		Map<UnitType, TreeSet<Unit>> ret = new LinkedHashMap<UnitType, TreeSet<Unit>>(3);
		ret.put(UnitType.FRONT, new TreeSet<Unit>());
		ret.put(UnitType.MIDDLE, new TreeSet<Unit>());
		ret.put(UnitType.BACK, new TreeSet<Unit>());
		for (Unit u : units) {
			UnitType type = u.getModel().getType();
			TreeSet<Unit> set = ret.get(type);
			set.add(u);
		}
		return ret;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int y = 0; y < Y_SIZE; y++) {
			for (int x = 0; x < X_SIZE; x++) {
				sb.append("[");
				for (Unit u : units[x][y]) {
					sb.append(u.getId()).append(" ");
				}
				sb.append("]");
			}
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}

	/**
	 * 重新定位(允许从备战区域直接跳转到战斗区域)
	 * @param unit
	 * @param x
	 * @param y
	 * @return 新的位置
	 */
	public Position reloacte(Unit unit, int x, int y) {
		if (x < 0 || x > X_MAX || y < 0 || y > Y_MAX) {
			throw new IllegalArgumentException("坐标[" + x + "," + y + "]不是合法的战斗区域");
		}

		Position prev = unit.getPosition();
		Position current = Position.valueOf(x, y);
		if (prev.equals(current)) {
			return prev;
		}

		// 允许从备战区域直接跳转到战斗区域
		if (prev.isValid()) {
			List<Unit> prevs = getUnits(prev.getX(), prev.getY());
			prevs.remove(unit);
		}

		List<Unit> currents = getUnits(x, y);
		currents.add(unit);

		return current;
	}

	/**
	 * 位置修正用于客户端战报
	 * @param position
	 * @param justX x轴调整参数
	 * @return
	 */
	public static Position justBoundary(Position position, int justX) {
		int x = position.getX();
		int y = position.getY();
		int after = x + justX;
		if (after < -Report.OFFSET) {
			after = -Report.OFFSET;
		} else if (after > X_MAX) {
			after = X_MAX;
		}
		return Position.valueOf(after, y);
	}

	public Battle getOwner() {
		return owner;
	}

	public void addElement(Element... elements) {
		// 预留...
	}

	public void removeElement(Element element) {
		// 预留...
	}

}
