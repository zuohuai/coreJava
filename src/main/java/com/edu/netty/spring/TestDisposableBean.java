package com.edu.netty.spring;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

@Component
public class TestDisposableBean implements DisposableBean {

	@Override
	public void destroy() throws Exception {
		System.out.println("TestDisposableBean destroy execute ");
	}

	@PreDestroy
	public void destroy2() {
		System.out.println("TestDisposableBean destroy2 execute");
	}
}
