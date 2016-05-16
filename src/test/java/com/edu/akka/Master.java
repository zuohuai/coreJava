package com.edu.akka;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class Master extends UntypedActor {

	private ActorRef worker;

	@Override
	public void onReceive(Object arg0) throws Exception {
		System.out.println("master 收到消息");
		init();
		worker.tell(arg0);
	}

	private void init() {
		if (worker == null) {
			worker = this.context().actorOf(new Props(Worker.class), "worker");
		}
	}
}
