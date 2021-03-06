package com.edu.mapEditor.model;

/**
 * 用来标识地图某一个坐标的位置
 * @author Administrator
 */
public enum State {
	/** 通过 */
	UNBLOCK(0),
	/** 阻塞 */
	BLOCK(1),
	/** 穿过去 */
	ACROSS(2);

	private int state;

	private State(int state) {
		this.state = state;
	}

	public int getState() {
		return state;
	}

}
