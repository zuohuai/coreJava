package com.edu.game.dota.fight.resource;

import java.util.Map;

import com.edu.game.dota.fight.service.effect.EffectType;
import com.edu.game.resource.anno.Id;
import com.edu.game.resource.anno.Resource;

/**
 * 效果配置
 * @author Frank
 */
@Resource("dota")
public class EffectSetting {

	/** 标识 */
	@Id
	private String id;
	/** 战报编码 */
	private short code;
	/** 效果类型 */
	private EffectType type;
	/** 效果配置 */
	private Map<String, Object> config;

	// Getter and Setter ...
	
	public String getId() {
		return id;
	}

	public short getCode() {
		return code;
	}

	public EffectType getType() {
		return type;
	}

	public Map<String, Object> getConfig() {
		return config;
	}

}
