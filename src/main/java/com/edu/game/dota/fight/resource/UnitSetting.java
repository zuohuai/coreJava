package com.edu.game.dota.fight.resource;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.edu.game.dota.fight.model.UnitValue;
import com.edu.game.dota.fight.service.core.Skill;
import com.edu.game.dota.fight.service.core.SkillFactory;
import com.edu.game.dota.fight.service.core.Stage;
import com.edu.game.dota.fight.service.core.Unit;
import com.edu.game.resource.Validate;
import com.edu.game.resource.anno.Id;
import com.edu.game.resource.anno.Resource;

/**
 * 战斗单位配置表
 * @author Frank
 */
@Resource("dota")
public class UnitSetting implements Validate {

	private static final Logger logger = LoggerFactory.getLogger(UnitSetting.class);

	/** 标识 */
	@Id
	private String id;
	/** 战报编码 */
	private short code;
	/** 模型配置标识 */
	private short model;

	// 属性相关
	/** 等级 */
	private int level;
	/** 属性数值 */
	private Map<UnitValue, Integer> values;
	/** 修正系数 */
	private Map<UnitValue, Integer> radios;
	/** 战斗力系数 */
	private int fightRadio;

	// 技能相关
	/** 超必杀 */
	private String unique;
	/** 普通攻击 */
	private String normal;
	/** 技能攻击(目前可以用序列取代,预留) */
	private String[] skills;
	/** 阶段技能 */
	private Map<Stage, String> stages = new HashMap<>();
	/** 攻击序列 */
	private String[] sequence;
	/** 队长技 */
	private String captainSkill;

	// cache ...
	/** 战斗单元缓存 */
	private volatile transient Unit unit;
	/** 根据等级缓存 */
	private volatile transient Map<Integer, Unit> units = new ConcurrentHashMap<>();

	@Override
	public boolean isValid() {
		for (String seqSkill : sequence) {
			boolean result = false;
			if (normal != null && normal.equals(seqSkill)) {
				result = true;
			}
			if (skills != null) {
				for (String skill : skills) {
					if (skill.equals(seqSkill)) {
						result = true;
					}
				}
			}
			if (!result) {
				logger.error("出手序列[{}]不存在于普通攻击和技能攻击中", seqSkill);
				return false;
			}
		}
		return true;
	}

	public boolean hasStages() {
		return !CollectionUtils.isEmpty(stages);
	}

	// Getter and Setter ...

	/** 根据等级获取战斗单元 */
	public Unit getUnitByLevel(int level) {
		return units.get(level);
	}

	public void setUnit(Unit unit, int level) {
		units.put(level, unit);
	}

	public String getId() {
		return id;
	}

	void setId(String id) {
		this.id = id;
	}

	public int getLevel() {
		return level;
	}

	void setLevel(int level) {
		this.level = level;
	}

	public short getModel() {
		return model;
	}

	void setModel(byte model) {
		this.model = model;
	}

	public Map<UnitValue, Integer> getValues() {
		return values;
	}

	void setValues(Map<UnitValue, Integer> values) {
		this.values = values;
	}

	public String getUnique() {
		return unique;
	}

	void setUnique(String unique) {
		this.unique = unique;
	}

	public String getNormal() {
		return normal;
	}

	void setNormal(String normal) {
		this.normal = normal;
	}

	public String[] getSkills() {
		return skills;
	}

	void setSkills(String[] skills) {
		this.skills = skills;
	}

	public Map<Stage, String> getStages() {
		return stages;
	}

	void setStages(Map<Stage, String> stages) {
		this.stages = stages;
	}

	public String[] getSequence() {
		return sequence;
	}

	void setSequence(String[] sequence) {
		this.sequence = sequence;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public Map<UnitValue, Integer> getRadios() {
		return radios;
	}

	public String getCaptainSkill() {
		return captainSkill;
	}

	public int getFightRadio() {
		return fightRadio;
	}

	public short getCode() {
		return code;
	}

	/**
	 * 获取所有技能
	 * @return
	 */
	public Map<String, Skill> getSkillsAll() {
		// 所有技能
		Map<String, Skill> all = new HashMap<>();
		// 构建普通攻击
		Skill normalSkill = SkillFactory.getInstance().getSkill(normal);
		all.put(normal, normalSkill);
		// 构建技能攻击
		if (skills != null) {
			for (String sid : skills) {
				Skill skill = SkillFactory.getInstance().getSkill(sid);
				all.put(sid, skill);
			}
		}

		// 构建大招
		if (unique != null) {
			Skill uniqueSkill = SkillFactory.getInstance().getSkill(unique);
			all.put(unique, uniqueSkill);
		}
		// 构建技能阶段
		for (Entry<Stage, String> entry : stages.entrySet()) {
			if (entry.getKey() != Stage.CORRECT) { // 修正阶段技能不需要构造skill
				Skill skill = SkillFactory.getInstance().getSkill(entry.getValue());
				all.put(entry.getValue(), skill);
			}
		}
		return all;
	}

}
