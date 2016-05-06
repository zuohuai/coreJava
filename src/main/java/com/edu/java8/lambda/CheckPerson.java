package com.edu.java8.lambda;

@FunctionalInterface
interface CheckPerson {
	boolean test(Person p);

	// 默认方法
	default void defaultMethod() {
		System.out.println("default method test  !!!");
	}

	// 静态方法
	static void theStaticMethod() {
		System.out.println("static method test  !!!");
	}
}