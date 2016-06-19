package com.edu.design.singleton;

public class Singleton {

	public static void main(String[] args){
		
	}
	
	
	private static class SingletonHolder{
		private static final Singleton SINGLETON = new Singleton();
	}
	
	/**
	 * 定义一个内部类，用jvm 来保证线程安全，不用同步代码，也可以保证是线程安全的
	 * 这种写法都优雅 TODO 
	 * @return
	 */
	public static Singleton getInstance(){
		return SingletonHolder.SINGLETON;
	}
}
