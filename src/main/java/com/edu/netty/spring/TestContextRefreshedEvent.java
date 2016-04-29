package com.edu.netty.spring;

import javax.annotation.PostConstruct;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class TestContextRefreshedEvent implements ApplicationListener<ContextRefreshedEvent>{

	/**
	 * 解决依赖时候会出现当依赖于其他Bean对象时候，可能会出现NullPointException的错误
	 */
	@PostConstruct
	private void init(){
		System.out.println("TestContextRefreshedEvent init");
	}
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		System.out.println("所有Bean创建完之后，容器构建完成事件被抛出");
	}

}
