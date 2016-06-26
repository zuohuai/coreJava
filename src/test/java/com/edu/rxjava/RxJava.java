package com.edu.rxjava;


import org.junit.Test;

import rx.Observable;
import rx.Subscriber;

/**
 * https://github.com/ReactiveX/RxJava/wiki
 * http://gank.io/post/560e15be2dca930e00da1083
 * @author administrat
 *
 */
public class RxJava {

	@Test
	public void test_RxJava_start() throws Exception{
	
		//1.创建一个Observable对象
		Observable<String> myObservable = Observable.create( 
			    new Observable.OnSubscribe<String>() {  
			        @Override  
			        public void call(Subscriber<? super String> sub) { 
			        	int i = 1;
			        	if(i == 1){
				        	throw new RuntimeException("运行时候，出现异常！！！");
			        	}
			        	sub.onNext("Hello, world!");  
			            sub.onCompleted();  
			        }  
			    }  
			);
		
		//2.创建一个Subscriber对象
		Subscriber<String> mySubscriber = new Subscriber<String>() {  
		    @Override  
		    public void onNext(String s) { 
		    	System.out.println(s); 
		    }  
		  
		    @Override  
		    public void onCompleted() { 
		    	
		    }  
		  
		    @Override  
		    public void onError(Throwable e) {
		    	System.out.println("出现异常+++");
		    }  
		};  
		
		//3.mySubscriber订阅了myObservable
		myObservable.subscribe(mySubscriber); 
	}
}
