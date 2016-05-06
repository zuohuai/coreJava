package com.edu.game.dota.fight.model;

/**
 * 模型信息
 * @author Frank
 */
public class Model {

	public static Model valueOf(short id, UnitType type, int order) {
		Model ret = new Model();
		ret.id = id;
		ret.type = type;
		ret.order = order;
		return ret;
	}

	/** 模型标识 */
	private short id;
	/** 战斗单位类型 */
	private UnitType type;
	/** 排序值 */
	private int order;

	private Model() {
	}

	@Override
	public String toString() {
		return "{id:" + id + ", type:" + type + "}";
	}

	// Getter and Setter ...

	public short getId() {
		return id;
	}

	public UnitType getType() {
		return type;
	}

	public int getOrder() {
		return order;
	}

}
