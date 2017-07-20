package com.edu.mockito;

import org.springframework.stereotype.Component;

@Component
public class OrderSpyTest {

	public String spyTest(){
		System.out.println("the method OrderSpyTest.spyTest is execute !");
		return "spy-rest";
	}
}
