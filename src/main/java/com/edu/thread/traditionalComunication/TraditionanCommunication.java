package com.edu.thread.traditionalComunication;

/**
 * ʵ�����߳�����50�� ִ��50�Σ�
 * ���߳�����20�� ִ��20�Σ�֮���ڽ���ִ��
 * һ������ִ��
 * �����������ѧϰ�����ھۣ���������ķ�ʽ��װ��һ����
 *  synchronized������һ���Ƿ�����Ҫ���ʵ���Դ�ϵ� 
 * @author 
 *
 */
public class TraditionanCommunication {

	public static void main(String[] args)
	{
		final Bussiness bussiness = new Bussiness();
		/*
		 * ���߳�
		 */
		new Thread(new Runnable(){
			@Override
			public void run(){
				for(int i=1;i<=50;i++){
					bussiness.sub(i);
				}
			}
		}).start();
		
		/*
		 * ���߳�
		 */
		for(int i=1;i<=50;i++){
			bussiness.main(i);
		}
	}
}

/**
 * ҵ����
 * @author 
 *
 */
class Bussiness{
	private boolean beShouldSub = true;
	synchronized public void sub(int i){
		//ʹ��while�����if ���Ա��ⱻ�ٻ��ѵ���� 
		while(!beShouldSub){
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for(int j=1;j<=20;j++){
			System.out.println("sub "+i+"��sequence" +j+"+++++++++++++++++++++++");
		}
		beShouldSub = false;
		this.notify(); //֪ͨһ����wait ���߳�
	}
	synchronized public void main(int i){
		//ʹ��while�����if ���Ա��ⱻ�ٻ��ѵ����
		while(beShouldSub){
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for(int j=1;j<=100;j++){
			System.out.println("main "+i+"��sequence" +j);
		}
		beShouldSub = true;
		this.notify();
	}
}


