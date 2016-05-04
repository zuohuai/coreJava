package com.edu.game.jct.fight.service.core;

import java.util.HashMap;
import java.util.Map.Entry;

import com.edu.game.jct.fight.model.AlterType;
import com.edu.game.jct.fight.model.AlterValue;

/**
 * 上下文对象
 * @author administrator
 */
public class Context {

	/** 战斗单元的值变更 */
	private final HashMap<Unit, AlterValue> values;

	/** 构造方法 */
	public Context() {
		values = new HashMap<Unit, AlterValue>();
	}

	/** 构造方法 */
	public Context(int size) {
		values = new HashMap<Unit, AlterValue>(size);
	}

	/**
	 * 合并缓存修改值
	 * @param target
	 * @param value
	 */
	public void merge(Unit target, AlterValue value) {
		AlterValue current = loadOrCreateValue(target);
		current.merge(value);
	}

	/** 使上下文中的值变更全部生效 */
	public void effect() {
		for (Entry<Unit, AlterValue> entry : values.entrySet()) {
			Unit unit = entry.getKey();
			AlterValue value = entry.getValue();
			value.effect(unit);
		}
	}

	/**
	 * 使上下文中的指定战斗单元的值变更全部生效,并重置为空状态
	 */
	public AlterValue effect(Unit owner) {
		AlterValue value = values.remove(owner);
		if (value != null) {
			value.effect(owner);
		}
		return value;
	}

	/**
	 * 获取指定键的值，如果不存在则返回默认值
	 * @param <E>
	 * @param unit 单位
	 * @param type 值修改类型
	 * @param defaultValue 默认值
	 * @param clz 值类型
	 * @return
	 */
	public <E> E getValue(Unit unit, AlterType type, E defaultValue, Class<E> clz) {
		AlterValue value = values.get(unit);
		if (value == null) {
			return defaultValue;
		}
		E result = value.getValue(type, clz);
		if (result == null) {
			return defaultValue;
		}
		return result;
	}

	/**
	 * @param target
	 * @param hp
	 * @param value
	 */
	public void addValue(Unit target, AlterType type, Number value) {
		AlterValue current = loadOrCreateValue(target);
		current.addValue(type, value);
	}

	// 内部方法

	/**
	 * 加载或创建指定单位的临时值修改对象
	 * @param target
	 * @return
	 */
	private AlterValue loadOrCreateValue(Unit target) {
		AlterValue result = values.get(target);
		if (result == null) {
			result = new AlterValue();
			values.put(target, result);
		}
		return result;
	}
}
