package com.edu.design.proxy.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class TestPushHandler implements InvocationHandler {
	/** 被代理的对象 */
	private TestPush testPush;

	public TestPushHandler(TestPush testPush) {
		this.testPush = testPush;
	}

	public TestPushHandler() {
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("Before TestPush");
		Object result = null;
		// Object result = method.invoke(testPush, args);
		System.out.println("After TestPush");
		return result;
	}

}
