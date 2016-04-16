package com.edu.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

public class LogInController {

	@ResponseBody
	@RequestMapping(value="",method = {RequestMethod.GET})
	public void testLogIn(@RequestParam  String data){
		
	}
}
