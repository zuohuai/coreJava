package com.edu.game.jct.fight.model;

import java.util.HashMap;
import java.util.Map;

import com.edu.game.jct.fight.service.core.Unit;


/**
 * 战斗单元信息
 * @author administrator
 */
public class UnitInfo {
	/** 标识 */
	private String id;
	/** 等级 */
	private int level;
	/** 模型信息 */
	private ModelInfo model;
	/** 位置 */
	private String position;

	/** 生命 */
	private int hp;
	/** 生命上限 */
	private int hpMax;
	/** 怒气 */
	private int mp;
	/** 怒气上限 */
	private int mpMax;

	/** 技能与CD */
	private Map<String, Integer> skills;
	/** 技能列表 */
	private String[] skillList;

	// Getter and Setter ...

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public ModelInfo getModel() {
		return model;
	}

	public void setModel(ModelInfo model) {
		this.model = model;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getHpMax() {
		return hpMax;
	}

	public void setHpMax(int hpMax) {
		this.hpMax = hpMax;
	}

	public int getMp() {
		return mp;
	}

	public void setMp(int mp) {
		this.mp = mp;
	}

	public int getMpMax() {
		return mpMax;
	}

	public void setMpMax(int mpMax) {
		this.mpMax = mpMax;
	}

	public Map<String, Integer> getSkills() {
		return skills;
	}

	public void setSkills(Map<String, Integer> skills) {
		this.skills = skills;
	}

	public String[] getSkillList() {
		return skillList;
	}

	public void setSkillList(String[] skillList) {
		this.skillList = skillList;
	}

	/** 构造方法 */
	public static UnitInfo valueOf(Unit unit) {
		UnitInfo result = new UnitInfo();
		result.id = unit.getId();
		result.position = unit.getPosition().toString();
		result.hp = unit.getValue(UnitValue.HP);
		result.hpMax = unit.getValue(UnitValue.HP_MAX);
		result.mp = unit.getValue(UnitValue.MP);
		result.mpMax = unit.getValue(UnitValue.MP_MAX);
		return result;
	}

}
