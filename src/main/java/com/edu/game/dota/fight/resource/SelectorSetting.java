package com.edu.game.dota.fight.resource;

import com.edu.game.dota.fight.service.core.Selector;
import com.edu.game.resource.anno.Id;
import com.edu.game.resource.anno.Resource;

/**
 * 选择器配置
 * @author Kent
 */
@Resource("dota")
public class SelectorSetting {

	/** 标识 */
	@Id
	private String id;
	/** 选择器组合 */
	private Selector[] selectors;

	public String getId() {
		return id;
	}

	public Selector[] getSelectors() {
		return selectors;
	}

	

}
