package com.edu.thread.timmerTradition;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * ��ͳ�Ķ�ʱ��
 * @author 
 *
 */
public class TimmerTraditionTest {
	private static int count =0;
	public static void main(String[] args){
		/**
		 * ʵ�� 1��֮��ը��֮��ÿ��2��ըһ��
		 */
//		new Timer("��һ����ʱ��").schedule(new TimerTask() {
//			@Override
//			public void run() {
//				System.out.println("bomb����");
//			}
//		}, 1000,2000);	
		/**
		 * ʵ�� 2��֮��ը��֮��4��ըһ��
		 * ֮����ѭ�����ϵĲ���
		 */
		class MyTimerTask extends TimerTask{
			@Override
			public void run() {
				count =(count+1)%2;
				System.out.println("bomb����");
				new Timer().schedule(new MyTimerTask(), 2000+2000*count);
			}			
		}
		new Timer("�ڶ�����ʱ��").schedule(new MyTimerTask(),2000);
		while(true){
			System.out.println(new Date().getSeconds());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
