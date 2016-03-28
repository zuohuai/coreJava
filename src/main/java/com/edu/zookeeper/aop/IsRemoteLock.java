package com.edu.zookeeper.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface IsRemoteLock {

	/**
	 * 是否锁定对象中的元素
	 * @return
	 */
	String element() default "";
}
