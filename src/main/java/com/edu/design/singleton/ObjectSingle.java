package com.edu.design.singleton;

/**
 * 单例设计模式
 * @author Administrator
 *
 */
public class ObjectSingle {

	private static volatile ObjectSingle objectSingle;

	private ObjectSingle() {

	}

	public static ObjectSingle getInstance() {
		synchronized (ObjectSingle.class) {
			if (objectSingle == null) {
				objectSingle = new ObjectSingle();
			}
			return objectSingle;
		}

	}
}
