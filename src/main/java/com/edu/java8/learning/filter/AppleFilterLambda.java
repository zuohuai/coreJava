package com.edu.java8.learning.filter;

import java.util.LinkedList;
import java.util.List;

import com.edu.java8.learning.ApplePredicateLambda;

public class AppleFilterLambda {

	public static <T> List<T> filter(List<T> list, ApplePredicateLambda<T> p) {
		List<T> resut = new LinkedList<>();
		for (T e : list) {
			if (p.test(e)) {
				resut.add(e);
			}
		}
		return resut;
	}
}
