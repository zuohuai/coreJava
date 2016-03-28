package com.edu.date;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.edu.utils.time.DateUtils;

public class DateTest {

	@Test
	public void test_date_convert() {
		Date current = new Date(1451945572000L);
		System.out.println(current);
	}

	@Test
	public void test_date_add() throws Exception{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		
		Date startDate = DateUtils.addDays(DateUtils.getFirstTime(calendar.getTime()), 28);
		
		Date endDate = DateUtils.addDays(startDate, 11);
		
		System.out.println(DateUtils.date2String(endDate, DateUtils.PATTERN_DATE_TIME));
		
		
	}
}
