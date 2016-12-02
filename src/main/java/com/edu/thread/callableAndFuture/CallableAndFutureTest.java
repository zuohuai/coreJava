package com.edu.thread.callableAndFuture;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 
 * ClassName: CallableAndFutureTest <br/>  
 *  
 * @author hison.zhang  
 * @version   
 * @since JDK 1.7
 */
public class CallableAndFutureTest {
	
	public static void main(String[] args){
		ExecutorService theadPool = Executors.newFixedThreadPool(3) ;
		Future<Object> future = theadPool.submit(new Callable<Object>(){
			@Override
			public Object call(){
				try {
					Thread.sleep(new Random().nextInt(5000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return "hello";
			}
		});
		Callable<Object> myTask = new Callable<Object>(){
			@Override
			public Object call(){
				while (!Thread.interrupted()) {
					//模拟一个执行时间超级长的代码
					while(true){
						System.out.println("Hello, I am on call");
						Thread.yield();
					}
				}
				System.out.println("++++被中断了++++");
				return "被中断";
			}
		};
		Future<Object> future2 = theadPool.submit(myTask);
		try {
			System.out.println(future.get());
			future2.cancel(true);
			System.out.println(future2.get());
		} catch (Exception e) {
			e.printStackTrace();
		} 
		CompletionService<Object> completionServcie = new ExecutorCompletionService<Object>(theadPool);
		for(int i=1;i<=10;i++){
			final int j = i;
			completionServcie.submit(new Callable<Object>(){
				@Override
				public Object call(){
					try {
						Thread.sleep(new Random().nextInt(1000));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					return "返回值是:"+j;
				}
			});
		}
		for(int i=1;i<=10;i++){
			;
			try {
				System.out.println(completionServcie.take().get());
			} catch (InterruptedException e) {
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		theadPool.shutdown();
	}
}
