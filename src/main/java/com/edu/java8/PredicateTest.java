package com.edu.java8;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import org.junit.Test;

/**
 * java.util.function.Predicate<T>接口定义了一个名叫test的抽象方法，它接受泛型
 * T对象，并返回一个boolean。这恰恰和你先前创建的一样，现在就可以直接使用了
 * @author zuohuai
 *
 */
public class PredicateTest {

	public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
		List<T> results = new LinkedList<T>();
		for (T s : list) {
			if (predicate.test(s)) {
				results.add(s);
			}
		}
		return results;
	}

	@Test
	public void test_predicate() throws Exception{
		
		List<String> stringOfLists = new LinkedList<>();
		stringOfLists.add("Hello World");
		stringOfLists.add("1");
		stringOfLists.add("22");
		stringOfLists.add("223");
		
		Predicate<String> stringPredicate = (String s) -> s.length() > 2;
		List<String> results = filter(stringOfLists, stringPredicate);
		System.out.println(results);
	}	
}
