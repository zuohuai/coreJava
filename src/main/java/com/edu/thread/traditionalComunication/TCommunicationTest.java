package com.edu.thread.traditionalComunication;

/**
 * ʵ�� ���߳�����50�� ���߳�����20�� �Ľ�������
 * @author zuohuai
 */
public class TCommunicationTest {

	public static void main(String[] args) {
		final MyBussiness bussiness = new MyBussiness();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					for (int i = 1; i < 20; i++) {
						bussiness.sub(i);
					}
				} catch (Exception e) {

				}

			}
		}).start();

		new Thread(new Runnable() {
			public void run() {
				try {
					for (int i = 1; i < 50; i++) {
						bussiness.main(i);
					}
				} catch (Exception e) {

				}
			}
		}).start();
	}
}

class MyBussiness {
	private boolean beShouldSub = false;

	MyBussiness() {

	}

	public synchronized void sub(int i) {
		while (!beShouldSub) {
			try {
				this.wait();
			} catch (InterruptedException e) {

			}
		}

		for (int j = 0; j < 5; j++) {
			try {
				System.out.println("sub: " + i + " is running");
				Thread.sleep(500);
			} catch (InterruptedException e) {

			}

		}
		beShouldSub = false;
		this.notify();
	}

	public synchronized void main(int i) {
		while (beShouldSub) {
			try {
				this.wait();
			} catch (InterruptedException e) {

			}
		}
		for (int j = 0; j < 1; j++) {
			try {
				System.out.println("main: " + i + " is running");
				Thread.sleep(500);
			} catch (InterruptedException e) {

			}
		}
		beShouldSub = true;
		this.notify();
	}
}