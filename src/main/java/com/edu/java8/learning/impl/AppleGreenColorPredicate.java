package com.edu.java8.learning.impl;

import com.edu.java8.learning.ApplePredicate;
import com.edu.java8.learning.model.Apple;

public class AppleGreenColorPredicate implements ApplePredicate{

	@Override
	public boolean test(Apple apple) {
		return "grean".equals(apple.getColor());
	}

}
