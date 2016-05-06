package com.eyu.snm.module.fight.service.core;

import java.util.LinkedHashMap;

import com.eyu.common.utils.json.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * 目标选择器
 * @author Frank
 */
public class Selector {

	/** 过滤类型 */
	private SelectorType type;
	/** 过滤条件内容 */
	private String content;
	/** 选择器内容缓存 */
	private transient volatile Object cache;

	@SuppressWarnings("unchecked")
	public <T> T getContent(Class<T> clazz) {
		if (cache == null) {
			synchronized (this) {
				if (cache == null) {
					cache = JsonUtils.string2Object(content, clazz);
				}
			}
		}
		return (T) cache;
	}

	@SuppressWarnings("unchecked")
	public <T> T[] getArrayContent(Class<T> clazz) {
		if (cache == null) {
			synchronized (this) {
				if (cache == null) {
					cache = JsonUtils.string2Array(content, clazz);
				}
			}
		}
		return (T[]) cache;
	}

	@SuppressWarnings("unchecked")
	public <K, V> LinkedHashMap<K, V> getMapContent(Class<K> key, Class<V> value) {
		if (cache == null) {
			synchronized (this) {
				if (cache == null) {
					cache = JsonUtils.string2Map(content, key, value, LinkedHashMap.class);
				}
			}
		}
		return (LinkedHashMap<K, V>) cache;
	}

	@SuppressWarnings("unchecked")
	public <T> T getContent(TypeReference<T> tr) {
		if (cache == null) {
			synchronized (this) {
				if (cache == null) {
					cache = JsonUtils.string2GenericObject(content, tr);
				}
			}
		}
		return (T) cache;
	}

	public <T> T getContent(Class<T> clazz, T defaults) {
		T ret = getContent(clazz);
		if (ret == null) {
			return defaults;
		}
		return ret;
	}

	public <T> T getContent(TypeReference<T> tr, T defaults) {
		T ret = getContent(tr);
		if (ret == null) {
			return defaults;
		}
		return ret;
	}

	// Getter and Setter ...

	public SelectorType getType() {
		return type;
	}

	protected void setType(SelectorType type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	protected void setContent(String content) {
		this.content = content;
	}

}
