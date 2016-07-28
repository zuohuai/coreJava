package com.edu.dynamicdb;

import javax.management.RuntimeErrorException;

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
	public void test_xml_parser() throws Exception {
		dynamicDateSource.test_xml_parser();
	}

	/**
	 * 测试动态分库
	 * @throws Exception
	 */
	@Test
	public void test_sprint_ibatis() throws Exception {
		userService.getUserById(1);
	}

	/**
	 * 测试事务回滚
	 * @throws Exception
	 */
	@Test
	public void test_insert_user() throws Exception {
		long start = System.currentTimeMillis();
		userService.insertUser();
		long end = System.currentTimeMillis();
		System.out.println("单条-插入5000条的时间是:" + (end-start));
	}

	/**
	 * 
	 * test_batch_insert:测试mysql的批量插入 <br/>
	 * 
	 * @author hison.zhang
	 */
	@Test
	public void test_batch_insert() throws Exception {
		long start = System.currentTimeMillis();
		userService.insertUserByBatch();
		long end = System.currentTimeMillis();
		System.out.println("批量-插入5000条的时间是:" + (end-start));
		// throw new RuntimeException("批量插入异常"); //事务回滚有问题
	}

	/**
	 * 
	 * test_delete:删除测试<br/>
	 * 
	 * @author hison.zhang
	 * @throws Exception
	 */
	@Test
	public void test_delete() throws Exception {
		int id = 2;
		userService.deleteById(id);
	}
	
	/**
	 * 
	 * test_batch_delete:测试批量删除的性能<br/>  
	 *  
	 * @author hison.zhang  
	 * @throws Exception
	 */
	@Test
	public void test_batch_delete() throws Exception{
		long start = System.currentTimeMillis();
		userService.deleteUserByBatch();
		long end = System.currentTimeMillis();
		System.out.println("批量删除5000条的时间是:" + (end-start));
		
	}
}
