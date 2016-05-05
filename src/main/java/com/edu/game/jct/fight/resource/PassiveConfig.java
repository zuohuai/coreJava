package com.edu.game.jct.fight.resource;

import com.edu.game.jct.fight.service.effect.passive.PassiveState;
import com.edu.game.jct.fight.service.effect.passive.PassiveType;
import com.edu.game.resource.anno.Id;
import com.edu.game.resource.anno.Resource;

/**
 * 被动效果配置对象
 * @author Frank
 */
@Resource("fight")
public class PassiveConfig {

	/** 被动效果标识 */
	@Id
	private String id;
	/** 被动效果类型 */
	private PassiveType type;
	/** 被动技能状态 */
	private PassiveState state;
	
	// Getter and Setter ...

	public String getId() {
		return id;
	}

	public PassiveType getType() {
		return type;
	}

	public PassiveState getState() {
		return state.clone(id);
	}

}
