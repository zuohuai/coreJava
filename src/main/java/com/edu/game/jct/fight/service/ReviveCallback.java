package com.edu.game.jct.fight.service;

/**
 * 复活回调函数
 * @author qu.yy
 */
public interface ReviveCallback {

	/**
	 * 复活
	 * @param battle
	 * @return
	 */
	boolean revive(Battle battle);
}
