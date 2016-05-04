package com.edu.game.jct.fight.resource;

import com.edu.game.jct.fight.service.effect.buff.BuffState;
import com.edu.game.jct.fight.service.effect.buff.BuffType;
import com.edu.game.resource.anno.Id;
import com.edu.game.resource.anno.Resource;

/**
 * BUFF效果配置对象
 * @author administrator
 */
@Resource("fight")
public class BuffConfig {

	/** BUFF效果标识 */
	@Id
	private String id;
	/** BUFF效果类型 */
	private BuffType type;
	/** BUFF状态 */
	private BuffState state;
	
	// Getter and Setter ...

	public String getId() {
		return id;
	}

	public BuffType getType() {
		return type;
	}

	public BuffState getState() {
		return state.clone(id);
	}

}
