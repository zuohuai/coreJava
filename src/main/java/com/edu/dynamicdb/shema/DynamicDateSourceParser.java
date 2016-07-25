package com.edu.dynamicdb.shema;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.ResourceUtils;

import com.edu.dynamicdb.proxy.DataSourceBuilder;
import com.edu.dynamicdb.proxy.DynamicDateSource;

/**
 * 手动创建一个Bean
 * 
 * @author zuohuai
 *
 */
public class DynamicDateSourceParser extends AbstractBeanDefinitionParser {
	private final static String PROP_URL = "url";
	private final static String PROP_USERNAME = "username";
	private final static String PROP_PASSWORD = "password";

	private final static String TARGET_DATA_SOURCES = "targetDataSources";

	@SuppressWarnings({ "unused", "rawtypes" })
	@Override
	protected AbstractBeanDefinition parseInternal(org.w3c.dom.Element element, ParserContext parserContext) {

		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(DynamicDateSource.class);
		String driverName = element.getAttribute(SchemaConstants.DB_CLASS);
		if (StringUtils.isBlank(driverName)) {
			throw new IllegalArgumentException("启动参数driverName不能为空");
		}

		Map<Object, Object> targetDataSources = new HashMap<>();
		builder.addPropertyValue(TARGET_DATA_SOURCES, targetDataSources);

		// 读取用url username password num
		String path = element.getAttribute(SchemaConstants.DB_PATH);
		String proPath = element.getAttribute(SchemaConstants.DB_PROPERTIES);
		FileInputStream fileInputStream = null;
		try {
			File resourceFile = ResourceUtils.getFile(proPath);
			fileInputStream = new FileInputStream(resourceFile);
			Properties properties = new Properties();
			properties.load(fileInputStream);

			String alias = StringUtils.EMPTY;
			File cfgFile = ResourceUtils.getFile(path);
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(cfgFile);
			Element root = document.getRootElement();
			List dbElements = root.elements(SchemaConstants.DB);
			Iterator iterator = dbElements.iterator();
			while (iterator.hasNext()) {
				Element child = Element.class.cast(iterator.next());
				Attribute defalutAttr = child.attribute(SchemaConstants.DB);
				boolean defalut = false;
				if (defalutAttr != null) {
					defalut = Boolean.parseBoolean(defalutAttr.getText());
				}
				alias = child.element(SchemaConstants.DB_ALIAS).getText();
				properties.put(PROP_URL, child.element(SchemaConstants.DB_URL).getTextTrim());
				properties.put(PROP_USERNAME, child.element(SchemaConstants.DB_USERNAME).getTextTrim());
				properties.put(PROP_PASSWORD, child.element(SchemaConstants.DB_PASSWORD).getTextTrim());

				DataSource dataSource = DataSourceBuilder.createDataSource(properties);
				Object targetSource = targetDataSources.get(alias);
				if(targetSource != null){
					throw  new IllegalArgumentException("存在重复别名"+alias);
				}
				if (defalut) {

				}

			}

		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException(path + "文件不存在");
		} catch (DocumentException e) {
			throw new IllegalArgumentException(path + "中xml解析失败");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(fileInputStream);
		}
		return builder.getBeanDefinition();
	}
}
