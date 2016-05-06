package com.edu.game;

import org.springframework.stereotype.Component;

/**
 * 地图服务推送类
 * @author Administrator
 */
@Component
public class MapService {

	public void pushFightState(Player player, boolean flag) {
		System.out.println(player.getId() + "++地图推送服务++");
	}
}
