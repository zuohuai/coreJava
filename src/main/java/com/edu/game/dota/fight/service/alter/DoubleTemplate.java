package com.eyu.snm.module.fight.service.alter;

public abstract class DoubleTemplate implements Alter<Double, Double> {

	@Override
	public Double merge(Double current, Double value) {
		if (current == null) {
			return value;
		} else {
			return current + value;
		}
	}

	@Override
	public Double add(Double current, Double value) {
		if (current == null) {
			return value;
		} else {
			return current + value;
		}
	}

	@Override
	public Double getAbsMax(Double n1, Double n2) {
		Double t1 = Math.abs(n1);
		Double t2 = Math.abs(n2);
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
	public Double getReverse(Double n) {
		return -n;
	}

	@Override
	public Double toValue(String value) {
		return Double.valueOf(value);
	}

	@Override
	public String toString(Double value) {
		return value.toString();
	}

	@Override
	public Double multiply(Double value, int multiple) {
		return value * multiple;
	}

	@Override
	public Double multiply(Double value, double multiple) {
		return value * multiple;
	}
}
