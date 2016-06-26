package com.edu.design.builder;

import org.junit.Test;

public class BuilderTest {

	@Test
	public void test_builder() throws Exception{
		Builder builder = new ConrectBuilder();
		
		//构建产品
		builder.construct();
		Product product = builder.retrieveResult();
		product.print();
		
	}
}
