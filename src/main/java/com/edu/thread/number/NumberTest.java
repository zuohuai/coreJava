package com.edu.thread.number;

import org.junit.Test;

public class NumberTest {

	@Test
	public void test_number() {
		long owner = 12345567;
		Long value1 = new Long(12345567);
		Long value2 = new Long(12345567);
		
		System.out.println(owner == value1);
		System.out.println(value2 == value1);
	}
}
