package com.eyu.ahxy.module.fight.model.report;

import com.eyu.ahxy.module.fight.model.AlterType;
import com.eyu.ahxy.module.fight.model.AlterValue;
import com.eyu.ahxy.module.fight.service.effect.buff.BuffState;
import com.my9yu.common.protocol.annotation.Transable;

/**
 * BUFF 战报
 * @author Frank
 */
@Transable
public class BuffReport {

	/** BUFF战报类型 */
	private BuffAlterType type;
	/** BUFF ID */
	private String id;
	/** 剩余次数 */
	private int times;
	/** 变更值 */
	private AlterValue value;

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BuffAlterType getType() {
		return type;
	}

	public void setType(BuffAlterType type) {
		this.type = type;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public AlterValue getValue() {
		return value;
	}

	public void setValue(AlterValue value) {
		this.value = value;
	}

	// Static Method's ...

	/** 构造方法 */
	public static BuffReport addOf(BuffState state) {
		BuffReport result = new BuffReport();
		result.type = BuffAlterType.ADD;
		result.id = state.getId();
		result.times = state.getTimes();
		return result;
	}

	/** 构造方法 */
	public static BuffReport removeOf(BuffState state) {
		BuffReport result = new BuffReport();
		result.type = BuffAlterType.REMOVE;
		result.id = state.getId();
		return result;
	}

	/** 构造方法 */
	public static BuffReport updateOf(BuffState state) {
		BuffReport result = new BuffReport();
		result.type = BuffAlterType.UPDATE;
		result.id = state.getId();
		result.times = state.getTimes();
		return result;
	}

	/** 构造方法 */
	public static BuffReport immuneOf() {
		BuffReport result = new BuffReport();
		result.type = BuffAlterType.IMMUNE;
		return result;
	}

}
