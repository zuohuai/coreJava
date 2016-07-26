package com.edu.dynamicdb;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.edu.dynamicdb.proxy.DynamicDateSource;
import com.edu.dynamicdb.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class DynamicTest {

	@Autowired
	private DynamicDateSource dynamicDateSource;
	@Autowired
	private UserService userService;

	@Test
	public void test_xml_parser() throws Exception{
		dynamicDateSource.test_xml_parser();
	}
	
	
	/**
	 * 测试动态分库
	 * @throws Exception
	 */
	@Test
	public void test_sprint_ibatis() throws Exception{
		userService.getUserById(1);
	}
	
	/**
	 * 测试事务回滚
	 * @throws Exception
	 */
	@Test
	public void test_transation_roll_back() throws Exception{
		userService.insertUser();
	}

	
	@Test
	public void test_delete() throws Exception{
		int id = 2;
		userService.deleteById(id);
	}
}
