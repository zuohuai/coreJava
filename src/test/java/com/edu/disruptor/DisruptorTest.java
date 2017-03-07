package com.edu.disruptor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class DisruptorTest {
	@Autowired
	private com.edu.event.EventBus eventBus1;
	@Autowired
	private com.edu.disruptor.EventBus eventBus2;

	@Component
	public static class DisruptorReceiver extends com.edu.disruptor.AbstractReceiver<String>{

		@Override
		public String[] getEventNames() {
			return new String[]{EVENT_NAME};
		}

		@Override
		public void doEvent(String event) {
			count.incrementAndGet();
		}
		
	}
	@Component
	public static class MyReceiver extends com.edu.event.AbstractReceiver<String>{
		
		@Override
		public String[] getEventNames() {
			return new String[]{EVENT_NAME};
		}
		
		@Override
		public void doEvent(String event) {
			count.incrementAndGet();
		}
		
	}
	
	private static final String EVENT_NAME = "TEST_EVENT";
	private static AtomicInteger count = new AtomicInteger();
	
	@Test(timeout=2000)
	public void test_post_event1() throws Exception {
		for(int i=0; i < 1000000; i++){
			com.edu.event.Event<String> event = com.edu.event.Event.valueOf(EVENT_NAME, "Hello World3");
			eventBus1.post(event);
		}
	
		Thread.sleep(1000);
		System.out.println(count.get());
		assertThat(count.get(), is(1000000));
	}
	
	
	@Test(timeout=1500)
	public void test_post_event2() throws Exception {
		for(int i=0; i < 1000000; i++){
			com.edu.disruptor.Event<String> event = com.edu.disruptor.Event.valueOf(EVENT_NAME, "Hello World3");
			eventBus2.post(event);
		}
		Thread.sleep(1000);
		System.out.println(count.get());
		assertThat(count.get(), is(1000000));
	}
}
