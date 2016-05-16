package com.edu.akka;

import akka.actor.UntypedActor;

public class Listener extends UntypedActor {

	@Override
	public void onReceive(Object arg0) throws Exception {
		System.out.println("Listener 收到消息");
	}

}
