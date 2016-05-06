package com.edu.game.dota.fight.model.report;

import com.eyu.snm.module.fight.model.AlterType;

/**
 * 值变更对象
 * @author Kent
 */
public class Alter {

	/** 值类型 */
	private AlterType type;
	/** 变更值 */
	private int value;
	/** 最新值 */
	private int current;

	public Alter(AlterType type, int value, int current) {
		this.type = type;
		this.value = value;
		this.current = current;
	}

	public boolean isReportable() {
		return type.isReportable();
	}

	public void merge(Alter alter) {
		this.value += alter.getValue();
		this.current += alter.getCurrent();
	}

	public AlterType getType() {
		return type;
	}

	public int getValue() {
		return value;
	}

	public int getCurrent() {
		return current;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

}
