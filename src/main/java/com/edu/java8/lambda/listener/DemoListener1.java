package com.edu.java8.lambda.listener;
//定义具体的事件监听器：   
public class DemoListener1 implements DemoListener {   
       public void handleEvent(DemoEvent de) {   
              System.out.println("Inside listener1...");   
              de.say();//回调   
       }   
}   