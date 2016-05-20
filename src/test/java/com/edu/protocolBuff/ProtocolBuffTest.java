package com.edu.protocolBuff;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class ProtocolBuffTest {

	String path;
	{
		path = System.getProperty("user.dir");
		path += File.separator + "interface";
	}

	@Test
	public void test_start() throws Exception {
		File root = new File(path);
		

		List<File> files = new LinkedList<>();
		if (root.isDirectory()) {
			File[] subFile = root.listFiles();
			files.addAll(Arrays.asList(subFile));
		}
		for (File file : files) {
			ProtoParserOrBuilder builder = new ProtoParserOrBuilder(file);
		}

	}
}
