package com.edu.codis.protocol.proxy;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import com.edu.codis.protocol.Context;
import com.edu.codis.protocol.Types;

public class NullProxy extends AbstractProxy<Object> {

	@Override
	public Object getValue(Context ctx, byte flag) throws IOException {
		// 0000 0001 (1 - 0x01)
		return null;
	}

	@Override
	public void setValue(Context ctx, Object value) throws IOException {
		ByteBuf out = ctx.getBuffer();
		byte flag = Types.NULL;
		out.writeByte((byte) flag);
	}
}
