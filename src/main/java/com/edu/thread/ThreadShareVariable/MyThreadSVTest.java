package com.edu.thread.ThreadShareVariable;

/**
 * �ҵ��̹߳����������
 * ����һ�������� 
 * ��������̣߳�һ������ j ��һ���̱߳�����j  ��  1 ��
 * һ���̶߳Ա���  j �� 1
 * @author
 *
 */
public class MyThreadSVTest {
	
	private static MyShareData data = new MyShareData();
	private int j=10;//�̹߳�������
	public static void main(String[] args){
//		new Thread(new Runnable(){
//			public void run(){
//				data.dec();
//			}
//		}).start();
//		new Thread(new Runnable(){
//			public void run(){
//				data.inc();
//			}
//		}).start();
		MyThreadSVTest mt =  new MyThreadSVTest();
		new Thread(mt.new DecThread()).start();
		new Thread(mt.new IncThread()).start();
	}
	
	public synchronized void inc(){
		for(int i=0;i<10;i++){
			j++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName()+" "+j);
		}
	}
	
	public synchronized void dec(){
		for(int i=0;i<10;i++){
			j--;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName()+" "+j);
		}
	}
	
	/**
	 * ʵ�ּ� 1 �������߳�
	 * @author 
	 *
	 */
	class IncThread implements Runnable{

		@Override
		public void run() {
			inc();
		}
		
	}
	
	/**
	 * ʵ�ּ�1 �������߳�
	 * @author 
	 */
	class DecThread implements Runnable{

		@Override
		public void run() {
			dec();
		}
		
	}

}

/**
 * ��Ϊ�̹߳�����ⲿ��
 * @author zuohuai
 *
 */
class MyShareData{
	int j =9;;
	
	/**
	 * ��j �� 1 �Ĳ���
	 */
	public  void inc(){
		while(true){
			j--;
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName()+" "+ j);
		}
	}
	
	/**
	 *��j �� 1 �Ĳ���
	 */
	public  void dec(){
		while(true){
			j++;
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName()+" "+ j);
		}
	}
}
