package com.edu.codis.protocol.proxy;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import com.edu.codis.protocol.Context;
import com.edu.codis.protocol.Types;
import com.edu.codis.protocol.exception.UnknowSignalException;
import com.edu.codis.protocol.exception.WrongTypeException;

public class BooleanProxy extends AbstractProxy<Object> {

	@Override
	public Object getValue(Context ctx, byte flag) throws IOException {
		byte type = getFlagTypes(flag);
		if (type != Types.BOOLEAN) {
			throw new WrongTypeException(Types.BOOLEAN, type);
		}

		byte signal = getFlagSignal(flag);
		if (signal == 0x00) {
			return false;
		} else if (signal == 0x01) {
			return true;
		}
		throw new UnknowSignalException(type, signal);
	}

	@Override
	public void setValue(Context ctx, Object value) throws IOException {
		ByteBuf out = ctx.getBuffer();
		byte flag = Types.BOOLEAN;
		if (value instanceof Boolean) {
			Boolean bool = (Boolean) value;
			if (bool) {
				// #### 0001
				flag |= 0x01;
				out.writeByte(flag);
			} else {
				// #### 0000
				out.writeByte(flag);
			}
		} else if (value instanceof AtomicBoolean) {
			AtomicBoolean bool = (AtomicBoolean) value;
			if (bool.get()) {
				// #### 0001
				flag |= 0x01;
				out.writeByte(flag);
			} else {
				// #### 0000
				out.writeByte(flag);
			}
		}
	}

}
