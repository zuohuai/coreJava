package com.edu.java8.stream;

public class Dish {

	private String name;
	private int calories;
	private String type;
	private boolean vegetarian;

	public Dish(String name, int calories, String type, boolean vegetarian) {
		this.name = name;
		this.calories = calories;
		this.type = type;
		this.vegetarian = vegetarian;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCalories() {
		return calories;
	}

	public void setCalories(int calories) {
		this.calories = calories;
	}

}
