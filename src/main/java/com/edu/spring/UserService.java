package com.edu.spring;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	@Autowired
	private UserManager userManager;
	
	@PostConstruct
	private void init(){
		userManager.test_manger();
	}
	
	public void test_service(){
		System.out.println("I am  service !!");
	}
}
