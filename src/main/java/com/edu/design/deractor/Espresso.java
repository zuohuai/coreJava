package com.edu.design.deractor;

/**
 * 定义一个浓咖啡
 * @author Administrator
 *
 */
public class Espresso extends Beverage{

	@Override
	public String getDescription() {
		return "Espresso";
	}
	
	@Override
	public double cost() {
		return 1.99;
	}

}
