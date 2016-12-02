package com.edu.thread.callableAndFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 用来测试CompletableFuture的用法
 * @author zuohuai
 *
 */
public class CompletableFutureTest {

	private static ExecutorService executor = Executors.newFixedThreadPool(5);

	public static void main(String[] args) throws Exception {
		try {
			CompletableFutureTest completableFutureTest = new CompletableFutureTest();
			//completableFutureTest.testFuture();
			completableFutureTest.testCompletableFuture2();
			//completableFutureTest.testCompletableFuture1();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//停止提交任务
			executor.shutdown();
			if (!executor.awaitTermination(100000L, TimeUnit.MILLISECONDS)) {
				//1.停止提交任务 2.中断当前执行的线程 sleep 的线程会被直接唤醒
				//executor.shutdownNow();
			}
		}
	}

	public void testFuture() throws Exception {
		Future<String> result = executor.submit(() -> {
			TimeUnit.SECONDS.sleep(3);
			return "Hello Future";
		});
		System.out.println(result.get());
	}

	public void testCompletableFuture1() throws Exception{
		CompletableFuture<String> resultCompletableFuture = CompletableFuture.supplyAsync(new Supplier<String>() {  
		    @Override  
		    public String get() {  
		        try {  
		            TimeUnit.SECONDS.sleep(3);  
		        } catch (InterruptedException e) {  
		            // TODO Auto-generated catch block  
		            e.printStackTrace();  
		        }  
		        return "Hello CompletableFuture1";  
		    }  
		}, executor);  
		System.out.println(resultCompletableFuture.get());  
	}
	
	public void testCompletableFuture2() throws Exception {
		CompletableFuture<String> resultCompletableFuture = CompletableFuture.supplyAsync(new Supplier<String>() {
			@Override
			public String get() {
				try {
					TimeUnit.SECONDS.sleep(2);
					System.out.println("get当前线程:"+ Thread.currentThread().getName());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				return "Hello CompletableFuture";
			}
		}, executor);
		
		resultCompletableFuture.thenAcceptAsync(new Consumer<String>() {
			@Override
			public void accept(String t) {
				System.out.println(t);
				System.out.println("thenAcceptAsync当前线程:"+Thread.currentThread().getName());
			}
		});
		
		resultCompletableFuture.thenAccept(new Consumer<String>() {
			@Override
			public void accept(String t) {
				System.out.println(t);
				System.out.println("thenAccept当前线程:"+Thread.currentThread().getName());
			}
		});
		System.out.println(123);
	}
}
