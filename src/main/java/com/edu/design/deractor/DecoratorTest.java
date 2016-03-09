package com.edu.design.deractor;

public class DecoratorTest {

	public static void main(String[] args){
		//定义一杯浓咖啡，不需要调料
		Beverage beverage = new Espresso();
		System.out.println(beverage.getDescription() + ", $" + beverage.cost());
		
		//定义一杯豆浆 + 摩卡
		Beverage beverage2 = new HouseBlend();
		beverage2 = new Mocha(beverage2);
		System.out.println(beverage2.getDescription() + ", $" + beverage2.cost());
		
		//
		Beverage beverage3 = new Mocha(beverage);
		System.out.println(beverage3.getDescription() + ", $" + beverage3.cost());
	}
}
