package com.edu.mapEditor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainMapEditor implements Runnable {
	/** 默认的上下文配置名 */
	private static final String DEFAULT_APPLICATION_CONTEXT = "applicationContext-mapEditor.xml";

	public static void main(String[] arguments) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
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

	@Override
	public void run() {
		while (true) {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					System.in));
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
