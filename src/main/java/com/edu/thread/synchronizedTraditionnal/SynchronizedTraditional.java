package com.edu.thread.synchronizedTraditionnal;


public class SynchronizedTraditional {
//	private static int a = 0; 
	public static void main(String[] args){
		new SynchronizedTraditional().init();
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
						outputer.output1("Jacking");
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
						outputer.output3("zhangzuohuai");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	 static class Outputer {
		private void output1(String name){
			//System.out.println(a);
			int length = name.length();
			synchronized (/*this*/Outputer.class) {
				for(int i=0;i<length;i++){
					System.out.print(name.charAt(i));
				}
				System.out.println();
			}
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
