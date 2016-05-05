package com.edu.game.jct.fight.service.effect.skill;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.edu.game.jct.fight.model.UnitValue;
import com.edu.game.jct.fight.service.core.Unit;
import com.edu.game.resource.JsonObject;
import com.edu.utils.RandomUtils;

/**
 * 技能状态
 * @author Frank
 */
public class SkillState implements Cloneable, JsonObject {

	/** 优先级别排序器 */
	public static final Comparator<SkillState> COMPARATOR_PRIORITY = new Comparator<SkillState>() {
		@Override
		public int compare(SkillState o1, SkillState o2) {
			if (o1.priority == o2.priority) {
				return RandomUtils.isHit(0.5) ? 1 : -1;
			}
			return o2.priority - o1.priority;
		}
	};

	/** 技能标识 */
	private String id;
	/** 冷却CD(再回合结束时统一扣减) */
	private int cd;
	/** 技能施放的状态要求 */
	private int state;
	/** 技能施放的怒气要求 */
	private int anger;
	/** 施放后可获得的怒气 */
	private int mp;
	/** 技能优先级 */
	private int priority;
	/** 技能施放后的冷却回合数 */
	private int round;
	/** 技能施放后免CD的比率 */
	private double rate;
	/** 技能效果状态 */
	private List<EffectState> effectStates;
	
	/**
	 * 检查战斗单位能否选择该技能
	 * @param unit
	 * @return
	 */
	public boolean isVaild(Unit unit, boolean chkState) {
		if (chkState) {
			if (cd <= 0 && unit.hasState(state) && unit.getValue(UnitValue.MP) >= anger) {
				return true;
			}
		} else {
			if (cd <= 0 && unit.getValue(UnitValue.MP) >= anger) {
				return true;
			}
		}
		return false;
	}

	/** 扣减一个回合的冷却CD */
	public void decreaseCd() {
		if (cd > 0) {
			cd--;
		}
	}

	/** 添加新的冷却CD */
	public void addCd() {
		cd = round;
	}
	
	/** 清除CD */
	public void clearCd(int value) {
		cd -= value;
		if(cd < 0) {
			cd = 0;
		}
	}

	/**
	 * 添加技能效果状态
	 * @param state
	 */
	public void addEffectState(EffectState state) {
		effectStates.add(state);
	}

	public SkillState clone(String id) {
		SkillState state = clone();
		state.id = id;
		state.effectStates = new ArrayList<EffectState>(1);
		if (effectStates != null) {
			for (EffectState effectState : effectStates) {
				state.effectStates.add(effectState.clone());
			}
		}
		return state;
	}

	@Override
	protected SkillState clone() {
		try {
			return (SkillState) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException("对象无法被克隆", e);
		}
	}
	
	// Getter and Setter ...

	public String getId() {
		return id;
	}

	public int getCd() {
		return cd;
	}

	public int getMp() {
		return mp;
	}

	public int getState() {
		return state;
	}

	protected void setState(int state) {
		this.state = state;
	}
	
	public int getAnger() {
		return anger;
	}

	protected void setAnger(int anger) {
		this.anger = anger;
	}

	public int getPriority() {
		return priority;
	}

	public List<EffectState> getEffectStates() {
		return effectStates;
	}

	protected void setId(String id) {
		this.id = id;
	}

	protected void setCd(int cd) {
		this.cd = cd;
	}

	protected void setMp(int mp) {
		this.mp = mp;
	}

	protected void setPriority(int priority) {
		this.priority = priority;
	}

	public int getRound() {
		return round;
	}

	protected void setRound(int round) {
		this.round = round;
	}

	public double getRate() {
		return rate;
	}

	protected void setRate(double rate) {
		this.rate = rate;
	}

	@Override
	public String toString() {
		return "[cd=" + cd + ", mp=" + mp + ", priority=" + priority + "]";
	}

}
