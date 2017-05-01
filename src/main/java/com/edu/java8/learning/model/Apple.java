package com.edu.java8.learning.model;

public class Apple {
	/** 颜色 */
	private String color;
	/** 重量 */
	private int weight;

	public Apple() {

	}

	public Apple(Integer  weight) {
		this.weight = weight;
	}
	
	public Apple(String color, int weight) {
		super();
		this.color = color;
		this.weight = weight;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
}
