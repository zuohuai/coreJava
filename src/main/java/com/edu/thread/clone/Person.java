package com.edu.thread.clone;

public class Person implements Cloneable {
	private String name;
	private int age;
	private Sex sex;
	private Message message;

	public static Person valueOf(String name, int age, Sex sex, Message message) {
		Person person = new Person();
		person.name = name;
		person.age = age;
		person.message = message;
		person.sex = sex;
		return person;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	public Message getMessage() {
		return message;
	}

	public Sex getSex() {
		return sex;
	}
}
