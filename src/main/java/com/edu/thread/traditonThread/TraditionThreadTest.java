package com.edu.thread.traditonThread;


public class TraditionThreadTest {

	public static void main(String[] args){
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
