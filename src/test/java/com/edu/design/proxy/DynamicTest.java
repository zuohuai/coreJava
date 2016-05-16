package com.edu.design.proxy;

import java.lang.reflect.InvocationHandler;

import org.junit.Test;

import com.edu.design.proxy.dynamic.PushProxyFactory;
import com.edu.design.proxy.dynamic.TestPush;
import com.edu.design.proxy.dynamic.TestPushHandler;
import com.edu.design.proxy.dynamic.TestPushImpl;

public class DynamicTest {

	@Test
	public void test_dynamic() throws Exception {
		TestPush testPush = new TestPushImpl();
		InvocationHandler invoker = new TestPushHandler(testPush);

		//获取动态代理
		PushProxyFactory proxyFactory = new PushProxyFactory(invoker);
		TestPush proxy = proxyFactory.getProxy(TestPush.class);
		proxy.push();
	}
}
