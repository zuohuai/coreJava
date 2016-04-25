package com.edu.netty.schema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.edu.netty.conf.AttributeNames;
import com.edu.netty.conf.ClientConfig;

public class ParserHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(ParserHelper.class);

	/**
	 * TODO 了解  BeanDefinitionBuilder  BeanDefinition BeanDefinitionRegistry  RuntimeBeanReference的区别和联系
	 * @param element
	 * @param parserContext
	 * @param clz
	 * @return
	 */
	public static RuntimeBeanReference getConfig(Element element, ParserContext parserContext, Class<?> clz) {
		String id = element.getAttribute(AttributeNames.ID);
		String location = element.getAttribute(AttributeNames.CONFIG);
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(clz);
		builder.addPropertyValue(ClientConfig.PROP_NAME_LOCATION, location);
		BeanDefinition result = builder.getBeanDefinition();
		BeanDefinitionRegistry registry = parserContext.getRegistry();

		String name = id + ClientConfig.BEAN_NAME_SUFFIX;
		registry.registerBeanDefinition(name, result);;
		return new RuntimeBeanReference(name);
	}
}
