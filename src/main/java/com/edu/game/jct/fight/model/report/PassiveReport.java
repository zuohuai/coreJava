package com.edu.game.jct.fight.model.report;

import com.edu.game.jct.fight.model.AlterType;
import com.edu.game.jct.fight.model.AlterValue;
import com.edu.game.jct.fight.service.effect.passive.PassiveState;

/**
 * 被动效果战报
 * @author Frank
 */
public class PassiveReport {

	/** 被动效果标识 */
	private String id;
	/** 变更值 */
	private AlterValue value;
	/** 由被动效果添加的BUFF信息 */
	private BuffReport buff;
	
	public void addBuff(BuffReport report) {
		this.buff = report;
	}

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

	protected void setId(String id) {
		this.id = id;
	}

	public AlterValue getValue() {
		return value;
	}

	protected void setValue(AlterValue value) {
		this.value = value;
	}
	
	public BuffReport getBuff() {
		return buff;
	}
	
	protected void setBuff(BuffReport buff) {
		this.buff = buff;
	}
	
	// 构造方法

	/** 构造方法 */
	public static PassiveReport valueOf(PassiveState state) {
		PassiveReport result = new PassiveReport();
		result.id = state.getId();
		return result;
	}

}
