package com.edu.thread.traditonThread;

/**
 * ��ͳ�����̵߳�2�ַ�ʽ
 * Thread ִ�з��� ����û�и���Thread����ʱ�����û�и���
 * @author 
 *
 */
public class TraditionThreadTest {

	public static void main(String[] args){
		/**
		 * Thread ������
		 */
		Thread t1 = new Thread(){
			public void run(){
				while(true){
					try {
						Thread.sleep(500);
						System.out.println("1:"+Thread.currentThread().getName());
						System.out.println("1:"+this.getName()+":***");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		t1.start();
		
		/**
		 *ʵ��Runnable�ӿ�
		 */
		Thread t2 = new Thread(new Runnable(){
			@Override
			public void run(){
				while(true){
					try {
						Thread.sleep(500);
						System.out.println("2:"+Thread.currentThread().getName());
						//System.out.println("1:"+this.getName()+":***");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		t2.start();
		
		/**
		 * ���е���Thread�����е�run()����
		 * ������Runnable�е�run()����
		 */
		new Thread(new Runnable(){
			@Override
			public void run() {
				while(true){
					try {
						Thread.sleep(500);
						System.out.println("Runnable:"+Thread.currentThread().getName());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}}){
			public void run(){
				while(true){
					try {
						Thread.sleep(500);
						System.out.println("Thread:"+Thread.currentThread().getName());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}
}
