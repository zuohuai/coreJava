package com.edu.design.builder;

/**
 * 其实这里用到的模板模式, buildPart1, buildPart2, retrieveResult 是顶级的模板类
 * 子类需要根据该模板来生成方法
 * @author administrat
 *
 */
public abstract class Builder {
	 
	public void construct(){
		//构建商品的part1 和 part2
		buildPart1();
		buildPart1();
	}
	protected abstract void buildPart1();
	
	protected abstract void buildPart2();
	
	public  abstract Product retrieveResult(); 
}
