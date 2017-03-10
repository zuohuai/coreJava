package com.edu.java8.learning;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.edu.java8.learning.model.Apple;

public class AppleComparator implements Comparator<Apple>{

	@Override
	public int compare(Apple o1, Apple o2) {
		return new CompareToBuilder().append(o1.getWeight(), o2.getWeight()).toComparison();
	}

}
