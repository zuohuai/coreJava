package com.edu.thread.threadPoolTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPoolTest {
	
	public static void main(String[] args){
		/**
		 * ���ʵ���߳����ˣ�������һ�������Դ���һ����һ�̵߳��̳߳أ�����ʹ���ػ��߳�
		 */
//		ExecutorService  threadPool1 = Executors.newSingleThreadExecutor();����СΪ1 ��ʱ��ʼ����һ���߳����̳߳��� 
		ExecutorService  threadPool = Executors.newFixedThreadPool(3); //�̶� �̵߳����� 
//		ExecutorService  threadPool = Executors.newCachedThreadPool(); //��������Ķ��٣��̳߳��е��߳���������
		for(int i=1;i<=10;i++){
			threadPool.execute(new Runnable(){
				@Override
				public void run(){
					for(int j=1;j<=10;j++){
						try {
							Thread.sleep(10);
							System.out.println(Thread.currentThread().getName()+" is loop of "+j);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});
		}
		System.out.println("all of 10 task has commited ! ");
		threadPool.shutdown(); //����������������У��ȵ��������н���֮�󣬹ر��̳߳�
//		threadPool.shutdownNow() ; //����ֹͣ�̳߳�
		
		/**
		 * ��ʱ��
		 */
		Executors.newScheduledThreadPool(3).scheduleAtFixedRate(new Runnable(){
			@Override
			public void run(){
				System.out.println("bomb ! ");
			}
		}, 10,2,TimeUnit.SECONDS);
	}
}
