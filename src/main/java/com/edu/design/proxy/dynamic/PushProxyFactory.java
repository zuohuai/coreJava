package com.edu.design.proxy.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 动态代码 工厂
 * @author Administrator
 */
public class PushProxyFactory {
	/** 动态代码处理器 */
	private InvocationHandler invoker;

	public PushProxyFactory(InvocationHandler invoker) {
		this.invoker = invoker;
	}

	public <T> T getProxy(Class<T> clz) {
		T instance = clz.cast(Proxy.newProxyInstance(clz.getClassLoader(), new Class[] { clz }, invoker));
		return instance;
	}
}
