package com.edu.akka;

import org.junit.Test;

import scala.concurrent.duration.Duration;

public class AkkaTest {

	@Test
	public void test_java_duration() throws Exception{
		final Duration fivesec = Duration.create(5, "seconds");
	    final Duration threemillis = Duration.create("3 millis");
	    final Duration diff = fivesec.minus(threemillis);
	    
	    System.out.println();
	}
}
