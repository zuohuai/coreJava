package com.edu.thread.callableAndFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

/**
 * 
 * ClassName: ThreadStopTest <br/>
 * Function: 测试线程的关闭 <br/>
 * date: 2016年12月2日 上午10:29:07 <br/>
 * 
 * @author hison.zhang
 * @version
 * @since JDK 1.7
 */
public class ThreadStopTest {

	public static void main(String[] args) throws Exception{
		ThreadStopTest threadStopTest = new ThreadStopTest();
		threadStopTest.test_callable_stop();
	}
	
	@Test
	public void test_tradition_stop() throws Exception {
		Thread runThread = new Thread(new Runnable() {
			public void run() {
				while (true) {
					System.out.println("runing");
				}
			}
		});
		runThread.start();
		Thread.sleep(3000L);
		System.out.println("主线程睡眠3s之后被唤醒");
		//终极方法，黑科技，会引起一系列的问题
		runThread.stop();
		System.out.println("线程被终止");
		System.out.println("主线程睡眠2s");
		Thread.sleep(2000L);
		System.out.println("程序终止");
	}

	public void test_callable_stop() throws Exception {
		ExecutorService theadPool = Executors.newFixedThreadPool(3) ;
		Future<Object> future = theadPool.submit(new Callable<Object>(){
			@Override
			public Object call(){
				try{
					Thread.sleep(2000L);
					//TimeUnit.SECONDS.sleep(1);
				}catch(Exception e){
					
				}
				while(!Thread.interrupted()){
					long_time_execute();
				}
				return "hello";
			}
		});
		try {
			future.get(1, TimeUnit.MICROSECONDS);
			//future.cancel(true);
			theadPool.shutdown();
			if(!theadPool.awaitTermination(3, TimeUnit.SECONDS)){
				theadPool.shutdownNow();
			}
		} catch (Exception e) {
			e.printStackTrace();
			theadPool.shutdown();
			if(!theadPool.awaitTermination(3, TimeUnit.SECONDS)){
				theadPool.shutdownNow();
			}
		}
	}
	
	public static void long_time_execute(){
		for(int i=0 ; i< 1000000L; i++){
			Thread.yield();
			System.out.println("Hello");
		}
	}
}
