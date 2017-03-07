package com.edu.scheduler.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.scheduling.support.MethodInvokingRunnable;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.MethodCallback;

import com.edu.scheduler.Scheduled;
import com.edu.scheduler.ScheduledTask;
import com.edu.scheduler.Scheduler;

/**
 * 
 * ClassName: ScheduledProcessor <br/>  
 * Function:  定时任务处理器, 由他负责检查定时任务声明，以及开启任务线程池 <br/>  
 * date: 2016年7月28日 下午2:41:29 <br/>  
 *  
 * @author hison.zhang  
 * @version   
 * @since JDK 1.7
 */
@Component
public class ScheduledProcessor implements BeanPostProcessor, ApplicationListener<ContextRefreshedEvent>, Ordered, BeanFactoryAware {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ScheduledProcessor.class);
	
	// 定时任务注册部分
	private final Map<ScheduledTask, String> tasks = new HashMap<ScheduledTask, String>();

	/** 获取定时任务信息 */
	public Object postProcessAfterInitialization(final Object bean, String beanName) {
		ReflectionUtils.doWithMethods(AopUtils.getTargetClass(bean), new MethodCallback() {
			public void doWith(Method method) throws IllegalArgumentException,
					IllegalAccessException {
				Scheduled annotation = AnnotationUtils.getAnnotation(method, Scheduled.class);
				if (annotation == null) {
					return;
				}

				ScheduledTask task = createTask(bean, method, annotation);
				String experssion = resolveExperssion(bean, annotation);
				tasks.put(task, experssion);
			}
		});
		return bean;
	}

	/** 创建定时任务 */
	private ScheduledTask createTask(Object bean, Method method, Scheduled annotation) {
		if (!void.class.equals(method.getReturnType())) {
			throw new IllegalArgumentException("定时方法的返回值必须为 void");
		}
		if (method.getParameterTypes().length != 0) {
			throw new IllegalArgumentException("定时方法不能有参数");
		}

		final MethodInvokingRunnable runnable = new MethodInvokingRunnable();
		runnable.setTargetObject(bean);
		runnable.setTargetMethod(method.getName());
		runnable.setArguments(new Object[0]);
		try {
			runnable.prepare();
		} catch (Exception e) {
			throw new IllegalStateException("无法创建定时任务", e);
		}

		final String name = annotation.name();
		return new ScheduledTask() {
			@Override
			public void run() {
				runnable.run();
			}

			@Override
			public String getName() {
				return name;
			}
		};
	}

	/** 获取Cron表达式 */
	private String resolveExperssion(Object bean, Scheduled annotation) {
		String result = null;
		switch (annotation.type()) {
		case EXPRESSION:
			result = annotation.value();
			break;
		default:
			break;
		}
		return result;
	}

	/** 定时任务调度器 */
	@Autowired
	private Scheduler scheduler;
	
	private volatile boolean inited = false;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if(inited) {
			return;
		}
		inited = true;
		for (Entry<ScheduledTask, String> entry : tasks.entrySet()) {
			scheduler.schedule(entry.getKey(), new CronTrigger(entry.getValue()));
		}
	}

	public int getOrder() {
		return LOWEST_PRECEDENCE;
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName) {
		return bean;
	}

	@SuppressWarnings("unused")
	private BeanFactory beanFactory;
	
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

}
