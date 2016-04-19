package com.edu.thread.clone;

public class Animal implements Cloneable {

	private int id;

	private Sex sex;

	public static Animal valueOf(int id, Sex sex) {
		Animal animal = new Animal();
		animal.id = id;
		animal.sex = sex;
		return animal;
	}

	public int getId() {
		return id;
	}

	public Sex getSex() {
		return sex;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
}
