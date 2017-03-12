package com.edu.kafka.producer;

import java.util.Properties;

import org.apache.commons.codec.StringEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;

@Component
public class MessageProducer {
	@Autowired
	private MessageProducerConfig producerConfig;

	public void sayHelloProducer() {
		
	}
	
	private Producer createProducer() {
		Properties properties = new Properties();
		properties.put("zookeeper.connect", "192.168.1.110:2181,192.168.1.111:2181,192.168.1.112:2181");// 声明zk
		properties.put("serializer.class", StringEncoder.class.getName());
		properties.put("metadata.broker.list", "192.168.1.110:9092,192.168.1.111:9093,192.168.1.112:9094");// 声明kafka
																											// broker
		return new Producer<Integer, String>(new ProducerConfig(properties));
	}
}
