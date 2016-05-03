package com.edu.netty.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * BeanPostProcessor 是每一个Bean都会调用一次
 * 可以 用来修改bean的属性值
 * @author Administrator
 *
 */
@Component
public class TestBeanPostProcessor implements BeanPostProcessor {
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		//TODO 为什么不调用
		System.out.println(beanName + "\t" +"postProcessBeforeInitialization !!!");
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		//TODO 为什么不调用
		System.out.println(beanName+ "\t" + "postProcessAfterInitialization !!!");
		return bean;
	}
}
