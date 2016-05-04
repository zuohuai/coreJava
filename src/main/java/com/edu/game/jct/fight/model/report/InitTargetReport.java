package com.edu.game.jct.fight.model.report;

import java.util.ArrayList;
import java.util.List;

import com.edu.game.jct.fight.model.AlterType;
import com.edu.game.jct.fight.model.AlterValue;
import com.edu.game.jct.fight.service.core.Unit;

/**
 * 初始化效果对于每个承受目标的战报
 * @author Frank
 */
public class InitTargetReport {
	
	/** 承受目标标识 */
	private String target;
	/** 施放状态 */
	private int state;
	/** 变更值 */
	private AlterValue value;
	/** BUFF的战报信息 */
	private List<BuffReport> buffs;
	
	/** 添加BUFF战报信息 */
	public void addBuff(BuffReport report) {
		if (buffs == null) {
			buffs = new ArrayList<BuffReport>(1);
		}
		buffs.add(report);
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

	// 构造方法

	/** 构造方法 */
	public static InitTargetReport valueOf(Unit target) {
		InitTargetReport result = new InitTargetReport();
		result.target = target.getId();
		return result;
	}

}
