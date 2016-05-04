package com.eyu.ahxy.module.fight.model.report;

import java.util.ArrayList;
import java.util.List;

import com.eyu.ahxy.module.fight.model.AlterType;
import com.eyu.ahxy.module.fight.model.AlterValue;
import com.eyu.ahxy.module.fight.model.UnitInfo;
import com.eyu.ahxy.module.fight.service.core.Unit;
import com.eyu.ahxy.module.fight.service.effect.skill.SkillEffect;
import com.my9yu.common.protocol.annotation.Transable;

/**
 * 技能效果({@link SkillEffect})对于每个承受目标的战报
 * @author Frank
 */
@Transable
public class TargetReport {
	
	/** 承受目标标识 */
	private String target;
	/** 技能施放状态 */
	private int state;
	/** 变更值 */
	private AlterValue value;
	/** 召唤的战斗单元 */
	private List<UnitInfo> units;
	/** BUFF的战报信息 */
	private List<BuffReport> buffs;
	/** 调整阶段的被动效果 */
	private List<PassiveReport> adjusts;
	/** 防御阶段的被动效果 */
	private List<PassiveReport> defences;
	/** 结束阶段的被动效果 */
	private List<PassiveReport> ends;
	
	/** 添加新的战斗单元 */
	public void addUnit(Unit unit) {
		if (units == null) {
			units = new ArrayList<UnitInfo>();
		}
		units.add(UnitInfo.valueOf(unit));
	}

	/** 添加BUFF战报信息 */
	public void addBuff(BuffReport report) {
		if (buffs == null) {
			buffs = new ArrayList<BuffReport>(1);
		}
		buffs.add(report);
	}

	/** 添加被动效果(调整阶段) */
	public void addAdjust(PassiveReport report) {
		if (adjusts == null) {
			adjusts = new ArrayList<PassiveReport>(1);
		}
		adjusts.add(report);
	}

	/** 添加被动效果(防御阶段) */
	public void addDefence(PassiveReport report) {
		if (defences == null) {
			defences = new ArrayList<PassiveReport>(1);
		}
		defences.add(report);
	}

	/** 添加被动效果(结束阶段) */
	public void addEnd(PassiveReport report) {
		if (ends == null) {
			ends = new ArrayList<PassiveReport>(1);
		}
		ends.add(report);
	}
 
	/** 设置闪避 */
	public void setDodgy() {
		state = state | EffectState.DODGY;
	}

	/** 设置暴击 */
	public void setCrit() {
		state = state | EffectState.CRIT;
	}

	/** 设置破击 */
	public void setFatal() {
		state = state | EffectState.FATAL;
	}

	/** 设置施放失败 */
	public void setFail() {
		state = state | EffectState.FAIL;
	}
	
	/** 清理失败效果 */
	public void clearFail() {
		state = state & ~EffectState.FAIL;
	}
	
	/** 判断是否失败 */
	public boolean isFail() {
		return (state & EffectState.FAIL) != 0;
	}

	/**
	 * 添加修改的属性值
	 * @param type 属性键
	 * @param value 修改值
	 * @return
	 */
	public void addValue(AlterType type, Number value) {
		if (this.value == null) {
			this.value = new AlterValue();
		}
		this.value.addValue(type, value);
	}

	// Getter and Setter ...

	public String getTarget() {
		return target;
	}

	protected void setTarget(String target) {
		this.target = target;
	}

	public int getState() {
		return state;
	}

	protected void setState(int state) {
		this.state = state;
	}

	public AlterValue getValue() {
		return value;
	}

	protected void setValue(AlterValue value) {
		this.value = value;
	}

	public List<BuffReport> getBuffs() {
		return buffs;
	}

	protected void setBuffs(List<BuffReport> buffs) {
		this.buffs = buffs;
	}

	public List<PassiveReport> getAdjusts() {
		return adjusts;
	}

	protected void setAdjusts(List<PassiveReport> adjusts) {
		this.adjusts = adjusts;
	}

	public List<PassiveReport> getDefences() {
		return defences;
	}

	protected void setDefences(List<PassiveReport> defences) {
		this.defences = defences;
	}

	public List<PassiveReport> getEnds() {
		return ends;
	}

	protected void setEnds(List<PassiveReport> ends) {
		this.ends = ends;
	}
	
	public List<UnitInfo> getUnits() {
		return units;
	}

	protected void setUnits(List<UnitInfo> units) {
		this.units = units;
	}

	// 构造方法

	/** 构造方法 */
	public static TargetReport valueOf(Unit target) {
		TargetReport result = new TargetReport();
		result.target = target.getId();
		return result;
	}

}
