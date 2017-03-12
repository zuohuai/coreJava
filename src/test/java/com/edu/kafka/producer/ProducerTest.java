package com.edu.kafka.producer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = { "ProducerTest-context.xml" })
public class ProducerTest {

	@Autowired
	private MessageProducer producer;

	@Test
	public void test_kafka_start() throws Exception {
		producer.sayHelloProducer();
	}

}
