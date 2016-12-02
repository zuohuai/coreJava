package com.edu.thread.Semaphore;

import java.util.concurrent.Semaphore;


public class SemaphoreABC {

	public static void main(String[] args){
		Semaphore aSem = new Semaphore(1);  
        Semaphore bSem = new Semaphore(0);  
        Semaphore cSem = new Semaphore(0); 
        
        T a = new T("A",aSem,bSem);  
        T b = new T("B",bSem,cSem);  
        T c = new T("C",cSem,aSem);
        
        a.start();  
        b.start();  
        c.start(); 
        
        
	}
	
	static class T extends Thread{
		Semaphore me = null ;
		Semaphore next = null ;
		
		T(String name,Semaphore me,Semaphore next ){
			super(name);
			this.me = me ;
			this.next = next ;
		}
		
		public void run(){
			for(int i = 0 ; i < 2 ; i ++){  
                try {  
                    me.acquire();  
                } catch (InterruptedException e) {  
                    e.printStackTrace();  
                }  
                System.out.print(this.getName());  
                next.release();  
            }  
		}
	}
}
