package com.edu.design.command;

import org.junit.Test;

public class CommandTest {

	@Test
	public void test_command(){
		TV commandReceiver = new TV();
		
		Command cmdTurnOff = new CommandOff(commandReceiver);
		
		Command cmdTurnOn = new CommandOn(commandReceiver);
		CommandControl commandControl = new CommandControl(cmdTurnOn, cmdTurnOff);
		
		commandControl.turnOn();
		commandControl.turnOff();
	}
}
