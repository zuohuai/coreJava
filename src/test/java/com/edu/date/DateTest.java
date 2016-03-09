package com.edu.date;

import java.util.Date;

import org.junit.Test;


public class DateTest {

	@Test
	public void test_date_convert(){
		Date current = new Date(1451945572000L);
		System.out.println(current);
	}
}
