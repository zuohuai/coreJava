package com.edu.codis.protocol.proxy;

import io.netty.buffer.ByteBuf;

import java.io.EOFException;
import java.io.IOException;

import com.edu.codis.protocol.Context;
import com.edu.codis.protocol.Types;
import com.edu.codis.protocol.exception.WrongTypeException;

public class BytesProxy extends AbstractProxy<byte[]> {

	@Override
	public byte[] getValue(Context ctx, byte flag) throws IOException {
		ByteBuf in = ctx.getBuffer();
		byte type = getFlagTypes(flag);
		if (type != Types.BYTE_ARRAY) {
			throw new WrongTypeException(Types.BYTE_ARRAY, type);
		}

		// byte signal = getFlagSignal(flag);
		// if (signal == 0x00) {
		// #### 0000
		byte tag = in.readByte();
		int len = readVarInt32(in, tag);
		if (in.readableBytes() < len) {
			throw new EOFException();
		}
		byte[] result = new byte[len];
		in.readBytes(result);
		return result;
		// }
		// throw new WrongTypeException();
	}

	@Override
	public void setValue(Context ctx, byte[] value) throws IOException {
		ByteBuf out = ctx.getBuffer();
		byte flag = Types.BYTE_ARRAY;
		// #### 0000
		out.writeByte(flag);
		int len = value.length;
		putVarInt32(out, len);
		out.writeBytes(value);
	}
}
