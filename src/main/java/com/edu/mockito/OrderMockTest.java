package com.edu.mockito;

import org.springframework.stereotype.Component;

@Component
public class OrderMockTest {

	public String mockTest() {
		System.out.println("the method OrderMockTest.mockTest is execute !");
		return "mock-rest";
	}
}
