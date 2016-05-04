package com.edu.game.jct.fight.model.report;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.edu.game.jct.fight.model.UnitDegree;
import com.edu.game.jct.fight.model.UnitRate;
import com.edu.game.jct.fight.model.UnitValue;

/**
 * 调试用的战斗信息
 * @author Frank
 */
public class DebugInfo {

	/** 标识 */
	private String id;
	/** 战斗力 */
	private int score;
	/** 数值属性 */
	private Map<UnitValue, Integer> values;
	/** 比率属性 */
	private Map<UnitRate, Double> rates;
	/** 比率属性 */
	private Map<UnitDegree, Double> degrees;
	/** 状态值 */
	private int state;

	/** 技能与CD */
	private Map<String, Integer> skills;
	/** 被动效果状态 */
	private Set<String> passives = new HashSet<String>();
	/** BUFF效果状态 */
	private Set<String> buffs = new HashSet<String>();

	// Getter and Setter ...

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<UnitValue, Integer> getValues() {
		return values;
	}

	public void setValues(Map<UnitValue, Integer> values) {
		this.values = values;
	}

	public Map<UnitRate, Double> getRates() {
		return rates;
	}

	public void setRates(Map<UnitRate, Double> rates) {
		this.rates = rates;
	}

	public Map<UnitDegree, Double> getDegrees() {
		return degrees;
	}

	public void setDegrees(Map<UnitDegree, Double> degrees) {
		this.degrees = degrees;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Map<String, Integer> getSkills() {
		return skills;
	}

	public void setSkills(Map<String, Integer> skills) {
		this.skills = skills;
	}

	public Set<String> getPassives() {
		return passives;
	}

	public void setPassives(Set<String> passives) {
		this.passives = passives;
	}

	public Set<String> getBuffs() {
		return buffs;
	}

	public void setBuffs(Set<String> buffs) {
		this.buffs = buffs;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public static DebugInfo valueOf(String id, int score, Map<UnitValue, Integer> values, Map<UnitRate, Double> rates,
			Map<UnitDegree, Double> degrees, int state, Map<String, Integer> skills, Set<String> passives,
			Set<String> buffs) {
		DebugInfo result = new DebugInfo();
		result.id = id;
		result.score = score;
		result.values = values;
		result.rates = rates;
		result.degrees = degrees;
		result.state = state;
		result.skills = skills;
		result.passives = passives;
		result.buffs = buffs;
		return result;
	}

}
