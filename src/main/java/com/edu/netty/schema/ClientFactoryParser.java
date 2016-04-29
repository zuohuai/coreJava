package com.edu.netty.schema;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.edu.netty.client.ClientFactory;
import com.edu.netty.conf.AttributeNames;
import com.edu.netty.conf.ClientConfig;

/**
 * 手动创建一个Bean
 * @author Administrator
 *
 */
public class ClientFactoryParser  extends AbstractBeanDefinitionParser {

	@Override
	protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
		System.out.println("parseInternal !!!");
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(ClientFactory.class);
		
		//添加依赖 TODO
		String dependOn = element.getAttribute(AttributeNames.DEPEND_ON);
		if(!StringUtils.isBlank(dependOn)){
			builder.addDependsOn(dependOn);
		}
		//设置属性配置
		RuntimeBeanReference config = ParserHelper.getConfig(element, parserContext, ClientConfig.class);

		// TODO 设置分发器
		
		//TODO 设置过滤器集合属性 
		//builder.addConstructorArgValue(config);
		
		return builder.getBeanDefinition();
	}
}
