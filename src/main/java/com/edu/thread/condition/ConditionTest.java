package com.edu.thread.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * �ڵȴ� Condition ʱ������������ٻ��ѡ�����ͨ����Ϊ�Ի���ƽ̨������ò���
 * ���ڴ����Ӧ�ó����������ʵ��Ӱ���С��
 * ��Ϊ Condition Ӧ��������һ��ѭ���б��ȴ��������������ȴ���״̬������
 * ĳ��ʵ�ֿ��������Ƴ����ܵ���ٻ��ѣ�������Ӧ�ó������Ա���Ǽٶ���Щ��ٻ��ѿ��ܷ�����
 * ���������һ��ѭ���еȴ���
	һ�����ڲ������ж��Condition�����ж�·�ȴ���֪ͨ��
	���Բο�jdk1.5�ṩ��Lock��Conditionʵ�ֵĿ��������е�Ӧ�ð�����
	���г���Ҫ��ζ�㷨����Ҫ��ζ�������ķ�װ��
	�ڴ�ͳ���̻߳�����һ��������������ֻ����һ·�ȴ���֪ͨ��Ҫ��ʵ�ֶ�·�ȴ���֪ͨ��
	����Ƕ��ʹ�ö��ͬ�����������󡣣����ֻ��һ��Condition�������ŵĶ��ڵȣ�
	һ��һ���ŵĽ�ȥ�ˣ���ô��֪ͨ���ܻᵼ����һ���Ž��������ߡ���
 * ʵ�����߳�����50�� ִ��50�Σ����߳�����20�� ִ��20�Σ�֮���ڽ���ִ��
 * һ������ִ��50��
 * @author zuohuai
 *
 */
public class ConditionTest {

	public static void main(String[] args)
	{
		final Bussiness bussiness = new Bussiness();
		/*
		 * ���߳�
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
		 * ���߳�
		 */
		for(int i=1;i<=20;i++){
			bussiness.main(i);
		}
	}
}

/**
 * ҵ����
 * @author zuohuai
 *
 */
class Bussiness{
	private boolean beShouldSub = true;
	private Lock loc = new ReentrantLock();
	private Condition condition = loc.newCondition();
	 public void sub1(int i){
		 loc.lock();
		 try{
		//condition Ҳ������ٻ��ѣ�������Ҫʹ�ã�ʹ��while�����if ���Ա��ⱻ�ٻ��ѵ����
		while(!beShouldSub){
			try {
				condition.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
			for(int j=1;j<=20;j++){
				System.out.println("sub "+i+"��sequence" +j);
			}
			beShouldSub = false;
			condition.signal();
		 }finally{
			loc.unlock(); 
		 }
	}
	 
	 public void sub2(int i){
		 loc.lock();
		 try{
		//condition Ҳ������ٻ��ѣ�������Ҫʹ�ã�ʹ��while�����if ���Ա��ⱻ�ٻ��ѵ����
		while(!beShouldSub){
			try {
				condition.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
			for(int j=1;j<=20;j++){
				System.out.println("sub "+i+"��sequence" +j);
			}
			beShouldSub = false;
			condition.signal();
		 }finally{
			loc.unlock(); 
		 }
	}
	 
	 public void main(int i){
		 loc.lock();
		 try{
		//ʹ��while�����if ���Ա��ⱻ�ٻ��ѵ����
		while(beShouldSub){
			try {
				condition.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
			for(int j=1;j<=100;j++){
				System.out.println("main "+i+"��sequence" +j);
			}
			beShouldSub = true;
			condition.signal();
		 }finally{
			 loc.unlock();
		 }
	}
}


