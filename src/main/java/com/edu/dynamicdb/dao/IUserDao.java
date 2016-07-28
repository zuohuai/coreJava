package com.edu.dynamicdb.dao;

import java.util.List;

import com.edu.dynamicdb.model.UserVo;


public interface IUserDao {
	/** 根据id来查询 */
	public UserVo getById(int id);

	/** 保存用户信息 */
	public int insertUser(UserVo userVo);

	/** 通过id删除User */
	public void deleteById(int id);
	
	/**批量插入*/
	public void insertUserByBatch(List<UserVo> userVos);
	
	/**批量删除*/
	public void deleteUserByBatch(List<UserVo> userVos);
}
