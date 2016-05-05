package com.edu.game.jct.fight.service;

import java.util.Date;

/**
 * 战斗超时处理
 * @author Frank
 */
public class OvertimeWork {

	/** 战斗标识 */
	private Integer id;
	/** 超时处理类型 */
	private OvertimeType type;
	/** 超时时间 */
	private Date overtime;
	/** 对应回合数({@link OvertimeType#ROUND}) */
	private int round;

	private OvertimeWork(int id, OvertimeType type, Date overtime, int round) {
		this.id = id;
		this.type = type;
		this.overtime = overtime;
		this.round = round;
	}

	// Getter and Setter ...

	public Integer getId() {
		return id;
	}

	public OvertimeType getType() {
		return type;
	}

	public Date getOvertime() {
		return overtime;
	}

	public int getRound() {
		return round;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + round;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OvertimeWork other = (OvertimeWork) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (round != other.round)
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	/** 战斗超时的构造器 */
	public static OvertimeWork valueOfBattle(int id, Date overtime) {
		return new OvertimeWork(id, OvertimeType.BATTLE, overtime, 0);
	}

	/** 回合超时的构造器 */
	public static OvertimeWork valueOfRound(int id, int round, Date overtime) {
		return new OvertimeWork(id, OvertimeType.ROUND, overtime, round);
	}

	/** 恢复超时的构造器 */
	public static OvertimeWork valueOfRestore(int id, Date overtime) {
		return new OvertimeWork(id, OvertimeType.RESTORE, overtime, 0);
	}
}
