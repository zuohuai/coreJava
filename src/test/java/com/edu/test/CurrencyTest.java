package com.edu.test;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.junit.Test;

public class CurrencyTest {

	@Test
	public void test_currency() throws Exception {
		File file = new File("E:\\git\\coreJava\\src\\test\\java\\com\\edu\\test\\0510_re.txt");
		LineIterator it = FileUtils.lineIterator(file);

		while (it.hasNext()) {
			String line = it.nextLine();
			System.out.println(line.substring(6, 12));
		}
		
	}
}
