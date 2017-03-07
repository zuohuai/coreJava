package com.edu.thread.CountDownLatch;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownLatchTest {  
  
    public static void main(String[] args) {  
        ExecutorService service = Executors.newCachedThreadPool();  
        final CountDownLatch cdOrder = new CountDownLatch(1);  
        final CountDownLatch cdAnswer = new CountDownLatch(3);        
        for(int i=0;i<3;i++){  
            Runnable runnable = new Runnable(){  
                    public void run(){  
                    try {  
                        System.out.println("线程" + Thread.currentThread().getName() +   
                                "选手就位");                       
                        cdOrder.await();  
                        System.out.println("线程" + Thread.currentThread().getName() +   
                        "选手开始跑++++");                                 
                        Thread.sleep((long)(Math.random()*10000));    
                        System.out.println("线程" + Thread.currentThread().getName() +   
                                "选手跑完了");                          
                        cdAnswer.countDown();                         
                    } catch (Exception e) {  
                        e.printStackTrace();  
                    }                 
                }  
            };  
            service.execute(runnable);  
        }         
        try {  
            Thread.sleep((long)(Math.random()* 10000));                        
            cdOrder.countDown();  
            System.out.println("线程" + Thread.currentThread().getName() +   
            "裁判开始比赛");      
            cdAnswer.await();  
            System.out.println("线程" + Thread.currentThread().getName() +   
            "裁判宣布比赛结束");     
        } catch (Exception e) {  
            e.printStackTrace();  
        }                 
        service.shutdown();  
    }  
} 