package com.edu.mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * mock 测试准备类
 * 
 * @author tiger
 * @version 1.0.0 createTime: 14/12/27 下午8:57
 * @see com.practice.mock.OrderHelper
 * @since 1.6
 */
@Component
public class OrderCreate {

	@Autowired
	private OrderHelper orderHelper;

	@Autowired
	private OrderSpyTest orderSpyTest;

	@Autowired
	private OrderMockTest orderMockTest;

	public void create() {
		System.out.println(getAmt());
		System.out.println(orderHelper.resolve());
	}

	public int getAmt() {
		System.out.println("the method OrderCreate.getAmt is execute !");
		return 10;
	}

	public void testSpy() {
		System.out.println(orderSpyTest.spyTest());
	}

	public void testMock() {
		System.out.println(orderMockTest.mockTest());
	}
}