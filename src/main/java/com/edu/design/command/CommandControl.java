package com.edu.design.command;

/**
 * 命令控制器
 * 
 * @author zuohuai
 *
 */
public class CommandControl {

	private Command cmdTurnOn;

	private Command cmdTurnOff;

	public CommandControl(Command cmdTurnOn, Command cmdTurnOff) {
		this.cmdTurnOn = cmdTurnOn;
		this.cmdTurnOff = cmdTurnOff;
	}

	public void turnOn() {
		cmdTurnOn.execute();
	}

	public void turnOff() {
		cmdTurnOff.execute();
	}
}
