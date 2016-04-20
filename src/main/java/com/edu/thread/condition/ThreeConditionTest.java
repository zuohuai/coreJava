package com.edu.thread.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 3 ���߳̽�������
 * @author zuohuai
 *
 */
public class ThreeConditionTest {

	public static void main(String[] args)
	{
		final Bussiness bussiness = new Bussiness();
		/*
		 * ���߳�1
		 */
		new Thread(new Runnable(){
			@Override
			public void run(){
				for(int i=1;i<=20;i++){
					bussiness.sub1(i);
				}
			}
		}).start();
		
		/*
		 * ���߳�2
		 */
		new Thread(new Runnable(){
			@Override
			public void run(){
				for(int i=1;i<=20;i++){
					bussiness.sub2(i);
				}
			}
		}).start();
		
		/*
		 * ���߳�
		 */
		for(int i=1;i<=20;i++){
			bussiness.main(i);
		}
	}
	
	/**
	 * ҵ����
	 * @author zuohuai
	 *
	 */
	static class Bussiness{
		private Lock loc = new ReentrantLock();
		private Condition condition1 = loc.newCondition();
		private Condition condition2 = loc.newCondition();
		private Condition condition3 = loc.newCondition();
		int flag = 1 ; //���߳����еı�־�ֱ��� 1,2,3
		
		 public void sub1(int i){
			 loc.lock();
			 try{
			//condition Ҳ������ٻ��ѣ�������Ҫʹ�ã�ʹ��while�����if ���Ա��ⱻ�ٻ��ѵ����
			while(flag!=2){
				try {
					condition2.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
				for(int j=1;j<=20;j++){
					System.out.println("sub1 "+i+"��sequence" +j);
				}
				flag = 3;
				condition2.signal();
			 }finally{
				loc.unlock(); 
			 }
		}
		 
		 public void sub2(int i){
			 loc.lock();
			 try{
			//condition Ҳ������ٻ��ѣ�������Ҫʹ�ã�ʹ��while�����if ���Ա��ⱻ�ٻ��ѵ����
			while(flag!=3){
				try {
					condition2.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
				for(int j=1;j<=30;j++){
					System.out.println("sub2 "+i+"��sequence" +j);
				}
				flag = 1;
				condition1.signal();
			 }finally{
				loc.unlock(); 
			 }
		}
		 
		 public void main(int i){
			 loc.lock();
			 try{
			//ʹ��while�����if ���Ա��ⱻ�ٻ��ѵ����
			while(flag!= 1){
				try {
					condition1.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
				for(int j=1;j<=50;j++){
					System.out.println("main "+i+"��sequence" +j);
				}
				flag = 2;
				condition2.signal();
			 }finally{
				 loc.unlock();
			 }
		}
	}
}




