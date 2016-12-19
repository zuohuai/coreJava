package com.edu.thread.timmerTradition;


import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/** 
 * 传统的定时器 
 * @author  
 * 
 */  
public class TimmerTraditionTest {  
    private static int count =0;  
    public static void main(String[] args){  
        /** 
         * 实现 1秒之后炸，之后，每隔2秒炸一次 
         */  
//      new Timer("第一个定时器").schedule(new TimerTask() {  
//          @Override  
//          public void run() {  
//              System.out.println("bomb……");  
//          }  
//      }, 1000,2000);    
        /** 
         * 实现 2秒之后炸，之后，4秒炸一次 
         * 之后在循环以上的操作 
         */  
        class MyTimerTask extends TimerTask{  
            @Override  
            public void run() {  
                count =(count+1)%2;  
                System.out.println("bomb……");  
                new Timer().schedule(new MyTimerTask(), 2000+2000*count);  
            }             
        }  
        new Timer("第二个定时器").schedule(new MyTimerTask(),2000);  
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