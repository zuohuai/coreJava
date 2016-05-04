package com.edu.game.jct.fight.resource;

import com.edu.game.jct.fight.service.effect.init.InitEffectState;
import com.edu.game.jct.fight.service.effect.init.InitType;
import com.edu.game.resource.anno.Id;
import com.edu.game.resource.anno.Resource;

/**
 * 初始化技能配置
 * @author Administrator
 *
 */
@Resource("fight")
public class InitEffectConfig {
	/** 标识 */
	@Id
	private String id;
	/** 技能类型 */
	private InitType type;
	/** 技能状态 */
	private InitEffectState state;

	// Getter and Setter ...

	public String getId() {
		return id;
	}

	public InitType getType() {
		return type;
	}

	public InitEffectState getState() {
		return state.clone(id);
	}

}