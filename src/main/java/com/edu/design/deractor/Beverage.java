package com.edu.design.deractor;

/**
 * 定义一个饮料的抽象类
 * @author Administrator
 */
public abstract class Beverage {
	
	public String getDescription() {
		return "Unknow Beverage";
	}

	public abstract double cost();
}
