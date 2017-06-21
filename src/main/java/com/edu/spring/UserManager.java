package com.edu.spring;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserManager {
	@Autowired
	private UserService userService;
	
	@PostConstruct
	private void init(){
		userService.test_service();
	}
	
	public void test_manger(){
		System.out.println("Hello , i am manager");
	}
}
