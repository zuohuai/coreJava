package com.edu.java8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.junit.Test;

/**
 * java.util.function.Function<T, R>接口定义了一个叫作apply的方法，
 * 它接受一个泛型T的对象， 并返回一个泛型R的对象。
 * 
 * @author zuohuai
 *
 */
public class FunctionTest {

	public static <T, R> List<R> mapFunctionTest(List<T> list, Function<T, R> f) {
		List<R> result = new ArrayList<>();
		for (T s : list) {
			result.add(f.apply(s));
		}
		return result;
	}

	@Test
	public void test_function() throws Exception {
		List<Integer> l = mapFunctionTest(Arrays.asList("lambdas", "in", "action"), (String s) -> s.length());
		System.out.println(l);
	}
}
