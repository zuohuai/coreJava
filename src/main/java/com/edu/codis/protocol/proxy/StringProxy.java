package com.edu.codis.protocol.proxy;

import io.netty.buffer.ByteBuf;

import java.io.EOFException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.edu.codis.protocol.Context;
import com.edu.codis.protocol.Types;
import com.edu.codis.protocol.exception.UnknowSignalException;
import com.edu.codis.protocol.exception.WrongTypeException;
import com.edu.utils.QuickLZUtils;


public class StringProxy extends AbstractProxy<String> {

	public static String CHARSET = "UTF-8";

	/**
	 * 是否对自动压缩字符串
	 */
	private boolean autoCompress = false;
	private int autoSize = 64;

	public boolean isAutoCompress() {
		return autoCompress;
	}

	public void setAutoCompress(boolean autoCompress) {
		this.autoCompress = autoCompress;
	}

	public int getAutoSize() {
		return autoSize;
	}

	public void setAutoSize(int autoSize) {
		this.autoSize = autoSize;
	}

	public String getValue(Context ctx, byte flag) throws IOException {
		ByteBuf in = ctx.getBuffer();
		byte type = getFlagTypes(flag);
		if (type != Types.STRING) {
			throw new WrongTypeException(Types.STRING, type);
		}

		byte signal = getFlagSignal(flag);
		if (signal == 0x00) {
			// #### 0000
			byte tag = in.readByte();
			int len = readVarInt32(in, tag);
			if (in.readableBytes() < len) {
				throw new EOFException();
			}
			byte[] buf = new byte[len];
			in.readBytes(buf);
			String result = new String(buf, CHARSET);
			// 添加到字符串表
			ctx.putStringRef(result);
			return result;
		} else if (signal == 0x01) {
			// #### 0001
			byte tag = in.readByte();
			int ref = readVarInt32(in, tag);
			String result = ctx.getStringRef(ref);
			return result;
		} else if (signal == 0x02) {
			// #### 0010
			byte tag = in.readByte();
			int len = readVarInt32(in, tag);
			if (in.readableBytes() < len) {
				throw new EOFException();
			}
			byte[] buf = new byte[len];
			in.readBytes(buf);
			// 压缩的字符串
			byte[] unzip = QuickLZUtils.unzip(buf, 30, TimeUnit.SECONDS);
			String result = new String(unzip, CHARSET);
			// 添加到字符串表
			ctx.putStringRef(result);
			return result;
		}
		throw new UnknowSignalException(type, signal);
	}

	public void setValue(Context ctx, String value) throws IOException {
		ByteBuf out = ctx.getBuffer();
		byte flag = Types.STRING;
		int ref = ctx.getStringRef(value);
		if (ref > 0) {
			// #### 0001
			flag |= 0x01;
			out.writeByte(flag);
			putVarInt32(out, ref);
		} else {
			// 加入引用表
			ctx.putStringRef(value);

			byte[] bytes = value.getBytes(CHARSET);
			if (isAutoCompress() && bytes.length > getAutoSize()) {
				flag |= 0x02;
				bytes = QuickLZUtils.zip(bytes);
			}
			out.writeByte(flag);

			int len = bytes.length;
			putVarInt32(out, len);
			out.writeBytes(bytes);
		}
	}

}
