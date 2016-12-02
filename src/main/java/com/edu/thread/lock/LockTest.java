package com.edu.thread.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class LockTest {
//	private static int a = 0; 
	public static void main(String[] args){
		new LockTest().init();
	}
	
	public void init(){
		 final Outputer outputer = new Outputer(); 
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

	 static class Outputer {
	
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
		

		synchronized private void output2(String name){
			int length = name.length();
				for(int i=0;i<length;i++){
					System.out.print(name.charAt(i));
					System.out.println();
			}
		}

		 static synchronized private void output3(String name){
			int length = name.length();
				for(int i=0;i<length;i++){
					System.out.print(name.charAt(i));
				}
				System.out.println();
		}
	}
}
