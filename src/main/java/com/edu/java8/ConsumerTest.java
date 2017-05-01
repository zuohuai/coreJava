package com.edu.java8;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * java.util.function.Consumer<T>定义了一个名叫accept的抽象方法，它接受泛型T
 * 的对象，没有返回（void）。你如果需要访问类型T的对象，并对其执行某些操作，就可以使用这个接口。
 * @author zuohuai
 *
 */
public class ConsumerTest {
	public static <T> void forEachTest(List<T> list, Consumer<T> c) {
		for (T i : list) {
			c.accept(i);
		}
	}
	
	public void test_consumer() throws Exception{
		List<Integer> consumerList = Arrays.asList(1,2,4);
		forEachTest(consumerList, (Integer value) -> System.out.println(value));
		
	}
}
