package com.edu.codis.protocol.proxy;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.edu.codis.protocol.Context;
import com.edu.codis.protocol.Types;
import com.edu.codis.protocol.exception.UnknowSignalException;
import com.edu.codis.protocol.exception.WrongTypeException;

public class NumberProxy extends AbstractProxy<Number> {

	public static byte INT32 = 0x01;  // 0001
	public static byte INT64 = 0x02;  // 0010
	public static byte FLOAT = 0x03;  // 0011
	public static byte DOUBLE = 0x04; // 0100

	@Override
	public Number getValue(Context ctx, byte flag) throws IOException {
		ByteBuf in = ctx.getBuffer();
		byte type = getFlagTypes(flag);
		if (type != Types.NUMBER) {
			throw new WrongTypeException(Types.NUMBER, type);
		}

		// 0000 #000
		boolean nevigate = ((flag & FLAG_0X08) != 0);

		// 0000 0###
		byte signal = getFlagSignal(flag);
		if (signal == INT32) {
			byte tag = in.readByte();
			int value = readVarInt32(in, tag);
			return nevigate ? -value : value;
		} else if (signal == INT64) {
			byte tag = in.readByte();
			long value = readVarInt64(in, tag);
			return nevigate ? -value : value;
		} else if (signal == FLOAT) {
			float value = in.readFloat();
			return value;
		} else if (signal == DOUBLE) {
			double value = in.readDouble();
			return value;
		}
		throw new UnknowSignalException(type, signal);
	}

	@Override
	public void setValue(Context ctx, Number value) throws IOException {
		ByteBuf out = ctx.getBuffer();
		byte flag = Types.NUMBER;
		if (value instanceof Integer || value instanceof Short
				|| value instanceof Byte || value instanceof AtomicInteger) {
			int v = value.intValue();
			if (v < 0) {
				flag |= FLAG_0X08 | INT32;
				v = -v;
			} else {
				flag |= INT32;
			}
			out.writeByte(flag);
			putVarInt32(out, v);
		} else if (value instanceof Long || value instanceof AtomicLong
				|| value instanceof BigInteger) {
			long v = value.longValue();
			if (v < 0) {
				flag |= FLAG_0X08 | INT64;
				v = -v;
			} else {
				flag |= INT64;
			}
			out.writeByte(flag);
			putVarInt64(out, v);
		} else if (value instanceof Float) {
			float v = value.floatValue();
			flag |= FLOAT;
			out.writeByte(flag);
			out.writeFloat(v);
		} else if (value instanceof Double || value instanceof BigDecimal) {
			double v = value.doubleValue();
			flag |= DOUBLE;
			out.writeByte(flag);
			out.writeDouble(v);
		}
	}

}
