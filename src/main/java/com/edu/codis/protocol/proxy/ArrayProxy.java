package com.edu.codis.protocol.proxy;

import io.netty.buffer.ByteBuf;

import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.Array;

import com.edu.codis.protocol.Context;
import com.edu.codis.protocol.Types;
import com.edu.codis.protocol.exception.UnknowSignalException;
import com.edu.codis.protocol.exception.WrongTypeException;

public class ArrayProxy extends AbstractProxy<Object> {

	@Override
	public Object getValue(Context ctx, byte flag) throws IOException {
		ByteBuf in = ctx.getBuffer();
		byte type = getFlagTypes(flag);
		if (type != Types.ARRAY) {
			throw new WrongTypeException(Types.ARRAY, type);
		}

		byte signal = getFlagSignal(flag);
		if (signal == 0x00) {
			// #### 0000
			byte tag = in.readByte();
			int len = readVarInt32(in, tag);
			if (in.readableBytes() < len) {
				throw new EOFException();
			}
			Object[] result = new Object[len];
			// 加入引用表
			ctx.putObjectRef(result);
			for (int i = 0; i < len; i++) {
				byte fValue = in.readByte();
				Object value = ctx.getValue(fValue);
				result[i] = value;
			}
			return result;
		} else if (signal == 0x01) {
			// #### 0001
			byte tag = in.readByte();
			int ref = readVarInt32(in, tag);
			Object[] result = (Object[]) ctx.getObjectRef(ref);
			return result;
		}
		throw new UnknowSignalException(type, signal);
	}

	@Override
	public void setValue(Context ctx, Object value) throws IOException {
		ByteBuf out = ctx.getBuffer();
		byte flag = Types.ARRAY;
		int ref = ctx.getObjectRef(value);
		if (ref > 0) {
			// #### 0001
			flag |= 0x01;
			out.writeByte(flag);
			putVarInt32(out, ref);
		} else {
			// 加入引用表
			ctx.putObjectRef(value);

			// #### 0000
			out.writeByte(flag);
			int len = Array.getLength(value);
			putVarInt32(out, len);
			for (int i = 0; i < len; i++) {
				Object obj = Array.get(value, i);
				ctx.setValue(obj);
			}
		}
	}

}
