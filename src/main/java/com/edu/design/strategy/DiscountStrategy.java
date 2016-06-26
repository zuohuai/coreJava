package com.edu.design.strategy;

/**
 * 折扣的类
 * @author administrat
 *
 */
public abstract class DiscountStrategy {

	protected int price = 0;
	
	protected int copies= 0;
	
	public abstract int calculateDiscount();
	
	public DiscountStrategy(int price, int copies){
		this.price = price;
		this.copies = copies;
	}
}
