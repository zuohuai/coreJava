package com.edu.game.dota.fight.resource;

import com.edu.game.resource.anno.Id;
import com.edu.game.resource.anno.Resource;

/**
 * 怪物等级对应能力系数表
 * @author shenlong
 */
@Resource("dota")
public class LevelRadioSetting {

	/** 玩家等级 */
	@Id
	private int level;
	/** 系数值 */
	private int radio;

	public int getLevel() {
		return level;
	}

	public int getRadio() {
		return radio;
	}

}
