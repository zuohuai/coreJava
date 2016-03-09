package com.edu.java8.lambda;

import java.time.LocalDate;
import java.util.Random;

public class Person {

	public enum Sex {
		MALE, FEMALE
	}

	private String name;
	private Sex gender;
	private int age;

	private LocalDate birthday;
	private String emailAddress;

	public Person(String name, Sex gender) {
		this.name = name;
		this.gender = gender;
		this.age = new Random().nextInt(26);
	}

	public String getName() {
		return name;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public Sex getGender() {
		return gender;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public int getAge() {
		return age;
	}

	public void printPerson() {
		System.out.println("name:" + this.name + ",gender:" + gender + ", age:" + age);
	}
}