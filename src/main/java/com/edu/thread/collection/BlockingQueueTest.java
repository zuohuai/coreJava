package com.edu.thread.collection;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueTest {
	public static void main(String[] args){
		BlockingQueue<Double> q = new ArrayBlockingQueue<Double>(10);
		Producer p = new Producer(q);
		Consumer<Double> c1 = new Consumer<Double>(q);
		Consumer<Double> c2 = new Consumer<Double>(q);
		new Thread(p).start();
		new Thread(c1).start();
		new Thread(c2).start();
	}
}

class Producer implements Runnable {
	private final BlockingQueue queue;

	Producer(BlockingQueue q) {
		queue = q;
	}

	public void run() {
		try {
			while (true) {
				queue.put(produce());
			}
		} catch (InterruptedException ex) {

		}
	}

	Object produce() {
		return Math.random();
	}
}

class Consumer<T> implements Runnable {
	private final BlockingQueue<T> queue;

	Consumer(BlockingQueue<T> q) {
		queue = q;
	}

	public void run() {
		try {
			while (true) {
				consume(queue.take());
			}
		} catch (InterruptedException ex) {

		}
	}

	void consume(Object x) {
		System.out.println(x);
		try{
			Thread.currentThread().sleep(1000);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}

