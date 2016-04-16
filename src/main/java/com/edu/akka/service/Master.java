package com.edu.akka.service;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinRouter;

public  class Master extends UntypedActor {
  private final int nrOfMessages;
  private final int nrOfElements;
 
  private double pi;
  private int nrOfResults;
  private final long start = System.currentTimeMillis();
 
  private final ActorRef listener;
  private final ActorRef workerRouter;
 
  public Master(final int nrOfWorkers, int nrOfMessages, int nrOfElements, ActorRef listener) {
    this.nrOfMessages = nrOfMessages;
    this.nrOfElements = nrOfElements;
    this.listener = listener;
 
    workerRouter = this.getContext().actorOf(new Props(Worker.class).withRouter(new RoundRobinRouter(nrOfWorkers)),
        "workerRouter");
  }
 
  public void onReceive(Object message) {
    // handle messages ...
  }
}