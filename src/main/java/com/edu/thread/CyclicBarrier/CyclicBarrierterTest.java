package com.edu.thread.CyclicBarrier;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CyclicBarrierterTest {  
  
    public static void main(String[] args) {  
        ExecutorService service = Executors.newCachedThreadPool();  
        final  CyclicBarrier cb = new CyclicBarrier(3);  
        for(int i=0;i<3;i++){  
            Runnable runnable = new Runnable(){  
                    public void run(){  
                    try {  
                        Thread.sleep((long)(Math.random()*10000));    
                        System.out.println("线程" + Thread.currentThread().getName() +   
                                "当前已有" + (cb.getNumberWaiting()+1) + "个选手就位，" + (cb.getNumberWaiting()==2?"选手都就位了，裁判开始比赛":"等候裁判开始比赛"));                         
                        cb.await();  
                          
                        Thread.sleep((long)(Math.random()*10000));    
                        System.out.println("线程" + Thread.currentThread().getName() +   
                                "当前已有" + (cb.getNumberWaiting()+1) + "个选手完成比赛" + (cb.getNumberWaiting()==2?"裁判宣布-比赛结束":"等待比赛结束中……"));  
                        cb.await();                         
                    } catch (Exception e) {  
                        e.printStackTrace();  
                    }                 
                }  
            };  
            service.execute(runnable);  
        }  
        service.shutdown();  
    }  
}  