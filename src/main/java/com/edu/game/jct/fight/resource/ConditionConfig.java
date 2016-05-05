package com.edu.game.jct.fight.resource;

import com.edu.game.jct.fight.service.effect.select.SelectType;
import com.edu.game.jct.fight.service.effect.skill.condition.ConditionType;
import com.edu.game.resource.anno.Id;
import com.edu.game.resource.anno.Resource;

/**
 * 技能施放的判断条件配置
 * @author Frank
 */
@Resource("fight")
public class ConditionConfig {

	@Id
	private String id;
	/** 选择目标 */
	private SelectType target;
	/** 判断条件类型 */
	private ConditionType type;
	/** 判断条件值 */
	private String value;
	
	// Getter and Setter ...

	public String getId() {
		return id;
	}

	public SelectType getTarget() {
		return target;
	}

	public ConditionType getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

}
