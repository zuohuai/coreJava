package com.edu.codis.protocol.proxy;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import com.edu.codis.protocol.Context;
import com.edu.codis.protocol.Types;
import com.edu.codis.protocol.exception.WrongTypeException;

public class CollectionProxy extends AbstractProxy<Collection<?>> {

	@Override
	public Collection<?> getValue(Context ctx, byte flag) throws IOException {
		byte type = getFlagTypes(flag);
		if (type != Types.COLLECTION) {
			throw new WrongTypeException(Types.COLLECTION, type);
		}
		byte signal = getFlagSignal(flag);

		// 读取数组
		byte arrayFlag = (byte) (Types.ARRAY | signal);
		Object[] array = (Object[]) ctx.getValue(arrayFlag);
		return Arrays.asList(array);
	}

	@Override
	public void setValue(Context ctx, Collection<?> value) throws IOException {
		ByteBuf out = ctx.getBuffer();
		byte flag = Types.COLLECTION;
		Object[] array = value.toArray();
		int ref = ctx.getObjectRef(array);
		if (ref > 0) {
			// #### 0001
			flag |= 0x01;
			out.writeByte(flag);
			putVarInt32(out, ref);
		} else {
			// 加入引用表
			ctx.putObjectRef(array);

			// #### 0000
			out.writeByte(flag);
			int len = array.length;
			putVarInt32(out, len);
			for (int i = 0; i < len; i++) {
				Object obj = array[i];
				ctx.setValue(obj);
			}
		}
	}
	
	

}
