package com.edu.mockmvc;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.edu.mockmvc.controller.ParamsController;
import com.edu.mockmvc.model.UserVo;
import com.edu.utils.json.JsonUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration
public class ParamsControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private ParamsController paramsController;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(paramsController).build();
	}

	@Test
	public void valid_start() throws Exception {
		// 发送请求
		ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.get("/params/valid_start")
				.param("userName", "hison.zhang")
				.param("password", ""));
		MvcResult mvcResult = resultActions.andReturn();
		String result = mvcResult.getResponse().getContentAsString();
		System.out.println("=====客户端获得反馈数据:" + result);
	}

	@Test
	public void valid_param() throws Exception {
		UserVo userVo = new UserVo();
		userVo.setUserName("hison.zhang");
		userVo.setPassword("password");
		//发送请求  
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.get("/params/valid_param") 
        		.requestAttr(ParamsController.USER_VO, userVo));  
        MvcResult mvcResult = resultActions.andReturn();  
        String result = mvcResult.getResponse().getContentAsString();  
        System.out.println("=====客户端获得反馈数据:" + result);  
	}
	
	@Test
	public void test_say_hello() {
		System.out.println("Hello World");
	}

}
