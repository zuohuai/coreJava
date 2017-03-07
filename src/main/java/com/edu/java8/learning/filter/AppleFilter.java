package com.edu.java8.learning.filter;

import java.util.LinkedList;
import java.util.List;

import com.edu.java8.learning.ApplePredicate;
import com.edu.java8.learning.model.Apple;

public class AppleFilter {

	//策略代码 被包裹在类中， 需要需要实现不同的策略，就需要重新创建不同的类，Lambda 表达式的作用在可以可以把表达式传递给方法，无需定义多个类
	public static List<Apple> filterApple(List<Apple> inventory, ApplePredicate p) {
		List<Apple> result = new LinkedList<>();
		for (Apple apple : inventory) {
			if (p.test(apple)) {
				result.add(apple);
			}
		}
		return result;
	}
}
