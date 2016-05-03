package com.edu.netty.conf;

import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

/**
 * 服务器信息配置对象 ClientConfig
 * @author Administrator
 */
public class ClientConfig implements ApplicationContextAware {
	/** Spring中的Bean Name的后缀 */
	public static final String BEAN_NAME_SUFFIX = "-config";
	/** 资源属性名 */
	public static final String PROP_NAME_LOCATION = "location";

	/** 配置文件的位置 */
	private String location;
	/** 配置文件 */
	private Properties properties;

	private SessionConfig sessionConfig;

	private ExecutorConfig executorConfig;

	/** 默认地址 */
	private String defaultAddress = "localhost";
	/** 回应超时时间(ms) */
	private int responseTimeout = 5000;
	/** 客户端过期时间(s) */
	private int removeTimes = 300;

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientConfig.class);

	@PostConstruct
	public void initialize() {
		// 加载配置文件
		Resource resource = this.applictionContext.getResource(location);
		properties = new Properties();
		try {
			properties.load(resource.getInputStream());
		} catch (Exception e) {
			FormattingTuple message = MessageFormatter.format("资源[{}]加载失败", location);
			LOGGER.error(message.getMessage(), e);
			throw new RuntimeException(message.getMessage(), e);
		}

		initializeClientConfig();
		initializeSessionConfig();
		initializeExecutorConfig();
	}

	/**
	 * 初始化地址连接信息
	 */
	private void initializeClientConfig() {
		String value = properties.getProperty(ClientConfigConstant.KEY_DEAULT_ADDRESS);
		this.defaultAddress = value;

		value = properties.getProperty(ClientConfigConstant.KEY_RESPONSE_TIMEOUT);
		this.responseTimeout = Integer.parseInt(value);

		value = properties.getProperty(ClientConfigConstant.KEY_REMOVE_TIME);
		this.removeTimes = Integer.parseInt(value);
	}

	/**
	 * 初始化连接会话配置
	 */
	private void initializeSessionConfig() {
		String value = properties.getProperty(ClientConfigConstant.KEY_BUFFER_READ);
		int readBufferSize = Integer.parseInt(value);

		value = properties.getProperty(ClientConfigConstant.KEY_BUFFER_WRITE);
		int writeBufferSize = Integer.parseInt(value);

		value = properties.getProperty(ClientConfigConstant.KEY_TIMEOUT);
		int timeout = Integer.parseInt(value);

		this.sessionConfig = SessionConfig.valueOfClient(readBufferSize, writeBufferSize, timeout);
	}

	private void initializeExecutorConfig() {
		String minStr = properties.getProperty(ClientConfigConstant.KEY_POOL_MIN);
		String maxStr = properties.getProperty(ClientConfigConstant.KEY_POOL_MAX);
		String idleStr = properties.getProperty(ClientConfigConstant.KEY_POOL_IDLE);

		int min = StringUtils.isBlank(minStr) ? Math.max(4, Runtime.getRuntime().availableProcessors() / 2)
				: Integer.parseInt(minStr);
		int max = StringUtils.isBlank(maxStr) ? Math.max(4, Runtime.getRuntime().availableProcessors() / 2)
				: Integer.parseInt(maxStr);

		long idle = StringUtils.isBlank(idleStr) ? 3000 : Long.parseLong(idleStr);

		executorConfig = ExecutorConfig.valueOf(min, max, idle);
	}

	private ApplicationContext applictionContext;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public Properties getProperties() {
		return properties;
	}

	public SessionConfig getSessionConfig() {
		return sessionConfig;
	}

	public ExecutorConfig getExecutorConfig() {
		return executorConfig;
	}

	public String getDefaultAddress() {
		return defaultAddress;
	}

	public int getResponseTimeout() {
		return responseTimeout;
	}

	public int getRemoveTimes() {
		return removeTimes;
	}

	@Override
	public void setApplicationContext(ApplicationContext applictionContext) throws BeansException {
		this.applictionContext = applictionContext;
	}

}
