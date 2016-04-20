package com.edu.netty.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ClientTest {
	@Autowired
	private ClientFactory clientFactory;

	private String address = "127.0.0.1:9999";

	@Test
	public void test_client_xml_parser() throws Exception {
		clientFactory.test_create_bean();
	}

	@Test
	public void test_get_client() throws Exception {
		clientFactory.getClient(address, false);
	}

	@Test
	public void test_connect() {
		Client client = clientFactory.getClient(address, false);
		client.connect();
		client.close();
	}
}
