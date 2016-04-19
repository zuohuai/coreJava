package com.edu.thread.CountDownLatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ���絹��ʱ������������CountDownLatch�����countDown�����ͽ���������1��
 * ����������0ʱ�������еȴ��߻򵥸��ȴ��߿�ʼִ�С�
 * ��ֱ��ͨ��������˵��CountDownLatch�����ã�����ѧԱ�����Ч����ֱ�ӡ�
	����ʵ��һ���ˣ�Ҳ�����Ƕ���ˣ��ȴ����������˶���֪ͨ����
	����ʵ��һ����֪ͨ����˵�Ч�������Ʋ���һ������˶�Աͬʱ��ʼ���ܣ�
	���������˶�Ա���ܵ��յ����вſ��Թ��������������������������ܵ���Ϸ���򲻴�Ŷ��
	������ʵ��һ���ƻ���Ҫ����쵼��ǩ�ֺ���ܼ�������ʵʩ�������
 * @author zuohuai
 *
 */
public class CountDownLatchTest {
	public static void main(String[] args) {
		ExecutorService service = Executors.newCachedThreadPool();
		final CountDownLatch cdOrder = new CountDownLatch(1);
		final CountDownLatch cdAnswer = new CountDownLatch(3);		
		for(int i=0;i<3;i++){
			Runnable runnable = new Runnable(){
					public void run(){
					try {
						System.out.println("�߳�" + Thread.currentThread().getName() + 
								"��׼����������");						
						cdOrder.await();
						System.out.println("�߳�" + Thread.currentThread().getName() + 
						"�ѽ�������");								
						Thread.sleep((long)(Math.random()*10000));	
						System.out.println("�߳�" + Thread.currentThread().getName() + 
								"��Ӧ�������");						
						cdAnswer.countDown();						
					} catch (Exception e) {
						e.printStackTrace();
					}				
				}
			};
			service.execute(runnable);
		}		
		try {
			Thread.sleep((long)Math.random()*100000);
		
			System.out.println("�߳�" + Thread.currentThread().getName() + 
					"������������");						
			cdOrder.countDown();
			System.out.println("�߳�" + Thread.currentThread().getName() + 
			"�ѷ���������ڵȴ����");	
			cdAnswer.await();
			System.out.println("�߳�" + Thread.currentThread().getName() + 
			"���յ�������Ӧ���");	
		} catch (Exception e) {
			e.printStackTrace();
		}				
		service.shutdown();
	}
}
