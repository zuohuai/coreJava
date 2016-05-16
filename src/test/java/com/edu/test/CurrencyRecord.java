package com.edu.test;

/**
 * 货币日志格式
 * @author Birdy
 */
public class CurrencyRecord {

	/**
	 * 用户ID
	 */
	private long userId;

	/**
	 * 类型
	 */
	private int type;

	private String source;

	private int change;

	private long current;

	private long time;

	private int vip;

	private String information;

	public String getInformation() {
		return this.information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	CurrencyRecord() {
	}

	public CurrencyRecord(String source, long createdAt, long userId, int type, int change, long current, int vip) {
		this.userId = userId;
		this.type = type;
		this.source = source;
		this.change = change;
		this.current = current;
		this.vip = vip;
		this.time = createdAt;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getChange() {
		return change;
	}

	public void setChange(int change) {
		this.change = change;
	}

	public long getCurrent() {
		return current;
	}

	public void setCurrent(long current) {
		this.current = current;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getVip() {
		return vip;
	}

	public void setVip(int vip) {
		this.vip = vip;
	}

}
