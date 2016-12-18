package com.edu.thread.traditonThread;

/**
 * 用一个对象控制多个线程
 * 
 * @author zuohuai
 *
 */
public class ThreadGroupTest {

	public static void main(String[] args) throws Exception {
		ThreadGroup threadGroup = new ThreadGroup("线程组one");

		ThreadGroupTestRunner runner = new ThreadGroupTestRunner();

		Thread t1 = new Thread(threadGroup, runner, "线程one");
		Thread t2 = new Thread(threadGroup, runner, "线程two");

		t1.start();
		t2.start();
		Thread.currentThread().sleep(1000L);
		threadGroup.interrupt();
	}

}

class ThreadGroupTestRunner implements Runnable {

	@Override
	public void run() {
		int i = 0;
		while (!Thread.currentThread().isInterrupted()) {
			if (i++ % 100 == 0) {
				System.out.println(Thread.currentThread().getName() + ":" + i);
			}
		}
	}

}
