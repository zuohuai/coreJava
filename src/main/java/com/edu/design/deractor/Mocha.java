package com.edu.design.deractor;

/**
 * 定义一个摩卡
 * @author Administrator
 */
public class Mocha extends CondimentDecorator {
	/** 定义一个饮料 */
	private Beverage beverage;

	public Mocha(Beverage beverage) {
		this.beverage = beverage;
	}

	@Override
	public String getDescription() {
		return beverage.getDescription() + ", Mocha";
	}

	@Override
	public double cost() {
		return 0.2 + beverage.cost();
	}

}
