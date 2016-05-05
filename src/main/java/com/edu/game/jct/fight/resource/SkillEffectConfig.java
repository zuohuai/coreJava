package com.edu.game.jct.fight.resource;

import com.edu.game.jct.fight.service.effect.skill.EffectState;
import com.edu.game.jct.fight.service.effect.skill.EffectType;
import com.edu.game.resource.anno.Id;
import com.edu.game.resource.anno.Resource;

/**
 * 技能效果配置
 * @author Frank
 */
@Resource("fight")
public class SkillEffectConfig {

	@Id
	private String id;
	/** 技能效果类型 */
	private EffectType type;
	/** 技能效果状态 */
	private EffectState state;
	
	// Getter and Setter ...

	public String getId() {
		return id;
	}

	public EffectType getType() {
		return type;
	}

	public EffectState getState() {
		return state.clone(id);
	}

}
