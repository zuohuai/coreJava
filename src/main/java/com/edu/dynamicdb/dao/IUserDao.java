package com.edu.dynamicdb.dao;

import com.edu.dynamicdb.model.UserVo;

public interface IUserDao {
	/**根据id来查询*/
	 public UserVo getById(int id);  
}
