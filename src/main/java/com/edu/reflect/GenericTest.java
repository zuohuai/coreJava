package com.edu.reflect;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class GenericTest {

	@Test
	public void test_same_class() throws Exception {

		List<Integer> interClass = new LinkedList<>();
		System.out.println(interClass.getClass());
		List<Double> doubleClass = new LinkedList<>();
		System.out.println(doubleClass.getClass());
		
		if(interClass.getClass() == doubleClass.getClass()){
			System.out.println("same!!!");
		}
	}
	
	@Test
	public void test_int_integer_class() throws Exception{
		System.out.println(int.class);
		System.out.println(Integer.class.getName());
		
		int a = 1;
		int b = 2 ;
		//System.out.println(a instanceof Integer);
		//System.out.println(a instanceof int.class);
		System.out.println(int.class.isInstance(a));
	}
	
	
}
