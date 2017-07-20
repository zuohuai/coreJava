package com.edu.mockito;

import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class MockitoTest extends MockBaseConfig {

	@InjectMocks
	private OrderCreate orderCreate = Mockito.mock(OrderCreate.class);

	// spy对象的方法默认调用真实的逻辑，mock对象的方法默认什么都不做
	@Mock
	private OrderHelper orderHelper;

	@Spy
	private OrderSpyTest orderSpyTest;

	@Mock
	private OrderMockTest orderMockTest;

	@Before
	public void initMocks() throws Exception {
		MockitoAnnotations.initMocks(this);
		doReturn(11000).when(orderCreate).getAmt();
		doReturn("success mock").when(orderHelper).resolve();

		/**
		 * 使用@Mock生成的类，所有方法都不是真实的方法，而且返回值都是NULL。 使用@Spy生成的类，所有方法都是真实方法，返回值都是和真实方法一样的。
		 * 所以，你用when去设置模拟返回值时，它里面的方法（dao.getOrder()）会先执行一次。 使用doReturn去设置的话，就不会产生上面的问题，因为有when来进行控制要模拟的方法，所以不会执行原来的方法
		 */
		doReturn("nice to meetyou , spy !").when(orderSpyTest).spyTest();
		// Mockito.when(orderSpyTest.spyTest()).thenReturn("nice to meetyou , spy");
		Mockito.when(orderMockTest.mockTest()).thenReturn("nice to meetyou , mock !");

		doCallRealMethod().when(orderCreate).create();
		doCallRealMethod().when(orderCreate).testSpy();
		doCallRealMethod().when(orderCreate).testMock();
		;

	}

	@Test
	public void testCreate() {
		orderCreate.create();
	}

	@Test
	public void testSpyAndMock() {
		orderCreate.testMock();
		orderCreate.testSpy();
	}

	@Test
	public void testMock() {
		List mockList = Mockito.mock(List.class);
		mockList.add(1);
		System.out.println(mockList.size());
	}

	@Test
	public void testSpy() {
		List spyList = Mockito.spy(LinkedList.class);
		spyList.add(1);

		Mockito.when(spyList.size()).thenReturn(100);
		System.out.println(spyList.get(0));
		System.out.println(spyList.size());
	}

	@Test
	public void testResetingMock() {
		List mockList = Mockito.mock(List.class);
		Mockito.when(mockList.size()).thenReturn(10);
		mockList.add(1);
		System.out.println(mockList.size());

		// 重置之后的值
		Mockito.reset(mockList);
		mockList.add(100);
		System.out.println(mockList.size());
	}
}
