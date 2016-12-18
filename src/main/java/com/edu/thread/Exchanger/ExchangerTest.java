package com.edu.thread.Exchanger;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 用来两个线程之间交换数据
 * 
 * @author zuohuai
 *
 */
public class ExchangerTest {

	public static void main(String[] args) {
		ExecutorService service = Executors.newCachedThreadPool();
		final Exchanger exchanger = new Exchanger();
		service.execute(new Runnable() {
			public void run() {
				try {

					String data1 = "zxx";
					System.out.println("当前线程名称是：" + Thread.currentThread().getName() + "输入的数据是：" + data1);
					Thread.sleep((long) (Math.random() * 10000));
					String data2 = (String) exchanger.exchange(data1);
					System.out.println("当前线程是：" + Thread.currentThread().getName() + "交换回来的数据是：" + data2);
				} catch (Exception e) {

				}
			}
		});
		service.execute(new Runnable() {
			public void run() {
				try {

					String data1 = "lhm";
					System.out.println("当前线程名称是：" + Thread.currentThread().getName() + "输入的数据是：" + data1);
					Thread.sleep((long) (Math.random() * 10000));
					String data2 = (String) exchanger.exchange(data1);
					System.out.println("当前线程是：" + Thread.currentThread().getName() + "交换回来的数据是：" + data2);
				} catch (Exception e) {

				}
			}
		});
		service.shutdown();
	}
}
