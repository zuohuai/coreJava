package com.edu.design.strategy;

public class PercentDiscountStrategy extends DiscountStrategy{

	private double percent = 0.0;
	
	public PercentDiscountStrategy(double percent, int price, int copies) {
		super(price, copies);
		this.percent = percent;
	}

	public double getPercent() {
		return percent;
	}
	
	@Override
	public int calculateDiscount() {
		return (int)(copies * price * percent);
	}

}
