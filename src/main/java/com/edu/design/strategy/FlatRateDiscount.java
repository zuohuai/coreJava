package com.edu.design.strategy;

public class FlatRateDiscount extends DiscountStrategy{

	private int amount;
	
	public FlatRateDiscount(int amount, int price, int copies) {
		super(price, copies);
	}

	public int getAmount() {
		return amount;
	}
	
	@Override
	public int calculateDiscount() {
		return this.copies * this.amount;
	}

}
