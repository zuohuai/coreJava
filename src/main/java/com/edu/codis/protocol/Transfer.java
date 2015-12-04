package com.edu.codis.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Transfer {
	protected Logger log = LoggerFactory.getLogger(getClass());

	private Definition definition;

	public void register(Class<?> clz, int index) {
		if (log.isDebugEnabled()) {
			log.debug("注册传输对象类型 [{}]", clz);
		}
		definition.register(clz, index);
	}

	/**
	 * 获取消息定义
	 */
	public byte[] getDescription() {
		return definition.getDescription();
	}

	/**
	 * 获取消息定义
	 */
	public void setDescribe(byte[] bytes) throws IOException {
		definition.setDescribe(bytes);
	}

	/**
	 * 获取消息定义MD5串
	 */
	public String getMD5Description() {
		return definition.getDescriptionMD5();
	}

	/**
	 * 对象编码
	 * @param value
	 * @return
	 * @throws IOException
	 */
	public ByteBuf encode(ByteBufAllocator alloc, Object value) throws IOException {
		long start = System.currentTimeMillis();
		ByteBuf buf = alloc.buffer();
		Context ctx = build(buf);
		ctx.setValue(value);
		long end = System.currentTimeMillis();
		log.error("protocol编码耗时:" + (end - start));
		return buf;
	}

	/**
	 * 对象解码
	 * @param buf
	 * @return
	 * @throws IOException
	 */
	public Object decode(ByteBuf buf) throws IOException {
		long start = System.currentTimeMillis();
		Context ctx = build(buf);
		if (!buf.isReadable()) {
			// return null;
			throw new EOFException("Empty ByteBuffer...");
		}
		byte flag = buf.readByte();
		Object result = ctx.getValue(flag);
		long end = System.currentTimeMillis();
		log.error("protocol解码耗时:" + (end - start));
		return result;
	}

	/**
	 * 对象解码
	 * @param buf
	 * @param type
	 * @return
	 * @throws IOException
	 */
	public <T> T decode(ByteBuf buf, Type type) throws IOException {
		Context ctx = build(buf);
		if (!buf.isReadable()) {
			// return null;
			throw new EOFException("Empty ByteBuffer...");
		}
		byte flag = buf.readByte();
		return ctx.getValue(flag, type);
	}

	/**
	 * 对象类型转换
	 * @param value
	 * @param type
	 * @return
	 * @throws IOException
	 */
	public <T> T convert(Object value, Type type) throws IOException {
		return definition.convert(value, type);
	}

	// ----------

	private Context build(ByteBuf buffer) {
		Context ctx = new Context(buffer, definition);
		return ctx;
	}

	// ----------

	public Transfer() {
		this.definition = new Definition();
	}

	public Transfer(byte[] description) throws IOException {
		// 类型注册
		this.definition = new Definition(description);
	}

	public Transfer(Collection<Class<?>> clzs, int startIndex) {
		// 类型注册
		this.definition = new Definition(clzs, startIndex);
	}

}
