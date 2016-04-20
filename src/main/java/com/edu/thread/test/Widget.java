package com.edu.thread.test;

public class Widget {
	private int a = 0 ;
	public synchronized void doSomething(){
		System.out.println("Widget");
		a++;
	}
	public int getA() {
		return a;
	}
	public void setA(int a) {
		this.a = a;
	}
	
}
