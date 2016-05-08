package com.edu.math;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.edu.mapEditor.MainMapEditor;

public class RandomNormalvariateTest implements Runnable {

	/** 默认的上下文配置名 */
	private static final String DEFAULT_APPLICATION_CONTEXT = "applicationContext-random.xml";

	public static void main(String[] arguments)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				DEFAULT_APPLICATION_CONTEXT);
		try {
			MainMapEditor mainMapEditor = new MainMapEditor();
			Thread thread = new Thread(mainMapEditor);
			thread.start();
		} catch (Exception exception) {
			exception.printStackTrace();
			if (applicationContext.isRunning()) {
				applicationContext.destroy();
			}
		}
	}

	// 这种方案
	@Override
	public void run() {
		while (true) {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			String line;
			try {
				line = in.readLine();
				System.out.println(line);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}
}
