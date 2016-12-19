package com.edu.thread.collection;

import java.util.concurrent.ArrayBlockingQueue;

public class ArrayBlockingQueueTest<T> {
	private  final ArrayBlockingQueue<T> arrayBlockingQueue  = new ArrayBlockingQueue<T>(10);
	
	public static void main(String[] args) throws Exception{
		final ArrayBlockingQueueTest<Double> test = new ArrayBlockingQueueTest<Double>();
		for(int i=0;i<20;i++){
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						test.put(Math.random());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();;
		}
		Thread.currentThread().sleep(5000);
		
		for(int i=0;i<30;i++){
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						System.out.println(test.get());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}
	
	public void put(T e) throws Exception{
		arrayBlockingQueue.put(e);
	}
	
	public T get() throws Exception{
		return arrayBlockingQueue.take();
	}
}
