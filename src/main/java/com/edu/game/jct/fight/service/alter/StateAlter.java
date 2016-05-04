package com.edu.game.jct.fight.service.alter;

import java.util.HashSet;
import java.util.Set;

import com.edu.game.jct.fight.service.core.Unit;

/**
 * 状态修改器
 * @author Administrator
 *
 */
public class StateAlter implements Alter<Set<Integer>, Integer> {

	@Override
	public void execute(Unit unit, Set<Integer> value) {
		Set<Integer> values = (Set<Integer>) value;
		for (int v : values) {
			if (v > 0) {
				unit.addState(Math.abs(v));
			} else {
				unit.removeState(Math.abs(v));
			}
		}
	}

	@Override
	public Set<Integer> merge(Set<Integer> current, Set<Integer> values) {
		if (current == null || current.isEmpty()) {
			return values;
		}
		if (values != null && !values.isEmpty()) {
			for (Integer v : values) {
				if (!current.contains(-v)) {
					current.add(v);
				} else {
					current.remove(-v);
				}
			}
		}
		return current;
	}

	@Override
	public Set<Integer> add(Set<Integer> current, Integer value) {
		if (current == null) {
			current = new HashSet<Integer>(1);
		}
		if (!current.contains(-value)) {
			current.add(value);
		} else {
			current.remove(-value);
		}
		return current;
	}

	@Override
	public Integer getAbsMax(Integer n1, Integer n2) {
		throw new IllegalStateException("状态值修改类型不支持获取最大值方法");
	}

	@Override
	public Integer getReverse(Integer n) {
		return -n;
	}

	@Override
	public Integer toValue(String value) {
		return new Integer(value);
	}
	
	@Override
	public Integer toValue(Integer value) {
		return value;
	}
	
	@Override
	public String toString(Integer value) {
		return value.toString();
	}

	@Override
	public Integer multiply(Integer value, int multiple) {
		return value;
	}
	
	@Override
	public Integer multiply(Integer value, double multiple) {
		return value;
	}

}
