package com.edu.zookeeper.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动远程锁定注释
 * @author Administrator
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AutoRemoteLocked {
	
	/**
	 * 是否强制使用远程锁定
	 * @return
	 */
	boolean value() default true;
	
}
