package com.edu.game.dota.fight.model.info;

import java.util.HashMap;

import com.eyu.common.protocol.annotation.Transable;
import com.eyu.snm.module.fight.model.UnitValue;
import com.eyu.snm.module.fight.service.core.Unit;

/**
 * 战斗单元vo
 * @author shenlong
 */
public class UnitVo {

	/** 战斗单元表配置标识,如玩法不需要可以为空 */
	private String baseId;
	/** 标识 */
	private short id;
	/** 等级 */
	private int level;
	/** 战斗单元原始数据 */
	private HashMap<UnitValue, Integer> origin = new HashMap<UnitValue, Integer>(UnitValue.values().length);
	/** 数值属性 */
	private HashMap<UnitValue, Integer> values = new HashMap<UnitValue, Integer>(UnitValue.values().length);

	public static UnitVo valueOf(Unit unit) {
		UnitVo result = new UnitVo();
		result.baseId = unit.getBaseId();
		result.id = unit.getId();
		result.level = unit.getLevel();
		result.origin = new HashMap<>(unit.getOrigin());
		result.values = new HashMap<>(unit.getValues());
		return result;
	}

	public String getBaseId() {
		return baseId;
	}

	public void setBaseId(String baseId) {
		this.baseId = baseId;
	}

	public short getId() {
		return id;
	}

	public void setId(short id) {
		this.id = id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public HashMap<UnitValue, Integer> getOrigin() {
		return origin;
	}

	public void setOrigin(HashMap<UnitValue, Integer> origin) {
		this.origin = origin;
	}

	public HashMap<UnitValue, Integer> getValues() {
		return values;
	}

	public void setValues(HashMap<UnitValue, Integer> values) {
		this.values = values;
	}

}
