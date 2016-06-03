package com.edu.design.singleton;

/**
 * 单例设计模式(这种方案去解决单例的线程问题，是很逗逼的，请看SingletonOther的方案来解决单例问题 )
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
