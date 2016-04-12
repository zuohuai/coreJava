package com.edu.javaassist;

import org.junit.Test;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

public class JavaassistTest {

	private ClassPool pool = ClassPool.getDefault();

	{
		// 用来解决在web容器中不能找到对应class的问题
		pool.insertClassPath(new ClassClassPath(this.getClass()));
	}

	/**
	 * 创建类和创建方法
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_create_class() throws Exception {

		CtClass cc = pool.makeClass("com.edu.javaassist.HelloWorld");
		CtMethod mthd = CtNewMethod.make("public Integer getInteger() { return null; }", cc);
		cc.addMethod(mthd);
		cc.writeFile();
	}

	/**
	 * 测试javaassist 在AOP中的应用
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_aop() throws Exception {
		// 对已有代码每次move执行时做埋点
		CtClass cc = pool.get("com.edu.javaassist.Point");
		CtMethod m = cc.getDeclaredMethod("move");
		m.insertBefore("{ System.out.println($1);  }");
		m.insertAfter("{System.out.println($2);    }");
		cc.writeFile();
	}
}
