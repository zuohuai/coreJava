package com.edu.game.dota.fight.service.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edu.game.dota.fight.model.UnitState;
import com.edu.game.dota.fight.model.report.ActionReport;
import com.edu.game.dota.fight.model.report.StageReport;
import com.edu.game.dota.fight.service.effect.EffectConfig;

/**
 * 技能对象
 * @author Frank
 */
public class Skill {

	private Logger logger = LoggerFactory.getLogger(Skill.class);

	/** 技能执行 ID */
	private Short executeId;
	/** 技能效果计算原始单位 */
	private Unit owner;
	/** 技能类型 */
	private SkillType type;

	/** 技能标识 */
	private String id;
	/** 允许施放的许可条件 */
	private Selector[] allow;
	/** 在判定阶段已经被选中的目标 */
	private List<Unit> targets;

	/** 阶段效果 */
	private List<EffectConfig>[] effects;
	/** 各阶段的目标选择类型 */
	private Selector[][] selectorses;
	/** 执行阶段计数器 */
	private byte step;
	/** 能否被打断 */
	private boolean[] canBreaks;
	/** 能否被硬直 */
	private boolean[] canHrs;
	/** 技能位置信息 */
	private Position[] positions;
	/** 黑屏时间(只有大招有) */
	private Integer bigSkillTime;

	/**
	 * 返回一个技能的克隆对象
	 * @param owner
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Skill clone(Unit owner) {
		Skill skill = new Skill();
		skill.owner = owner;
		skill.id = id;
		skill.allow = allow;
		skill.selectorses = selectorses;
		skill.canBreaks = canBreaks;
		skill.canHrs = canHrs;
		skill.type = type;
		skill.effects = new List[effects.length];
		for (int i = 0; i < effects.length; ++i) {
			skill.effects[i] = new ArrayList<>(effects[i].size());
			for (EffectConfig ec : effects[i]) {
				skill.effects[i].add(ec.clone());
			}
		}

		return skill;
	}

	/**
	 * 技能是否符合施放条件
	 * @return
	 */
	public boolean isAllow() {
		// 禁止攻击状态不能攻击
		if (type == SkillType.UNIT) {
			if (owner.hasState(UnitState.FORBID_ATTACK)) {
				return false;
			}
		}
		// 只在技能执行第一阶段执行
		if (step == 0 && (targets == null || targets.isEmpty())) {
			targets = TargetHelper.select(owner, null, allow, positions);
		}
		if (targets.isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * 执行技能效果并返回动作上下文信息
	 * @return
	 */
	public ActionReport execute() {

		if (logger.isDebugEnabled()) {
			logger.debug("[{}]正在执行技能[{}]", executeId, id);
		}
		// 确定执行目标
		Selector[] selectors = selectorses[step];
		targets = TargetHelper.select(owner, targets, selectors, positions);
		// 执行效果
		StageReport ret = StageReport.valueOf(owner, executeId, id, step);
		// 效果执行上下文
		Map<String, Object> context = new HashMap<>();
		List<EffectConfig> effects = this.effects[step];
		for (EffectConfig effect : effects) {
			try {
				effect.execute(ret, context, owner, targets);
			} catch (Exception e) {
				throw new RuntimeException("技能" + id + "在阶段" + step + "执行出错,出错效果" + effect.getType(), e);
			}
		}
		if (type == SkillType.UNIT) {
			if (++step == selectorses.length) {
				interrupt();
				owner.finishSkill();
			}
		}
		return ret;
	}

	/**
	 * 打断技能(重置技能执行状态)
	 */
	public void interrupt() {
		step = 0;
		targets = null;
	}

	/**
	 * 检查技能是否在施放中
	 * @return
	 */
	public boolean isInProgress() {
		return step != 0;
	}

	public boolean canBreak() {
		if (step == 0) {
			return false;
		}
		return canBreaks[step];
	}

	public boolean canHr() {
		if (step == 0) {
			return true;
		}
		return canHrs[step];
	}

	// Getter and Setter ...

	public Unit getOwner() {
		return owner;
	}

	public void setOwner(Unit owner) {
		this.owner = owner;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Short getExecuteId() {
		return executeId;
	}

	public void setExecuteId(short executeId) {
		this.executeId = executeId;
	}

	public Selector[] getAllow() {
		return allow;
	}

	public void setAllow(Selector[] allow) {
		this.allow = allow;
	}

	public List<Unit> getTargets() {
		return targets;
	}

	public void setTargets(List<Unit> targets) {
		this.targets = targets;
	}

	public void addTarget(Unit target) {
		if (targets == null) {
			targets = new ArrayList<>();
		}
		targets.add(target);
	}

	public List<EffectConfig>[] getEffects() {
		return effects;
	}

	public void setEffects(List<EffectConfig>[] effects) {
		this.effects = effects;
	}

	public Selector[][] getSelectorses() {
		return selectorses;
	}

	public void setSelectorses(Selector[][] selectorses) {
		this.selectorses = selectorses;
	}

	public byte getStep() {
		return step;
	}

	void setStep(byte step) {
		this.step = step;
	}

	public Position[] getPositions() {
		return positions;
	}

	public void setPositions(Position... positions) {
		this.positions = positions;
	}

	public Integer getBigSkillTime() {
		return bigSkillTime;
	}

	public void setBigSkillTime(Integer bigSkillTime) {
		this.bigSkillTime = bigSkillTime;
	}

	public static Skill valueOf(String id, Selector[] allow, List<EffectConfig>[] effects, Selector[][] selectorses, SkillType skillType, boolean[] canBreaks, boolean[] canHrs, Integer bigSkillTime) {
		Skill skill = new Skill();
		skill.id = id;
		skill.allow = allow;
		skill.effects = effects;
		skill.selectorses = selectorses;
		skill.type = skillType;
		skill.canBreaks = canBreaks;
		skill.canHrs = canHrs;
		skill.bigSkillTime = bigSkillTime;
		return skill;
	}

}
