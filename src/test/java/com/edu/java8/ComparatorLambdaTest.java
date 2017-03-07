package com.edu.java8;

import java.util.Comparator;

import org.junit.Test;

import com.edu.java8.learning.model.Apple;

public class ComparatorLambdaTest {

	@Test
	public void test_comparator() throws Exception {

		Comparator<Apple> comparator = new Comparator<Apple>() {
			@Override
			public int compare(Apple o1, Apple o2) {
				return o1.getWeight() - o2.getWeight();
			}
		};
		
		//使用了lambda表达式之后
		Comparator<Apple> lambdaComparator = (Apple o1, Apple o2) -> o1.getWeight() - o2.getWeight(); 
	}
}
