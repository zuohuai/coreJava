package com.edu.design.strategy;

import org.junit.Test;

/**
 * 
 * 当所有类均有相同的行为，区别在在于这种行为的实现方式可能不一样，
 * 则可以考虑使用策略模式
 * @author administrat
 *
 */
public class StrategyTest {

	@Test
	public void test_strategy() throws Exception{
		//由客户端自己去选择需要的策略
		DiscountStrategy discountStrategy = new NoDiscountStrategy(100, 10);
		discountStrategy.calculateDiscount();
		
		discountStrategy = new FlatRateDiscount(100, 100, 10);
		discountStrategy.calculateDiscount();
		
		discountStrategy = new PercentDiscountStrategy(1.2, 100, 10);
		discountStrategy.calculateDiscount();
	}
}
