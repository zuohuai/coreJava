package com.edu.akka;

import org.junit.Test;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;

/**
 * 基本概念:
 * @author Administrator
 */
public class AkkaTest {

	/**
	 * Akka开始
	 * @throws Exception
	 */
	@Test
	public void test_start() throws Exception {
		ActorSystem actorSystem = ActorSystem.create("ActorSystem");
		ActorSelection actorSelection = actorSystem.actorSelection("/user");
		System.out.println(actorSelection.target().path().name());

		ActorRef actorRef = actorSystem.actorOf(new Props(Listener.class), "listener");

		// create the master
		ActorRef master = actorSystem.actorOf(new Props(new UntypedActorFactory() {
			public UntypedActor create() {
				return new Master();
			}
		}), "master");

		// Actor的路径
		String masterPath = actorRef.path().name();
		System.out.println("master path: " + masterPath);

		master.tell("Hello World");
	}

	@Test
	public void test_actorOf() throws Exception {

	}
}
