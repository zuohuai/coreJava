package com.edu.game;

/**
 * 玩家实体定义
 * @author Administrator
 */
public class Player {
	/** 玩家ID */
	private long id;

	public Player valueOf(long id) {
		Player entity = new Player();
		entity.id = id;
		return entity;
	}

	public long getId() {
		return id;
	}
}
