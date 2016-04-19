package com.edu.thread.ThreadShareVariable;

import java.util.Random;

/**
 * �߳� ��Χ�ڵĹ������
 * ������˼�ǣ����̷߳�Χ�ڣ���ͬ��ģ���ǹ��������
 * �����̷߳�Χ�⣬������
 * ѧϰ������̷߳�Χ�ڹ������ݵ����˼· ,
 * ѧϰӦ���м����߳�������״̬��ThreadDeathEvent ����ôһ���¼���
 * @author zuohuai
 *
 */
public class ThreadShareVariable {
	public static void main(String[] args){
		new ThreadShareVariable().init();
	}
	public void init(){
		for(int i=1;i<=2;i++){
			new Thread(new Runnable(){
				@Override
				public void run(){
					int  data  = new Random().nextInt();
					MyThreadData.getThreadData().setAge(data);
					MyThreadData.getThreadData().setName(data);
					System.out.println(Thread.currentThread().getName()+" has put data ��"+MyThreadData.getThreadData().getName()+" ��"+MyThreadData.getThreadData().getAge());
					new A().get();
					new B().get();
				}
			}).start();
		}
	}
	
	/**
	 * ģ��A
	 * @author zuohuai
	 *
	 */
	static class A {
		public void get(){
			MyThreadData myData = MyThreadData.getThreadData();
			System.out.println("A "+Thread.currentThread().getName()+"  get myData�� "+myData.getName()+" ,"+myData.getAge());
		}
	}
	
	/**
	 * ģ��B
	 * @author zuohuai
	 *
	 */
	static class B{
		public void get(){
			MyThreadData myData = MyThreadData.getThreadData();
			System.out.println("B "+Thread.currentThread().getName()+"  get myData�� "+myData.getName()+" "+myData.getAge());
		}
	}
}

/**
 * �ҵĺ��߳���ص����ݶ��� ,����������������˼��
 * @author zuohuai
 *
 */
class MyThreadData{
	int name;
	int age;
	private static MyThreadData data;
	private static ThreadLocal<MyThreadData> map = new ThreadLocal<MyThreadData>();
	private MyThreadData(){
		
	}
	
	public static  MyThreadData getThreadData(){
		data = map.get();
		if(data == null){
			data = new MyThreadData();
			map.set(data);
		}
		return data;
	}
	public int getName() {
		return name;
	}
	public void setName(int name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
}
