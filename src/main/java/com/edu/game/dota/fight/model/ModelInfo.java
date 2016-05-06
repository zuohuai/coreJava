package com.edu.game.dota.fight.model;

import com.eyu.common.protocol.annotation.Transable;
import com.eyu.snm.module.player.model.Country;

/**
 * 队伍显示信息
 * @author shenlong
 */
public class ModelInfo {

	/** 战斗单位名称 */
	private String name;
	/** 队长模型ID */
	private short captainId;
	/** 队伍等级 */
	private int level;
	/** 队伍国家 */
	private Country country;
	/** 战斗力 */
	private int fight;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public short getCaptainId() {
		return captainId;
	}

	public void setCaptainId(short captainId) {
		this.captainId = captainId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public int getFight() {
		return fight;
	}

	public void setFight(int fight) {
		this.fight = fight;
	}

	public static ModelInfo valueOf(String name, short captainId, int level, Country country) {
		ModelInfo result = new ModelInfo();
		result.name = name;
		result.captainId = captainId;
		result.level = level;
		result.country = country;
		return result;

	}
}
