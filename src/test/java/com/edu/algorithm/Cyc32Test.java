package com.edu.algorithm;

import java.util.zip.CRC32;

import org.junit.Test;

public class Cyc32Test {

	/**
	 * 优化关系数据库的关联查询的时候，ON的字段选择为数值型，必须是会比字符型的数据快很多的，那么如何将一个字符型的数据，唯一性地转为数值型呢？
	 * 采用cry32
	 * @throws Exception
	 */
	@Test
	public void test_cyc32() throws Exception{
		CRC32 crc32 = new CRC32();
		crc32.update("hello-world".getBytes());
		System.out.println(crc32.getValue());
		
	}
	
	
}
