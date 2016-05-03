package com.edu.game.jct.fight.service.core;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.edu.game.Player;
import com.edu.game.jct.fight.model.Position;
import com.edu.game.jct.fight.model.UnitDegree;
import com.edu.game.jct.fight.model.UnitRate;
import com.edu.game.jct.fight.model.UnitValue;

/**
 * 战斗单元 该对象用来表示战斗中的计算个体
 * @author Administrator
 */
public class Unit implements Cloneable {
	/** 攻击方标识前缀 */
	public static final String ATTACER_PREFIX = "A:";
	/** 防御方标识前缀 */
	public static final String DEFENDER_PREFIX = "D:";

	/** 速度比较器 */
	public static final Comparator<Unit> COMPARATOR_SPEED = new Comparator<Unit>() {
		@Override
		public int compare(Unit o1, Unit o2) {
			int result = o1.getValue(UnitValue.SPEED) - o2.getValue(UnitValue.SPEED);
			if (result != 0) {
				return -result;
			}
			return o1.id.compareTo(o2.id);
		}
	};

	/** 标识 */
	private String id;
	/**位置*/
	private Position position;
	/**玩家对象*/
	private Player owner;
	/**玩家身份标识*/
	private Long playerId;
	/** 数值属性 */
	private Map<UnitValue, Integer> values = new HashMap<>(UnitValue.values().length);
	/** 比率属性(累加关系) */
	private Map<UnitRate, Double> rates = new HashMap<>(UnitRate.values().length);
	/** 比率属性() */
	private Map<UnitDegree, Double> degrees = new HashMap<>(UnitDegree.values().length);

	/**
	 * 获取基础属性
	 * @param type
	 * @return
	 */
	public int getValue(UnitValue type) {
		Integer result = values.get(type);
		if (result == null) {
			return 0;
		}
		return result;
	};

	/**
	 * 获取比率属性(累加关系)
	 * @param type
	 * @return
	 */
	public double getRate(UnitRate type) {
		Double result = rates.get(type);
		if (result == null) {
			return 0;
		}
		return result;
	}

	/**
	 * 获取比率属性(乘除关系)
	 * @param type
	 * @return
	 */
	public double getDegree(UnitDegree type) {
		Double result = degrees.get(type);
		if (result == null) {
			return 1.0;
		}
		return result;
	}

	public String getId() {
		return id;
	}

	public Position getPosition() {
		return position;
	}

	public Player getOwner() {
		return owner;
	}

	public Long getPlayerId() {
		return playerId;
	}
	
}
