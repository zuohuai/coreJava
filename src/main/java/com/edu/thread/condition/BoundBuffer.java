package com.edu.thread.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * �������еĹ������
 * @author 
 *
 */
public class BoundBuffer {
	private static final Lock lock = new ReentrantLock();
	private static final Condition notFull = lock.newCondition();
	private static final Condition notEmpty = lock.newCondition();
	
	private static final Object[] items =  new Object[100];
	int putptr,takeptr,count;
	/**
	 * �򻺴����� ��������
	 * @param obj
	 * @throws InterruptedException
	 */
	public void put(Object obj) throws InterruptedException{
		lock.lock();
		try{
			while(count == items.length){
				notFull.await();
			}
			items[putptr]=obj;
			if(++putptr==items.length){
				putptr=0;
			}
			++count;
			notEmpty.signal();
		}finally{
			lock.unlock();
		}
	}
	/**
	 * �ӹ�������ȡ������
	 * @return
	 * @throws InterruptedException
	 */
	public Object get() throws InterruptedException{
		lock.lock();
		try{
			if(count == 0){
				notEmpty.await();
			}
			Object obj = items[takeptr];
			if(++takeptr == items.length){
				takeptr = 0;
			}
			--count;
			notFull.signal();
			return obj;
		}finally{
			lock.unlock();
		}
	}
}
