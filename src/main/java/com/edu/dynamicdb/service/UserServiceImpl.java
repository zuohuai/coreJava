package com.edu.dynamicdb.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.edu.dynamicdb.dao.IUserDao;
import com.edu.dynamicdb.model.UserVo;
import com.edu.dynamicdb.proxy.DynamicDateSourceHolder;
import com.edu.utils.json.JsonUtils;

@Service
public class UserServiceImpl implements UserService{

	@Resource
	private IUserDao userDao;
	private static final String PREFIX = "dataSource-";
	
	private int num = 1024;
	
	public UserVo getUserById(int id){
		int num = id%this.num;
		DynamicDateSourceHolder.putDataSourceName(PREFIX + num);		
		UserVo userVo = userDao.getById(id);
		System.out.println(JsonUtils.object2String(userVo));
		DynamicDateSourceHolder.removeDataSoruceName();
		return userVo;
	}
	@Override
	public int insertUser() {
		UserVo userVo = UserVo.valueOf(2, "rose3", "rose3");
		int id = userDao.insertUser(userVo);
		//throw new RuntimeException("用来测试事务是否回滚");
		return id;
	}
	
	@Override
	public void deleteById(int id){
		userDao.deleteById(id);
	}

}
