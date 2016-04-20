package com.edu.thread.Semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * �����ź�����Semaphore�������ʵ�ֻ������Ĺ��ܣ�
 * ���ҿ�������һ���̻߳���ˡ�������������һ���߳��ͷš�������
 * ���Ӧ���������ָ���һЩ���ϡ�
 * @author zuohuai
 *
 */
public class SemaphoreTest {
	public static void main(String[] args) {
		ExecutorService service = Executors.newCachedThreadPool();
		final  Semaphore sp = new Semaphore(3);
		for(int i=0;i<10;i++){
			Runnable runnable = new Runnable(){
					public void run(){
					try {
						sp.acquire();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					System.out.println("�߳�" + Thread.currentThread().getName() + 
							"���룬��ǰ����" + (3-sp.availablePermits()) + "������");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("�߳�" + Thread.currentThread().getName() + 
							"�����뿪");					
					sp.release();
					//���������ʱ��ִ�в�׼ȷ����Ϊ��û�к�����Ĵ���ϳ�ԭ�ӵ�Ԫ
					System.out.println("�߳�" + Thread.currentThread().getName() + 
							"���뿪����ǰ����" + (3-sp.availablePermits()) + "������");					
				}
			};
			service.execute(runnable);			
		}
	}
}

