package com.edu.design.deractor;

/**
 * 定义一个豆浆
 * @author Administrator
 *
 */
public class HouseBlend extends Beverage{

	@Override
	public String getDescription() {
		return "HouseBlend";
	}
	
	@Override
	public double cost() {
		return 0.99;
	}

}
