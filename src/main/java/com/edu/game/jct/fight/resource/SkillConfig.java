package com.edu.game.jct.fight.resource;

import com.edu.game.jct.fight.service.effect.skill.SkillState;
import com.edu.game.resource.anno.Id;
import com.edu.game.resource.anno.Resource;

/**
 * 主动技能配置类
 * @author frank
 */
@Resource("fight")
public class SkillConfig {

	/** 技能标识 */
	@Id
	private String id;
	/** 技能的初始化状态 */
	private SkillState state;
	/** 技能能否施放的判断条件 */
	private String[] conditions;
	/** 技能的执行效果 */
	private String[] effects;
	
	// Getter and Setter ...

	public String getId() {
		return id;
	}

	public SkillState getState() {
		return state.clone(id);
	}

	public String[] getConditions() {
		return conditions;
	}

	public String[] getEffects() {
		return effects;
	}

}
