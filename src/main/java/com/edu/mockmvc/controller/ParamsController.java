package com.edu.mockmvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.edu.mockmvc.model.UserVo;


@Controller
@RequestMapping("/params")
public class ParamsController {

	public static final String USER_VO = "userVo";
	
	/**
	 * 
	 * valid:(这里用一句话描述这个方法的作用). <br/>   
	 *  验证UserVo, 并发结果放到如到BindingResult 中
	 * @author hison.zhang  
	 * @param request
	 * @param userVo
	 * @param bingingresult
	 * @return
	 */
	@RequestMapping(value = "valid_start", method = RequestMethod.GET)
	public ResponseEntity<String> valid_start(HttpServletRequest request, @Valid UserVo userVo, BindingResult bingingresult) {
		String msg = "SUCESS";
		if (bingingresult.hasErrors()) {
			FieldError error = bingingresult.getFieldError();
			msg = error.getDefaultMessage();
		}
		return new ResponseEntity<String>(msg, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "valid_param", method = RequestMethod.GET)
	public ResponseEntity<String> valid_param(HttpServletRequest request) {
		String msg = "SUCESS";
		System.out.println("Hello World");
		return new ResponseEntity<String>(request.getAttribute(USER_VO).getClass().toString(),  HttpStatus.OK);
	}
}
