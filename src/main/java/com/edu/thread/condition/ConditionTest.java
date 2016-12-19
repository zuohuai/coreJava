package com.edu.thread.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionTest {

	public static void main(String[] args) {
		final Bussiness bussiness = new Bussiness();

		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 1; i <= 3; i++) {
					bussiness.sub1(i);
				}
			}
		}).start();

		for (int i = 1; i <= 4; i++) {
			bussiness.main(i);
		}
	}
}

class Bussiness {
	private boolean beShouldSub = true;
	private Lock loc = new ReentrantLock();
	private Condition condition = loc.newCondition();

	public void sub1(int i) {
		loc.lock();
		try {
			while (!beShouldSub) {
				try {
					condition.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			for (int j = 1; j <= 20; j++) {
				System.out.println("sub :" + i + "sequence" + j);
			}
			beShouldSub = false;
			condition.signal();
		} finally {
			loc.unlock();
		}
	}

	public void sub2(int i) {
		loc.lock();
		try {
			while (!beShouldSub) {
				try {
					condition.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			for (int j = 1; j <= 20; j++) {
				System.out.println("sub " + i + "sequence" + j);
			}
			beShouldSub = false;
			condition.signal();
		} finally {
			loc.unlock();
		}
	}

	public void main(int i) {
		loc.lock();
		try {
			while (beShouldSub) {
				try {
					condition.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			for (int j = 1; j <= 100; j++) {
				System.out.println("main " + i + ":sequence" + j);
			}
			beShouldSub = true;
			condition.signal();
		} finally {
			loc.unlock();
		}
	}
}
