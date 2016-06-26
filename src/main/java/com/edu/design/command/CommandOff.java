package com.edu.design.command;

public class CommandOff implements Command{
	/**命令接收者*/
	private TV tv;
	
	public CommandOff(TV tv){
		this.tv = tv;
	}
	
	@Override
	public void execute() {
		System.out.println("执行关闭命令");
		tv.turnOff();
	}

}
