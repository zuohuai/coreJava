package com.edu.codis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.edu.service.CodisTestServiceImpl;

/**
 * 容器启动方法
 * 
 * @author root
 *
 */
public class CodisMain {

	private static final Logger logger = LoggerFactory.getLogger(CodisMain.class);

	/** 默认的上下文配置名 */
	private static final String DEFAULT_APPLICATION_CONTEXT = "applicationContext.xml";

	public static void main(String[] arguments)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				DEFAULT_APPLICATION_CONTEXT);
		try {
			logger.error("容器已启动完成，开启服务器控制台");
			CodisTestServiceImpl serviceImpl = applicationContext.getBean(CodisTestServiceImpl.class);
			serviceImpl.testFirstCodisLoadAndSave();
		} catch (Exception exception) {
			exception.printStackTrace();
			if (applicationContext.isRunning()) {
				applicationContext.destroy();
			}
		}
	}
}
