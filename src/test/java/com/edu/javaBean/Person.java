package com.edu.javaBean;

import java.util.Date;

public class Person {
	/** ID */
	private int id;
	/** 名称 */
	private String name;
	/** 积分 */
	private int score;
	/** 保存时间 */
	private Date current;

	Person() {

	}

	public static Person valueOf(int id, String name, int score) {
		Person person = new Person();
		person.id = id;
		person.name = name;
		person.score = score;
		person.current = new Date();
		return person;
	}

	public Person(int id, String name) {
		this.id = id;
		this.name = name;
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

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Date getCurrent() {
		return current;
	}

	public void setCurrent(Date current) {
		this.current = current;
	}

}
