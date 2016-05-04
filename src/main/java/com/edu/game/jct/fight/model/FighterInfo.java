package com.edu.game.jct.fight.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.edu.game.Player;
import com.edu.game.jct.fight.service.core.Fighter;
import com.edu.game.jct.fight.service.core.Unit;

/**
 * 战斗单位信息
 * @author adminstrator 
 * 
 */
public class FighterInfo {
	/** 战斗单位标识 */
	private String id;
	/** 当前出战中的战斗单元信息 */
	private UnitInfo[][] current;
	/** 总战斗单元波数 */
	private int total;
	/** 当前的战斗单元波数 */
	private int idx;
	/** 玩家与战斗单元的关系 */
	private Map<Long, String> players;
	/** 附加信息 */
	private Map<String, Object> additions;
	// Getter and Setter ...

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UnitInfo[][] getCurrent() {
		return current;
	}

	public void setCurrent(UnitInfo[][] current) {
		this.current = current;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public Map<Long, String> getPlayers() {
		return players;
	}

	public void setPlayers(Map<Long, String> players) {
		this.players = players;
	}

	public Map<String, Object> getAdditions() {
		return additions;
	}

	public void setAdditions(Map<String, Object> additions) {
		this.additions = additions;
	}


	// Static's Method

	/** 构造方法 */
	public static FighterInfo valueOf(Fighter fighter) {
		Unit[][] units = fighter.getCurrents();
		UnitInfo[][] current = new UnitInfo[Position.ROW_SIZE][Position.COLUMN_SIZE];
		for (int r = 0; r < Position.ROW_SIZE; r++) {
			for (int c = 0; c < Position.COLUMN_SIZE; c++) {
				Unit unit = units[r][c];
				if (unit != null) {
					current[r][c] = UnitInfo.valueOf(unit);
				}
			}
		}
		FighterInfo result = new FighterInfo();
		result.id = fighter.getId();
		result.current = current;
		result.total = fighter.getUnits().size();
		result.idx = fighter.getIdx();
		Map<Player, Unit> owners = fighter.getOwners();
		if (!owners.isEmpty()) {
			result.players = new HashMap<Long, String>(owners.size());
			for (Entry<Player, Unit> entry : owners.entrySet()) {
				result.players.put(entry.getKey().getId(), entry.getValue().getId());
			}
		}
		result.additions = fighter.getAddtions();
		return result;
	}


}
