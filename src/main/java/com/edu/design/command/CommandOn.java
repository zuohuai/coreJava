package com.edu.design.command;

public class CommandOn implements Command{
	/** 命令执行者*/
	private TV tv;
	
	public CommandOn(TV tv){
		this.tv = tv;
	}
	
	@Override
	public void execute() {
		System.out.println("执行开启命令");
		tv.turnOn();
	}

}
