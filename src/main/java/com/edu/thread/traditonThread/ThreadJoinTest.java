package com.edu.thread.traditonThread;

public class ThreadJoinTest {

	public static void main(String[] args) throws Exception {
		Thread t1 = new Thread(new JoinRuner(), "线程one");

		t1.start();
		t1.join();
		for (int i = 0; i < 1000000; i++) {
			System.out.println(Thread.currentThread().getName() + ":" + i);
		}
	}
}

class JoinRuner implements Runnable {

	@Override
	public void run() {
		int i = 0;
		while (!Thread.currentThread().isInterrupted()) {
			i++;
			if (i % 10 == 0) {
				System.out.println(Thread.currentThread().getName() + ":" + i);
			}
		}
	}

}