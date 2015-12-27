package com.edu.mapEditor;


import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.my9yu.common.console.Console;

public class MainMapEditor {
	/** 默认的上下文配置名 */
	private static final String DEFAULT_APPLICATION_CONTEXT = "applicationContext-mapEditor.xml";

	public static void main(String[] arguments) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(DEFAULT_APPLICATION_CONTEXT);
		try {
			Console console = new Console(applicationContext);
			console.start();
		} catch (Exception exception) {
			exception.printStackTrace();
			if (applicationContext.isRunning()) {
				applicationContext.destroy();
			}
		}
	}
}
