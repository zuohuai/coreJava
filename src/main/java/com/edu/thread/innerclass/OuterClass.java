package com.edu.thread.innerclass;

/**
 * �ڲ�����ⲿ��Ĳ���
 * @author zuohuai
 *
 */
public class OuterClass {
	public static void main(String[] args){
		
	}
	
	public void init(){
		InnerClass innerClass =  new InnerClass();
	}
	
	class InnerClass{
		public void outer(){
			System.out.println("I'm a innerClass !! ");
		}
	}
}

