package com.edu.dynamicdb;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.edu.dynamicdb.dao.IUserDao;
import com.edu.dynamicdb.model.UserVo;
import com.edu.dynamicdb.proxy.DynamicDateSource;
import com.edu.dynamicdb.proxy.DynamicDateSourceHolder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class DynamicTest {

	@Autowired
	private DynamicDateSource dynamicDateSource;
	@Resource
	private IUserDao userDao;
	
	private static final String PREFIX = "dataSource-";
	
	private int num = 1024;
	
	@Test
	public void test_xml_parser() throws Exception{
		dynamicDateSource.test_xml_parser();
	}
	
	@Test
	public void test_sprint_ibatis() throws Exception{
		int userId = 1;
		getDatas(userId);
		
		userId = 2;
		getDatas(userId);
		
	}

	private void getDatas(int userId) {
		int num = userId%this.num;
		DynamicDateSourceHolder.putDataSourceName(PREFIX + num);
		UserVo userVo = userDao.getById(userId);
		System.out.println(userVo.getUserid() + "\t" + userVo.getUsername() + "\t" + userVo.getPassword());
		DynamicDateSourceHolder.removeDataSoruceName();
	}
}
