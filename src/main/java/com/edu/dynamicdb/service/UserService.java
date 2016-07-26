package com.edu.dynamicdb.service;

import com.edu.dynamicdb.model.UserVo;

public interface UserService {
	/**通过id获取UserVo*/
	public UserVo getUserById(int id);
	/**插入UserVo*/
	public int insertUser();
	/**通过id删除User*/
	public void deleteById(int id);
}
