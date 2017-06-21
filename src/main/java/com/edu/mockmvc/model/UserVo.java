package com.edu.mockmvc.model;

import org.hibernate.validator.constraints.NotBlank;

public class UserVo {
	@NotBlank(message = "username not null")
	private String userName;
	@NotBlank(message = "password not null")
	private String password;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
