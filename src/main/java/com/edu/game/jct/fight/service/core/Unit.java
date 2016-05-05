package com.edu.game.jct.fight.service.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.edu.game.Player;
import com.edu.game.jct.fight.exception.FightException;
import com.edu.game.jct.fight.exception.FightExceptionCode;
import com.edu.game.jct.fight.model.Position;
import com.edu.game.jct.fight.model.UnitDegree;
import com.edu.game.jct.fight.model.UnitRate;
import com.edu.game.jct.fight.model.UnitState;
import com.edu.game.jct.fight.model.UnitValue;
import com.edu.game.jct.fight.model.report.CdInfo;
import com.edu.game.jct.fight.service.effect.buff.BuffState;
import com.edu.game.jct.fight.service.effect.init.InitEffectState;
import com.edu.game.jct.fight.service.effect.passive.PassiveState;
import com.edu.game.jct.fight.service.effect.skill.SkillFactory;
import com.edu.game.jct.fight.service.effect.skill.SkillState;
import com.edu.utils.RandomUtils;

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

	/** 选中的技能 */
	private String choseSkill;

	/** 选中技能 */
	public void choseSkill(String skill) {
		SkillState state = skillStates.get(skill);
		if (state == null) {
			throw new FightException(FightExceptionCode.SKILL_NOT_FOUND);
		}
		if (!state.isVaild(this, false)) {
			throw new FightException(FightExceptionCode.SKILL_CANNOT_CHOSE);
		}
		choseSkill = skill;
	}

	/**
	 * 获取战斗初始化效果，已经按照优先级排好序
	 * @return
	 */
	public List<InitEffectState> getInitEffectState() {
		List<InitEffectState> result = new ArrayList<InitEffectState>(initStates.size());
		for (InitEffectState state : initStates.values()) {
			result.add(state);
		}
		if (result.size() > 1) {
			Collections.sort(result, InitEffectState.COMPARATOR_PRIORITY);
		}
		return result;

	}

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

	/** 检查是否死亡 */
	public boolean isDead() {
		return hasState(UnitState.DEAD);
	}

	/** 设置死亡状态 */
	public void dead() {
		// 死亡直接设置死亡状态，没有任何其他效果
		this.state = UnitState.DEAD;

		// 清空所有的效果 TODO
	}

	/**
	 * 获取指定类型的BUFF
	 * @param tag 类型标识(由策划自行管理)
	 * @return
	 */
	public BuffState getBuffState(String tag) {
		for (BuffState state : buffStates.values()) {
			if (state.getTag().equals(tag)) {
				return state;
			}
		}
		return null;
	}

	/**
	 * 检查是否有BUFF存在
	 * @return true:有,false:没有
	 */
	public boolean hasBuff() {
		return !buffStates.isEmpty();
	}

	/**
	 * 获取指定阶段的被动效果(只返回有效的被动效果)
	 * @param phase 阶段
	 * @return 返回的被动效果集合已经按优先级别排好序，并不会返回null
	 */
	public List<PassiveState> getPassiveState(Phase phase) {
		List<PassiveState> result = new ArrayList<PassiveState>(passiveStates.size());
		for (PassiveState state : passiveStates.values()) {
			if (state.getPhases().contains(phase)) {
				result.add(state);
			}
		}
		if (result.size() > 1) {
			Collections.sort(result, PassiveState.COMPARATOR_PRIORITY);
		}
		return result;
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

	/** 获取全部BUFF状态 */
	@SuppressWarnings("unchecked")
	public Collection<BuffState> getAllBuffStates() {
		if (buffStates.isEmpty()) {
			return Collections.EMPTY_LIST;
		}

		List<BuffState> result = new ArrayList<BuffState>(buffStates.values());
		Collections.sort(result, BuffState.COMPARATOR_PRIORITY);

		return result;
	}

	/**
	 * 获取当前使用的技能
	 * @param friend 友方
	 * @param enemy 敌方
	 * @return
	 */
	public SkillState getCurrentSkill(Fighter friend, Fighter enemy) {
		SkillState result = null;
		if (choseSkill != null) {
			// 手动技能选择处理
			result = skillStates.get(choseSkill);
			choseSkill = null;
		} else {
			// 自动技能选择处理(技能选择AI)
			SkillFactory factory = SkillFactory.getInstance();
			List<SkillState> states = new ArrayList<SkillState>(skillStates.values());
			Collections.sort(states, SkillState.COMPARATOR_PRIORITY);
			Iterator<SkillState> it = states.iterator();
			int priority = Integer.MIN_VALUE;
			while (it.hasNext()) {
				SkillState state = it.next();
				if (state.getPriority() < priority) {
					it.remove();
					continue;
				}
				if (!state.isVaild(this, true)) {
					// 不满足CD与MP要求的技能直接跳过
					it.remove();
					continue;
				}
				if (!factory.isAllow(state.getId(), this, friend, enemy)) {
					// 再检查技能是否符合代码级别的施放要求
					it.remove();
					continue;
				}
				priority = state.getPriority();
			}
			result = states.get(RandomUtils.betweenInt(0, states.size() - 1, true));
		}
		return result;
	}

	/** 刷新全部技能CD */
	public void refreshSkillCd() {
		for (SkillState state : skillStates.values()) {
			state.decreaseCd();
		}
	}

	/** 获取技能的CD信息 */
	public CdInfo getCdInfo() {
		return CdInfo.valueOf(id, skillStates.values());
	}

	/**
	 * 检查是否有指定阶段的被动效果
	 * @param phase
	 * @return
	 */
	public boolean hasPassive(Phase phase) {
		for (PassiveState state : passiveStates.values()) {
			if (state.getPhases().contains(phase)) {
				return true;
			}
		}
		return false;
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

	/** 获取已经选择的技能标识 */
	public String getChoseSkill() {
		return choseSkill;
	}
}
