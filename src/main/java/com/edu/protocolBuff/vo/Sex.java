package com.edu.protocolBuff.vo;

public enum Sex {
	MALE(1), 
	FEMALE(2);

	private int value;

	private Sex(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}
