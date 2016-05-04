package com.edu.game.jct.fight.service.alter;

public abstract class IntegerTemplate implements Alter<Integer, Integer> {

	@Override
	public Integer merge(Integer current, Integer value) {
		if (current == null) {
			return value;
		} else {
			return current + value;
		}
	}
	
	@Override
	public Integer add(Integer current, Integer value) {
		if (current == null) {
			return value;
		} else {
			return current + value;
		}
	}

	@Override
	public Integer getAbsMax(Integer n1, Integer n2) {
		if (Math.abs(n1) > Math.abs(n2)) {
			return n1;
		} else {
			return n2;
		}
	}
	
	@Override
	public Integer getReverse(Integer n) {
		return -n;
	}
	
	@Override
	public Integer toValue(String value) {
		return Integer.valueOf(value);
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
		return value * multiple;
	}
	
	@Override
	public Integer multiply(Integer value, double multiple) {
		return (int) (value * multiple);
	}
}
