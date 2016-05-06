package com.edu.game.dota.fight.model.report.seed;

/**
 * 玩家操作数据
 * @author shenlong
 */
public class OperationData {

	/** 战斗单位id */
	private short id;
	/** 操作时间 */
	private int time;

	public short getId() {
		return id;
	}

	public void setId(short id) {
		this.id = id;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public static OperationData valueOf(short id, int time) {
		OperationData result = new OperationData();
		result.id = id;
		result.time = time;
		return result;
	}

}
