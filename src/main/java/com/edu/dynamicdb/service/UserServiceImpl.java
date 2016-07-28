package com.edu.dynamicdb.service;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.edu.dynamicdb.dao.IUserDao;
import com.edu.dynamicdb.model.UserVo;
import com.edu.dynamicdb.proxy.DynamicDateSourceHolder;
import com.edu.utils.json.JsonUtils;

@Service
public class UserServiceImpl implements UserService {

	@Resource
	private IUserDao userDao;
	private static final String PREFIX = "dataSource-";

	private int num = 1024;

	public UserVo getUserById(int id) {
		int num = id % this.num;
		DynamicDateSourceHolder.putDataSourceName(PREFIX + num);
		UserVo userVo = userDao.getById(id);
		System.out.println(JsonUtils.object2String(userVo));
		DynamicDateSourceHolder.removeDataSoruceName();
		return userVo;
	}

	@Override
	public int insertUser() {
		List<UserVo> userVos = buildUserVos();
		for(UserVo userVo : userVos){
			userDao.insertUser(userVo);
		}
		
		// throw new RuntimeException("用来测试事务是否回滚");
		return -1;
	}

	@Override
	public void deleteById(int id) {
		userDao.deleteById(id);
	}

	@Override
	public void insertUserByBatch() {
		List<UserVo> userVos = buildUserVos();
		userDao.insertUserByBatch(userVos);
	}
	
	@Override
	public void deleteUserByBatch() {
		List<UserVo> userVos =  buildUserVos();
		userDao.deleteUserByBatch(userVos);
	}
	

	private List<UserVo> buildUserVos() {
		List<UserVo> userVos = new LinkedList<>();
		for (int i = 3; i < 5002; i++) {
			userVos.add(UserVo.valueOf(i, "batch" + i, "batch" + i));
		}
		return userVos;
	}



	
}
