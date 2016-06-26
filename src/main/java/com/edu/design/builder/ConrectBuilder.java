package com.edu.design.builder;

public class ConrectBuilder extends Builder {

	@Override
	protected void buildPart1() {
		System.out.println("构建商品的part1");
	}

	@Override
	protected void buildPart2() {
		System.out.println("构建商品的part2");
	}

	@Override
	public Product retrieveResult() {
		return new Product();
	}

}
