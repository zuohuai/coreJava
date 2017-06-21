package com.edu.dom;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

public class DomTest {

	private static final String DOM_FILE = "E:\\study\\coreJava\\file\\pom.xml";

	@Test
	public void test_parse() throws Exception {
		SAXReader reader = new SAXReader();
		Document document = reader.read(DOM_FILE);
		Element projectElement = document.getRootElement();
		// 1.获取properties 节点
		Element propertiesElement = projectElement.element("properties");

		List<Element> subElements = propertiesElement.elements();
		for (Element element : subElements) {
			String name = element.getName();
			String value = element.getStringValue();
			name = buildName(name);
			System.out.println(name+" = " + value);
		}
	}
	
	private String buildName(String name){
		String point = ".";
		while(name.contains(point)){
			int index = name.indexOf(point);
			String subStr = name.substring(index+1, index+2).toUpperCase();
			String startStr = name.substring(0, index);
			String endStr = name.substring(index+2);
			
			name = startStr + subStr + endStr;
		}
		
		point = "-";
		while(name.contains(point)){
			int index = name.indexOf(point);
			String subStr = name.substring(index+1, index+2).toUpperCase();
			
			String startStr = name.substring(0, index);
			String endStr = name.substring(index+2);
			
			name = startStr + subStr + endStr;
		}
		
		return name;
	}

	
	
}
