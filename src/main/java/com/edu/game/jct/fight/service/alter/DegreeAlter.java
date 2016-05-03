package com.edu.game.jct.fight.service.alter;

import com.edu.game.jct.fight.model.UnitDegree;
import com.edu.game.jct.fight.service.core.Unit;

public class DegreeAlter implements Alter<Double, Double> {

	private UnitDegree degree;

	public DegreeAlter(UnitDegree degree) {
		this.degree = degree;
	}

	@Override
	public void execute(Unit unit, Double value) {
		unit.increaseDegree(degree, value);
	}

	@Override
	public Double merge(Double current, Double value) {
		if (current == null) {
			return value;
		} else {
			return current * value;
		}
	}

	@Override
	public Double add(Double current, Double value) {
		if (current == null) {
			return value;
		} else {
			if (current < 0) {
				current = -(1 / current);
			}
			if (value < 0) {
				value = -(1 / value);
			}
			return current * value;
		}
	}

	@Override
	public Double getAbsMax(Double n1, Double n2) {
		if (Math.abs(n1) > Math.abs(n2)) {
			return n1;
		} else {
			return n2;
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
