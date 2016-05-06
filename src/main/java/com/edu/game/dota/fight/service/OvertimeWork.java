package com.edu.game.dota.fight.service;

import java.util.Date;

/**
 * 超时未传入操作数据的战斗任务
 * @author shenlong
 */
public class OvertimeWork {

	/** 战斗标识 */
	private int id;
	/** 对应的玩家ID */
	private long owner;
	/** 超时时间 */
	private Date overtime;

	public static OvertimeWork valueOf(long owner, int id, Date overtime) {
		OvertimeWork oWork = new OvertimeWork();
		oWork.id = id;
		oWork.owner = owner;
		oWork.overtime = overtime;
		return oWork;
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
		if (id != other.id) {
			return false;
		}
		return true;
	}

	public long getOwner() {
		return owner;
	}

	public void setOwner(long owner) {
		this.owner = owner;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getOvertime() {
		return overtime;
	}

	public void setOvertime(Date overtime) {
		this.overtime = overtime;
	}

}
