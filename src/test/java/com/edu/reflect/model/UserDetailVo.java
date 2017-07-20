package com.edu.reflect.model;

public class UserDetailVo {

	private int id;

	private int age;

	public static UserDetailVo valueOf(int id, int age) {
		UserDetailVo detailVo = new UserDetailVo();
		detailVo.id = id;
		detailVo.age = age;
		return detailVo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

}
