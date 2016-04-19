package com.edu.thread.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Lock�ȴ�ͳ�߳�ģ���е�synchronized��ʽ������������������е������ƣ�
 * ������ҲӦ����һ������
 * �����߳�ִ�еĴ���Ƭ��Ҫʵ��ͬ�������Ч�������Ǳ�����ͬһ��Lock����
 * ���ڴ�ͳ�̻߳���  ʹ��synchronized �ؼ���
 * Ҫ���л���Ч����ʱ�򣬱���ʹ��ͬһ��������
 * @author zuohuai
 *
 */
public class LockTest {
//	private static int a = 0; 
	public static void main(String[] args){
		new LockTest().init();
	}
	
	public void init(){
		 final Outputer outputer = new Outputer(); //ΪʲôҪ����final
		//outputer.output1("");
		new Thread(new Runnable(){
			@Override
			public void run(){
				while(true){
					try {
						Thread.sleep(10);
						outputer.output1("zhangzuohuai");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
		}).start();
		new Thread(new Runnable(){
			@Override
			public void run(){
				while(true){
					try {
						Thread.sleep(10);
						outputer.output1("aaa");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	/**
	 * ���ڲ��� ���Ե����ⲿ�� ������ �Ǿ�̬��Ա
	 * @author zuohuai
	 *
	 */
	 static class Outputer {
		/**
		 * ��ʽһ
		 * ������ϼ���synchronized 
		 * �������synchronized �ͻ���֡�zhangzuohua
		 * i�� �Ľ��
		 * @param name
		 */
		 private Lock lock = new ReentrantLock(); 
		 // ��������synchronized�����Ʒ�� ��synchronized ���� ���ʵ����� �����Բ鿴  http://tenyears.iteye.com/blog/48750
		private void output1(String name){
			//System.out.println(a);
			int length = name.length();
			lock.lock();
			try{
				for(int i=0;i<length;i++){
					System.out.print(name.charAt(i));
				}
			}finally{
				lock.unlock();
			}
			System.out.println();
			
		}
		
		/**
		 * ������
		 * ����ǰ����synchronized ���ͷ���һ���Ի��⣬��Ϊ��ʹ�õĶ��� ͬһ������this ����
		 * @param name
		 */
		synchronized private void output2(String name){
			int length = name.length();
				for(int i=0;i<length;i++){
					System.out.print(name.charAt(i));
					System.out.println();
			}
		}
		
		/**
		 * ������
		 * ��̬����ǰ����synchronized ���ͷ���һ���Ի��⣬��Ϊ��ʹ�õĶ��� ͬһ������Outputer.class  �ֽ������
		 * @param name
		 */
		 static synchronized private void output3(String name){
			int length = name.length();
				for(int i=0;i<length;i++){
					System.out.print(name.charAt(i));
				}
				System.out.println();
		}
	}
}
