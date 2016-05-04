package com.edu.game.jct.fight.model;

import com.edu.game.resource.JsonObject;

/**
 * 战斗单位的模型信息
 * @author Frank
 */
public class ModelInfo implements Cloneable, JsonObject {

	/** 单位名 */
	private String name;
	/** 单位类型 / 兵种 */
	private UnitType type;

	public static ModelInfo valueOf(String name, UnitType type) {
		ModelInfo info = new ModelInfo();
		info.name = name;
		info.type = type;
		return info;
	}

	public String getName() {
		return name;
	}

	public UnitType getType() {
		return type;
	}

}
