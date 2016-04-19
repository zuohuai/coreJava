package com.edu.thread.CyclicBarrier;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ��ص㼯�Ϻ�ʾ��ұ˴˵ȴ�����Ҽ��Ϻú�ſ�ʼ������
 * ��ɢ�������ָ���ص㼯�����棬��ͺñ�������˾����Ա������ĩʱ�伯�彼��һ����
 * �ȸ��ԴӼҳ�������˾���Ϻ�
 * ����ͬʱ��������԰���棬��ָ����ͬʱ��ʼ�Ͳͣ�����
 * @author zuohuai
 *
 */
public class CyclicBarrierterTest {

	public static void main(String[] args) {
		ExecutorService service = Executors.newCachedThreadPool();
		final  CyclicBarrier cb = new CyclicBarrier(3);
		for(int i=0;i<3;i++){
			Runnable runnable = new Runnable(){
					public void run(){
					try {
						Thread.sleep((long)(Math.random()*10000));	
						System.out.println("�߳�" + Thread.currentThread().getName() + 
								"�������Ｏ�ϵص�1����ǰ����" + (cb.getNumberWaiting()+1) + "���Ѿ����" + (cb.getNumberWaiting()==2?"�������ˣ������߰�":"���ڵȺ�"));						
						cb.await();
						
						Thread.sleep((long)(Math.random()*10000));	
						System.out.println("�߳�" + Thread.currentThread().getName() + 
								"�������Ｏ�ϵص�2����ǰ����" + (cb.getNumberWaiting()+1) + "���Ѿ����" + (cb.getNumberWaiting()==2?"�������ˣ������߰�":"���ڵȺ�"));
						cb.await();	
						Thread.sleep((long)(Math.random()*10000));	
						System.out.println("�߳�" + Thread.currentThread().getName() + 
								"�������Ｏ�ϵص�3����ǰ����" + (cb.getNumberWaiting() + 1) + "���Ѿ����" + (cb.getNumberWaiting()==2?"�������ˣ������߰�":"���ڵȺ�"));						
						cb.await();						
					} catch (Exception e) {
						e.printStackTrace();
					}				
				}
			};
			service.execute(runnable);
		}
		service.shutdown();
	}
}
