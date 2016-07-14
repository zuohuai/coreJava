package com.edu.java8;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

import org.junit.Test;

import com.google.common.util.concurrent.ThreadFactoryBuilder;


public class CompletableFutureTest {

	@Test
	public void test_replace_future() throws Exception{
		 final CompletableFuture<Reponse> responseFuture = within(
				 new CompletableFuture<>(), Duration.ofSeconds(1));
			responseFuture
			        //.thenAccept(this::send) TODO
			        .exceptionally(throwable -> {
			        	System.out.println("出了异常了");
			        	return null;
			        });

	}
	
	private void sender(){
		System.out.println("发送请求结果");
	}
	
	private static final ScheduledExecutorService scheduler =
	        Executors.newScheduledThreadPool(
	                1,
	                new ThreadFactoryBuilder()
	                        .setDaemon(true)
	                        .setNameFormat("failAfter-%d")
	                        .build());

	public static <T> CompletableFuture<T> failAfter(Duration duration) {
	    final CompletableFuture<T> promise = new CompletableFuture<>();
	    scheduler.schedule(() -> {
	        final TimeoutException ex = new TimeoutException("Timeout after " + duration);
	        return promise.completeExceptionally(ex);
	    }, duration.toMillis(), TimeUnit.MILLISECONDS);
	    return promise;
	}
	
	public static <T> CompletableFuture<T> within(CompletableFuture<T> future, Duration duration) {
	    final CompletableFuture<T> timeout = failAfter(duration);
	    return future.applyToEither(timeout, Function.identity());
	}

	private class Reponse {

	}
}
