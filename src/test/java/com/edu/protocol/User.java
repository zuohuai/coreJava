package com.edu.protocol;

public class User {
	private int id;
	private String name;

	public static User valueOf(int id, String name) {
		User user = new User();
		user.id = id;
		user.name = name;
		return user;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
