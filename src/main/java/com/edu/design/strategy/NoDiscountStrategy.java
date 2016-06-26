package com.edu.design.strategy;

public class NoDiscountStrategy extends DiscountStrategy{

	public NoDiscountStrategy(int price, int copies) {
		super(price, copies);
	}

	@Override
	public int calculateDiscount() {
		return 0;
	}

}
