package com.edu.dynamicdb.model;

public class UserVo {
	/**用户id*/
	private int userid;
	/**用户名*/
	private String username;
	/**密码*/
	private String password;
	
	public static UserVo valueOf(int userid, String username, String password){
		UserVo userVo = new UserVo();
		userVo.userid = userid;
		userVo.username = username;
		userVo.password = password;
		return userVo;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	
}
