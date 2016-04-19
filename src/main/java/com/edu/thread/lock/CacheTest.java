package com.edu.thread.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

/**
 * ��ȡ ReentrantReadWriteLock  �е�api ����Ƶ� CachedData 
 * ��д������Ϊ������д����������������⣬������д�����⣬д����д�����⣬
 * ������jvm�Լ����Ƶģ���ֻҪ�Ϻ���Ӧ�������ɡ������Ĵ���ֻ�����ݣ�
 * ���Ժܶ���ͬʱ����������ͬʱд���Ǿ��϶����������Ĵ����޸����ݣ�
 * ֻ����һ������д���Ҳ���ͬʱ��ȡ���Ǿ���д����
 * ��֮������ʱ���϶�����д��ʱ����д����
 * ���ö�д�������һ�׻���ϵͳ
 * @author 
 *
 */
public class CacheTest {
	private Map<String,Object>  map = new HashMap<String,Object>();
	private ReadWriteLock  rwl = new ReentrantReadWriteLock(); //��д��
	private Lock readLock = rwl.readLock();
	private Lock writeLock = rwl.writeLock();
	public static void main(String[] args){
		new CacheTest().read(); 
	}
	
	 void write() {
		// do sth...
		//read();
		 synchronized (this) {
				
			}
	}
	
	 void read(){
		synchronized (this) {
			write();
		}
	}
	
	public Object getData(String key ){
		rwl.readLock().lock();
		try{
			Object value = null;
			if(value == null){
				rwl.readLock().unlock();
				rwl.writeLock().lock();
				try{
						if(value == null){
							value ="data"; //д������
							rwl.writeLock().unlock();
						}

				}finally{
					rwl.writeLock().unlock();
					rwl.readLock().lock();
				}
			}
		}finally{
			rwl.readLock().unlock();
		}
		return null;
	}
}
