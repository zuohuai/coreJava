package com.edu.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Test;

import com.edu.codis.protocol.Transfer;

public class ProtocalTest {
	private ByteBufAllocator alloc = new PooledByteBufAllocator();

	private ByteBuf buf;

	// 1111 0000
	public static byte TYPE_MASK = (byte) 0xF0;
	// 0000 0111
	public static byte SIGNAL_MASK = (byte) 0x07;
	// 1000 0000 
	public static final int FLAG_0X80 = 0x80;
	// 0000 1000
	public static final int FLAG_0X08 = 0x08;

	@Test
	public void before() {

	}

	@Test
	public void test_data() throws Exception {
		System.out.println(TYPE_MASK);
		System.out.println(Integer.toBinaryString(-16));
		System.out.println(SIGNAL_MASK);
		System.out.println(Integer.toBinaryString(7));
		System.out.println(FLAG_0X80);
		System.out.println(Integer.toBinaryString(128));
		
		System.out.println(FLAG_0X08);
		System.out.println(Integer.toBinaryString(8));
		
		
	}

	/**
	 * number的编码和解码测试
	 * @throws Exception
	 */
	@Test
	public void test_common() throws Exception {
		List<Class<?>> list = new ArrayList<Class<?>>();
		Transfer transfer = new Transfer(list, 0);
		ByteBuf buf = null;
		// buf = transfer.encode(alloc, 1);

		// buf = transfer.encode(alloc, 500);

		// int a = -5000;
		long a = Integer.MAX_VALUE + 1;
		buf = transfer.encode(alloc, a);

		transfer.decode(buf);

		buf.release();
	}

	/**
	 * boolean的编码和解码测试
	 * @throws Exception
	 */
	@Test
	public void test_boolean_atomicBoolean() throws Exception {
		List<Class<?>> list = new ArrayList<Class<?>>();
		Transfer transfer = new Transfer(list, 0);

		boolean flag = false;
		buf = transfer.encode(alloc, flag);

		transfer.decode(buf);

	}

	/**
	 * string的编码和解码测试
	 * @throws Exception
	 */
	@Test
	public void test_string() throws Exception {
		List<Class<?>> list = new ArrayList<Class<?>>();
		Transfer transfer = new Transfer(list, 0);
		String value = "我是一个男人";
		buf = transfer.encode(alloc, value);
		transfer.decode(buf);
	}

	/**
	 * byte编码 和解码测试
	 * @throws Exception
	 */
	@Test
	public void test_byte() throws Exception {
		List<Class<?>> list = new ArrayList<Class<?>>();
		Transfer transfer = new Transfer(list, 0);
		// 0000 0000
		byte value = (byte) 0x0F;
		buf = transfer.encode(alloc, value);
		transfer.decode(buf);
	}

	/**
	 * Collection的编码和解码测试
	 * @throws Exception
	 */
	@Test
	public void test_collection() throws Exception {
		List<Class<?>> list = new ArrayList<Class<?>>();
		Transfer transfer = new Transfer(list, 0);

		List<Integer> value = new LinkedList<Integer>();
		value.add(1);
		buf = transfer.encode(alloc, value);
		transfer.decode(buf);
	}

	/**
	 * Map 编码解码测试
	 * @throws Exception
	 */
	@Test
	public void test_map() throws Exception {
		List<Class<?>> list = new ArrayList<Class<?>>();
		Transfer transfer = new Transfer(list, 0);

		Map<String, Integer> value = new HashMap<String, Integer>();
		value.put("123456", 1);
		buf = transfer.encode(alloc, value);
		transfer.decode(buf);
	}

	/**
	 * Object 编码和解码测试
	 * @throws Exception
	 */
	@Test
	public void test_object() throws Exception {
		List<Class<?>> list = new ArrayList<Class<?>>();
		Transfer transfer = new Transfer(list, 0);
		User value = User.valueOf(1, "张三");
		buf = transfer.encode(alloc, value);
		transfer.decode(buf);
	}

	@After
	public void after() {
		if (buf != null) {
			buf.release();
		}
	}
}
