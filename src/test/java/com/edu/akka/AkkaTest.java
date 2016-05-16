package com.edu.akka;

import org.junit.Test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;

/**
 * 基本概念:
 * 
 * @author Administrator
 *
 */
public class AkkaTest {

	/**
	 * Akka开始
	 * @throws Exception
	 */
	@Test
	public void test_start() throws Exception{
		ActorSystem actorSystem = ActorSystem.create("ActorSystem");
		
		ActorRef actorRef =  actorSystem.actorOf(new Props(Listener.class), "listener");
		
		 // create the master
	    ActorRef master = actorSystem.actorOf(new Props(new UntypedActorFactory() {
	      public UntypedActor create() {
	        return new Master();
	      }
	    }), "master");
	    
	    master.tell("Hello World");
	}
	
}
