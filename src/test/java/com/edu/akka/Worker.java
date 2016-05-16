package com.edu.akka;

import akka.actor.UntypedActor;

public class Worker extends UntypedActor{

	@Override
	public void onReceive(Object arg0) throws Exception {
		System.out.println("worker 收到消息 ");
	}

}
