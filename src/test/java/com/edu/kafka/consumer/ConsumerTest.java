package com.edu.kafka.consumer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = { "ProducerTest-context.xml" })
public class ConsumerTest {

	@Autowired
	private Consumer consumer;

	@Test
	public void test_kafka_start() throws Exception {
		consumer.sayHelloConsumer();
	}

}
