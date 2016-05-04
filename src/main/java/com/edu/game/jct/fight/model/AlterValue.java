package com.edu.game.jct.fight.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.edu.game.jct.fight.service.alter.Alter;
import com.edu.game.jct.fight.service.core.Unit;


/**
 * 战斗单元的数值变更对象
 * @author Frank
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AlterValue {

	private Map<String, Object> content = new HashMap<String, Object>();

	/** 获取指定值 */
	public <T> T getValue(AlterType type, Class<T> clz) {
		String key = type.getKey();
		return (T) content.get(key);
	}

	/** 添加值 */
	public void addValue(AlterType type, Object value) {
		Alter alter = type.getAlter();
		String key = type.getKey();
		value = alter.add(content.get(key), (Number) value);
		content.put(key, value);
	}

	/** 检查是否存在指定的数值变更 */
	public boolean contain(AlterType type) {
		String key = type.getKey();
		return content.containsKey(key);
	}

	/** 合并数值变更 */
	public void merge(AlterValue value) {
		for (Entry<String, Object> entry : value.content.entrySet()) {
			AlterType type = AlterType.getAlterType(entry.getKey());
			Alter alter = type.getAlter();
			String key = entry.getKey();
			Object current = content.get(key);
			current = alter.merge(current, entry.getValue());
			content.put(key, current);
		}
	}

	/** 将数值变更作用到战斗单元 */
	public void effect(Unit unit) {
		List<AlterType> types = new ArrayList<AlterType>();
		for (String key : content.keySet()) {
			types.add(AlterType.getAlterType(key));
		}
		Collections.sort(types);
		for (AlterType type : types) {
			Object value = content.get(type.getKey());
			Alter alter = type.getAlter();
			alter.execute(unit, value);
		}
	}

	// Getter and Setter ...
	
	public Map<String, Object> getContent() {
		return content;
	}

	@Override
	public String toString() {
		return content.toString();
	}
}
