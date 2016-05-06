package com.edu.game.dota.fight.resource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.edu.game.dota.fight.service.core.Fighter;
import com.edu.game.resource.anno.Id;
import com.edu.game.resource.anno.Resource;

/**
 * 战斗单位组配置
 * @author Frank
 */
@Resource("dota")
public class FighterSetting {

	/** 标识 */
	@Id
	private String id;
	/** 等级 */
	private int level;
	/** 国家 */
	private Country country;
	/** 名称 */
	private String name;
	/** 队长ID */
	private String captainId;
	/** 出战单位标识 */
	private String[] units;

	/** 战斗单位缓存 */
	private volatile transient Fighter cache;
	private volatile transient Map<Integer, Fighter> caches = new ConcurrentHashMap<>();

	public Fighter getCache() {
		return cache;
	}

	/** 根据等级获取NPC缓存 */
	public Fighter getCacheByLevel(int level) {
		return caches.get(level);
	}

	public void setCache(Fighter cache) {
		this.cache = cache;
	}

	public void setCache(Fighter cache, int level) {
		caches.put(level, cache);
	}

	// Getter and Setter ...

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String[] getUnits() {
		return units;
	}

	public String getCaptainId() {
		return captainId;
	}

	public int getLevel() {
		return level;
	}

	public Country getCountry() {
		return country;
	}

}
