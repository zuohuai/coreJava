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
	
	@Test
	public void test_client_xml_parser() throws Exception{
		clientFactory.test_create_bean();
	}
	
}
