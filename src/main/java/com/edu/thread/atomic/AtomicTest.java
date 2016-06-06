package com.edu.thread.atomic;

import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import scala.actors.threadpool.AtomicInteger;

public class AtomicTest {

	/**
	 * 原子更新基本类型类
	 * AtomicBoolean
	 * AtomicInteger
	 * AtomicLong
	 */
	@Test
	public void test_atomicInteger() throws Exception{
		AtomicInteger result = new AtomicInteger(100);
		System.out.println(result.addAndGet(100));
		System.out.println(result.get());
	}
	
	/**
	 * 原子更新数组类
	 * AtomicIntegerArray
	 * AtomicLongArray
	 * AtomicReferenceArray
	 * @throws Exception
	 */
	@Test
	public void test_atomicIngterArray() throws Exception{
		int[] value = new int[] { 1, 2 };
		AtomicIntegerArray ai = new AtomicIntegerArray(value);
		ai.getAndSet(0, 3);
		
	    System.out.println(ai.get(0));
	    System.out.println(value[0]);
	}
	
	/**
	 * 原子更新引用类型
	 * AtomicReference：原子更新引用类型。
	 * AtomicReferenceFieldUpdater：原子更新引用类型里的字段。
	 * AtomicMarkableReference：原子更新带有标记位的引用类型。
	 * @throws Exception
	 */
	@Test
	public void test_atomicReference() throws Exception{
		AtomicReference<User> atomicUserRef = new AtomicReference<User>();
		
		User user = new User("conan", 15);
        atomicUserRef.set(user);
        
        User updateUser = new User("Shinichi", 17);
        atomicUserRef.compareAndSet(user, updateUser);
        System.out.println(atomicUserRef.get().getName());
        System.out.println(atomicUserRef.get().getOld());

	}
	
	/**
	 * 原子修改某个字段
	 * @throws Exception
	 */
	@Test
	public void test_atomicIntegerFieldUpdater() throws Exception{
		 AtomicIntegerFieldUpdater<User> a = AtomicIntegerFieldUpdater.newUpdater(User.class, "old");
		 User conan = new User("conan", 10);
		 System.out.println(a.getAndIncrement(conan));
		 System.out.println(a.get(conan));

	}
	
	class User{
		private String name;
		public volatile  int old;
		
		public User(String name, int old){
			this.name = name;
			this.old = old;
		}
		
		public String getName() {
			return name;
		}
		
		public int getOld() {
			return old;
		}
	}
}
