package com.edu.thread.Exchanger;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class ExchangerTest {

	public static void main(String[] args) {
		ExecutorService service = Executors.newCachedThreadPool();
		final Exchanger exchanger = new Exchanger();
		service.execute(new Runnable() {
			public void run() {
				try {

					String data1 = "++++物品++++";
					System.out.println("线程" + Thread.currentThread().getName() + "正在把数据" + data1 + "交易出去");
					Thread.sleep((long) (Math.random() * 10000));
					String data2 = (String) exchanger.exchange(data1);
					System.out.println("线程" + Thread.currentThread().getName() + "换回的数据为" + data2);
				} catch (Exception e) {

				}
			}
		});
		service.execute(new Runnable() {
			public void run() {
				try {

					String data1 = "****钱****";
					System.out.println("线程" + Thread.currentThread().getName() + "已经拿好" + data1);
					Thread.sleep((long) (Math.random() * 10000));
					String data2 = (String) exchanger.exchange(data1);
					System.out.println("线程" + Thread.currentThread().getName() + "换回的数据为" + data2);
				} catch (Exception e) {

				}
			}
		});

		service.shutdown();
	}

}