package com.edu.thread.test;

public class LoggerWriter extends Widget{
	
	public synchronized void doSomething(){
			System.out.println("loggerWriter");
			super.doSomething();
	}
}
