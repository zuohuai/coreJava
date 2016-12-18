package com.edu.thread.traditonThread;

public class TheadYieldTest {

	public static void main(String[] args) throws Exception{

		Thread t1 = new Thread(new TestYieldRunner(true), "有礼貌的线程one");
		Thread t2 = new Thread(new TestYieldRunner(false), "无礼貌的线程two");
		
		t1.start();
		t2.start();
		
		Thread.sleep(1000L);
		
		t1.interrupt();
		t2.interrupt();
	}
}

class TestYieldRunner implements Runnable {

	private final boolean flag;

	public TestYieldRunner(boolean flag) {
		this.flag = flag;
	}

	@Override
	public void run() {
		int i = 0;
		while (!Thread.currentThread().isInterrupted()) {
			i++;
			if (i % 10 == 0) {
				System.out.println(Thread.currentThread().getName() + ":" + i);
				if (flag) {
					//交出CPU资源
					Thread.yield();
				}
			}
		}
	}

}
