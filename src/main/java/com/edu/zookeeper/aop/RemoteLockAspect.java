package com.edu.zookeeper.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.edu.zookeeper.ZookeeperTest;

/**
 * 自动锁定方法切面
 * 
 * @author Administrator
 *
 */
@Aspect
@Component
public class RemoteLockAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(RemoteLockAspect.class);

	/**
	 * 定义一个方法拦截
	 * @param pjp
	 * @param autoRemoteLocked
	 * @return
	 * @throws Throwable
	 */
	@Around("@annotation(autoRemoteLocked)")
	public Object excute(ProceedingJoinPoint pjp, AutoRemoteLocked autoRemoteLocked) throws Throwable {
		Signature sign = pjp.getSignature();
		if (!(sign instanceof MethodSignature)) {
			LOGGER.error("不支持拦截切面:{}", sign);
			return pjp.proceed(pjp.getArgs());
		}

		Method method = ((MethodSignature) sign).getMethod();
		String path = getLock(pjp, method);
		if (StringUtils.isEmpty(path)) {
			return pjp.proceed(pjp.getArgs());
		}

		// 开始远程锁定
		CuratorFramework client = null;
		try {
			client = CuratorFrameworkFactory.newClient(ZookeeperTest.SERVER, new ExponentialBackoffRetry(1000, 3));
			client.start();
			final ClientLock clientLock = new ClientLock(client, path, "Client:" + sign.getName());
			return clientLock.doWork(pjp, 10, TimeUnit.SECONDS);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		} 
		finally {
			CloseableUtils.closeQuietly(client);
		}
	}

	/**
	 * 目前只是支持单个远程锁
	 * @param method
	 * @return
	 */
	private String getLock(ProceedingJoinPoint pjp, Method method) {
		String result = StringUtils.EMPTY;
		Annotation[][] annos = method.getParameterAnnotations();
		int index = 0;
		for (int i = 0; i < annos.length; i++) {
			;
			for (Annotation anno : annos[i]) {
				if (anno instanceof IsRemoteLock) {
					index = i;
					break;
				}
			}
		}
		Object[] args = pjp.getArgs();
		result = args[index].toString();
		return result;
	}
}
