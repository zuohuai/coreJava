package com.edu.game.jct.fight.service.core;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.edu.game.Player;
import com.edu.game.jct.fight.model.Position;
import com.edu.game.jct.fight.model.UnitDegree;
import com.edu.game.jct.fight.model.UnitRate;
import com.edu.game.jct.fight.model.UnitState;
import com.edu.game.jct.fight.model.UnitValue;
import com.edu.game.jct.fight.service.effect.buff.BuffState;
import com.edu.game.jct.fight.service.effect.init.InitEffectState;

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
	/** 位置 */
	private Position position;
	/** 状态值 */
	private int state = UnitState.NORMAL;
	/** 玩家对象 */
	private Player owner;
	/** 玩家身份标识 */
	private Long playerId;
	/** 数值属性 */
	private Map<UnitValue, Integer> values = new HashMap<>(UnitValue.values().length);
	/** 比率属性(累加关系) */
	private Map<UnitRate, Double> rates = new HashMap<>(UnitRate.values().length);
	/** 比率属性() */
	private Map<UnitDegree, Double> degrees = new HashMap<>(UnitDegree.values().length);

	
	/** 技能状态 */
	private HashMap<String, SkillState> skillStates = new HashMap<String, SkillState>();
	/** 被动效果状态 */
	private HashMap<String, PassiveState> passiveStates = new HashMap<String, PassiveState>();
	/** BUFF效果状态 */
	private HashMap<String, BuffState> buffStates = new HashMap<String, BuffState>();
	/** 初始化效果状态 */
	private HashMap<String, InitEffectState> initStates = new HashMap<String, InitEffectState>();
	/**
	 * 增加/减少属性值(累加关系)
	 * @param type 约定的键名
	 * @param value 增量
	 * @return 最新值
	 */
	public int increaseValue(UnitValue type, int value) {
		int current = getValue(type);
		switch (type) {
		case HP:
			current += value;
			if (current <= 0) {
				current = 0;
				dead();
			} else if (current > getValue(UnitValue.HP_MAX)) {
				current = getValue(UnitValue.HP_MAX);
			}
			setValue(type, current);
			break;
		case MP:
			current += value;
			if (current <= 0) {
				current = 0;
			} else if (current > getValue(UnitValue.MP_MAX)) {
				current = getValue(UnitValue.MP_MAX);
			}
			setValue(type, current);
			break;
		default:
			current += value;
			if (current < 0) {
				current = 0;
			}
			setValue(type, current);
			break;
		}
		return current;
	}

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
	 * 设置指定的数值属性
	 * @param type 属性类型
	 * @param value 值
	 * @return
	 */
	public void setValue(UnitValue type, int value) {
		values.put(type, value);
	}

	/**
	 * 增加/减少比率值(累加关系)
	 * @param name 约定的键名
	 * @param value 增量
	 * @return 最新值
	 */
	public double increaseRate(UnitRate rate, double value) {
		double current = getRate(rate);
		current += value;
		setRate(rate, current);
		return current;
	}
	
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
	 * 设置指定比率(累加关系)
	 * @param rate 比率
	 * @param value 值
	 * @return
	 */
	public void setRate(UnitRate rate, double value) {
		rates.put(rate, value);
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

	/**
	 * 增加/减少比率值(乘除关系)
	 * @param name 约定的键名
	 * @param value 增量
	 * @return 最新值
	 */
	public double increaseDegree(UnitDegree degree, double value) {
		double current = getDegree(degree);
		if (value > 0) {
			current *= value;
		} else if (value < 0) {
			current /= -value;
		} else {
			throw new IllegalArgumentException(degree.name() + "比率修改值不能为0");
		}
		setDegree(degree, current);
		return current;
	}

	/**
	 * 获取指定比率(乘除关系)
	 * @param type 类型
	 * @return
	 */
	public double getDegree(String type) {
		return getDegree(UnitDegree.valueOf(type));
	}

	/**
	 * 设置指定比率(乘除关系)
	 * @param degree 比率
	 * @param value 值
	 * @return
	 */
	public void setDegree(UnitDegree degree, double value) {
		degrees.put(degree, value);
	}

	// 状态相关的逻辑方法 ...

	/** 添加状态 */
	public void addState(int status) {
		state = state | status;
	}

	/** 移除状态 */
	public void removeState(int status) {
		if (hasState(status)) {
			state = state ^ status;
		}
	}

	/** 设置存活状态 */
	public void live() {
		removeState(UnitState.DEAD);
	}
	/** 检查是否有某种状态 */
	public boolean hasState(int status) {
		return (state & status) == status ? true : false;
	}

	
	/** 设置死亡状态 */
	public void dead() {
		// 死亡直接设置死亡状态，没有任何其他效果
		this.state = UnitState.DEAD;

		// 清空所有的效果 TODO
	}

	/**
	 * 修改怒气值
	 * @param value 值
	 */
	public void changeMp(int value) {
		int limit = getValue(UnitValue.MP_INIT);
		if (value > limit) {
			setValue(UnitValue.MP, value);
		} else {
			setValue(UnitValue.MP, limit);
		}
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

	/** 获取技能状态 */
	public Map<String, SkillState> getSkillStates() {
		return skillStates;
	}

	/** 获取初始化效果状态 */
	public Map<String, InitEffectState> getInitStates() {
		return initStates;
	}

	public HashMap<String, PassiveState> getPassiveStates() {
		return passiveStates;
	}

	public HashMap<String, BuffState> getBuffStates() {
		return buffStates;
	}
}
