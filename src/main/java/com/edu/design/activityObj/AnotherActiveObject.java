package com.edu.design.activityObj;

import java.util.concurrent.ForkJoinPool;

/**
 * 活动对象jdk1.8
 * @author Administrator
 *
 */
public class AnotherActiveObject {
	private double val; 
	private final ForkJoinPool fj = new ForkJoinPool(1, ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true);
    
    // implementation of active object method
    public void doSomething() throws InterruptedException {
        fj.execute(new Runnable() {
            @Override
            public void run() {
               System.out.println(Thread.currentThread().getName()+"-val:" + Math.random());
            }
        });
    }
 
    // implementation of active object method
    public void doSomethingElse() throws InterruptedException {
        fj.execute(new Runnable() {
            @Override
            public void run() {
                val = 2.0;
            }
        });
    }
    
    public double getVal() {
		return val;
	}
    
    public void setVal(double val) {
		this.val = val;
	}
    
    public static void main(String[] args) throws Exception{
    	AnotherActiveObject activeObject = new AnotherActiveObject();
    	for(int i=0; i<100; i++){
    		activeObject.doSomething();
    	}
    	Thread.sleep(10000);
    }
}
