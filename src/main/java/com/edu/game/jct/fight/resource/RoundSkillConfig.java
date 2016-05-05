package com.edu.game.jct.fight.resource;

import com.edu.game.jct.fight.model.RoundSkillType;
import com.edu.game.jct.fight.service.effect.round.RoundSkillState;
import com.edu.game.resource.anno.Id;
import com.edu.game.resource.anno.Resource;

/**
 * 回合技能配置
 * @author Frank
 */
@Resource("fight")
public class RoundSkillConfig {

	/** 标识 */
	@Id
	private String id;
	/** 回合技能类型 */
	private RoundSkillType type;
	/** 回合技能状态 */
	private RoundSkillState state;

	// Getter and Setter ...

	public String getId() {
		return id;
	}

	public RoundSkillType getType() {
		return type;
	}

	public RoundSkillState getState() {
		return state.clone(id);
	}

}
