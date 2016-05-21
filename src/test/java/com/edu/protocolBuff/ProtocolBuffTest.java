package com.edu.protocolBuff;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class ProtocolBuffTest {

	private String protoFPath;
	private static final String PROTOC_PATH = "protoc.exe";

	private String descPath = "decribe.proto";
	{
		protoFPath = System.getProperty("user.dir");

		descPath = protoFPath + File.separator + descPath;

		protoFPath += File.separator + "interface" + File.separator;
	}

	@Test
	public void test_start() throws Exception {
		createDescripFile();
	}

	@Test
	public void test_start2() throws Exception {
		protoFPath = protoFPath + "UserVoPro.proto";
		new ProtoParserOrBuilder(new File(protoFPath));
	}

	private void createDescripFile() {
		try {
			Runtime run = Runtime.getRuntime();
			String cmd = buildDescribeCmd(protoFPath);
			// 如果不正常终止, 则生成desc文件失败
			Process p = run.exec(cmd);
			if (p.waitFor() != 0) {
				if (p.exitValue() == 1) {// p.exitValue()==0表示正常结束，1：非正常结束
					throw new RuntimeException("protoc 编译器报错");
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String buildDescribeCmd(String protoFPath) {
		String cmd = PROTOC_PATH + " --proto_path=" + protoFPath + " --descriptor_set_out=" + descPath + " ";
		List<File> files = search(protoFPath, new LinkedList<>());
		for (File file : files) {
			cmd = cmd + " " + file.getAbsolutePath();
		}
		System.out.println(cmd);
		return cmd;
	}

	private String  buildJavaClass(){
		String cmd = PROTOC_PATH 
	}

	private List<File> search(String dir, final List<File> result) {
		File root = new File(dir);
		File[] files = root.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				search(file.getAbsolutePath(), result);
			} else {
				result.add(file);
			}
		}

		return result;
	}
}
