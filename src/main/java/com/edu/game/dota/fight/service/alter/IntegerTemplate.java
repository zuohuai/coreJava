package com.eyu.snm.module.fight.service.alter;

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
		Integer t1 = Math.abs(n1);
		Integer t2 = Math.abs(n2);
		if (t1 > t2) {
			return n1;
		} else if (t1 < t2) {
			return n2;
		} else {
			if (n1 < 0) {
				return n2;
			} else {
				return n1;
			}
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
