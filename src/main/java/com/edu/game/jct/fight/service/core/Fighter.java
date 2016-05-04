package com.edu.game.jct.fight.service.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edu.game.Player;
import com.edu.game.jct.fight.model.FighterInfo;

/**
 * 战斗单位对象 通过该对象来标识战斗中的攻击方和防御方
 * @author Administrator
 */
public class Fighter {
	private static final Logger LOGGER = LoggerFactory.getLogger(Fighter.class);

	/** 攻击方标识前缀 */
	public static final String ATTACKER_PREFIX = "A:";
	/** 防御方标识前缀 */
	public static final String DEFENDER_PREFIX = "D:";
	/** 战斗单位标识 */
	private String id;
	/** 玩家和战斗单元的关系 */
	private Map<Player, Unit> owners = new HashMap<>(0);
	/** 主将战斗单位 */
	private List<Unit> majors = new LinkedList<>();
	/** 当前的战斗单元 */
	private Unit[][] currents;
	/** 当前全部的战斗单元 */
	private List<Unit[][]> units;
	/** 当前的战斗单元序号 */
	private int idx;
	/** 附加信息 */
	private Map<String, Object> addtions;

	public String getId() {
		return id;
	}

	public Map<Player, Unit> getOwners() {
		return owners;
	}

	public List<Unit> getMajors() {
		return majors;
	}

	public Unit[][] getCurrents() {
		return currents;
	}

	public List<Unit[][]> getUnits() {
		return units;
	}

	public int getIdx() {
		return idx;
	}

	public Map<String, Object> getAddtions() {
		return addtions;
	}

	/**
	 * 获取战斗单位信息
	 * @return
	 */
	public FighterInfo createFighterInfo() {
		return FighterInfo.valueOf(this);
	}
}
