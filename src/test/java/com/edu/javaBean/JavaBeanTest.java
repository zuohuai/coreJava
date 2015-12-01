package com.edu.javaBean;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;

public class JavaBeanTest {

	@Test
	public void test_propertyDescriptor() throws Exception {
		PropertyDescriptor pd = new PropertyDescriptor("id", Person.class);
		Method readMethod = pd.getReadMethod();
		Assert.assertNotNull(readMethod);
		Method writeMethod = pd.getWriteMethod();
		Assert.assertNotNull(writeMethod);
	}

}
