package com.edu.codis.protocol.proxy;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.edu.codis.protocol.Context;
import com.edu.codis.protocol.Types;
import com.edu.codis.protocol.exception.UnknowSignalException;
import com.edu.codis.protocol.exception.WrongTypeException;

public class MapProxy extends AbstractProxy<Map<Object, Object>> {

	@Override
	public Map<Object, Object> getValue(Context ctx, byte flag)
			throws IOException {
		// 非明确定义的对象类型，均当做MAP解析

		ByteBuf in = ctx.getBuffer();
		byte type = getFlagTypes(flag);
		if (type != Types.MAP) {
			throw new WrongTypeException(Types.MAP, type);
		}

		byte signal = getFlagSignal(flag);
		if (signal == 0x00) {
			// 对象解析
			try {
				// 对象赋值
				Map<Object, Object> result = new HashMap<Object, Object>();
				// 加入引用表
				ctx.putObjectRef(result);
				// 字段数量
				byte tag = in.readByte();
				int len = readVarInt32(in, tag);
				for (int i = 0; i < len; i++) {
					byte fKey = in.readByte();
					Object key = ctx.getValue(fKey);
					byte fValue = in.readByte();
					Object value = ctx.getValue(fValue);
					// 字段赋值
					result.put(key, value);
				}
				return result;
			} catch (Exception e) {
				throw new IOException(e);
			}
		} else if (signal == 0x01) {
			// #### 0001
			byte tag = in.readByte();
			int ref = readVarInt32(in, tag);
			@SuppressWarnings("unchecked")
			Map<Object, Object> result = (Map<Object, Object>) ctx
					.getObjectRef(ref);
			return result;
		}

		throw new UnknowSignalException(type, signal);
	}

	@Override
	public void setValue(Context ctx, Map<Object, Object> value)
			throws IOException {
		ByteBuf out = ctx.getBuffer();
		byte flag = Types.MAP;
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
			Set<Entry<Object, Object>> entrySet = value.entrySet();
			// 字段数量
			int size = entrySet.size();
			putVarInt32(out, size);
			for (Entry<Object, Object> e : entrySet) {
				ctx.setValue(e.getKey());
				ctx.setValue(e.getValue());
			}
		}
	}
}
