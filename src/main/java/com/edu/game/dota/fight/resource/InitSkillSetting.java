package com.edu.game.dota.fight.resource;

import com.edu.game.dota.fight.service.effect.init.InitEffectState;
import com.edu.game.resource.anno.Id;
import com.edu.game.resource.anno.Resource;

/**
 * 队长技等特殊开场技配置
 * @author shenlong
 */
@Resource("dota")
public class InitSkillSetting {

	/** 初始化技能标识 */
	@Id
	private String id;
	/** 是否只做显示 */
	private boolean onlyShow;
	/** 技能编码 */
	private Short code;
	/** 地形 */
	private Terrain terrain;
	/** 初始化效果 */
	private InitEffectState[] effectStates;

	public String getId() {
		return id;
	}

	public Short getCode() {
		return code;
	}

	public Terrain getTerrains() {
		return terrain;
	}

	public boolean isOnlyShow() {
		return onlyShow;
	}

	public InitEffectState[] getInitEffectState() {
		return effectStates;
	}

}
