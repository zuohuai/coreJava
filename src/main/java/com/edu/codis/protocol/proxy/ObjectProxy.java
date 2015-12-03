package com.edu.codis.protocol.proxy;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.helpers.MessageFormatter;

import com.edu.codis.protocol.Context;
import com.edu.codis.protocol.Types;
import com.edu.codis.protocol.def.FieldDef;
import com.edu.codis.protocol.def.TypeDef;
import com.edu.codis.protocol.exception.ObjectProxyException;
import com.edu.codis.protocol.exception.UnknowSignalException;
import com.edu.codis.protocol.exception.UnknowTypeDefException;
import com.edu.codis.protocol.exception.WrongTypeException;

public class ObjectProxy extends AbstractProxy<Object> {

	@Override
	public Object getValue(Context ctx, byte flag) throws IOException {
		ByteBuf in = ctx.getBuffer();
		byte type = getFlagTypes(flag);
		if (type != Types.OBJECT) {
			throw new WrongTypeException(Types.OBJECT, type);
		}

		byte signal = getFlagSignal(flag);
		if (signal == 0x00) {
			// #### 0000
			byte tag = in.readByte();
			int rawType = readVarInt32(in, tag);

			if (rawType == 0) {
				// TODO 未定义的类型
				throw new UnknowTypeDefException(rawType);
			}

			// 对象解析
			TypeDef def = ctx.getTypeDef(rawType);
			if (def == null || def.getCode() < 0) {
				if (log.isWarnEnabled()) {
					log.warn("传输对象类型定义[{}]不存在", type);
				}
				throw new UnknowTypeDefException(rawType);
			}

			List<FieldDef> fields = def.getFields();
			Object obj;
			try {
				// 对象赋值
				obj = def.newInstance();
			} catch (Exception e) {
				String msg = MessageFormatter.format("创建类型[{}]实例异常", def.getName()).getMessage();
				throw new ObjectProxyException(msg, e);
			}
			// 加入引用表
			ctx.putObjectRef(obj);
			// 字段数量, 最大255
			int len = 0xFF & in.readByte();
			for (int i = 0; i < len; i++) {
				byte fValue = in.readByte();
				FieldDef fieldDef = fields.get(i);
				Type clz = fieldDef.getType();
				Object value = ctx.getValue(fValue, clz);
				if (value == null) {
					continue;
				}
				// 字段赋值
				try {
					def.setValue(obj, i, value);
				} catch (Exception e) {
					String msg = MessageFormatter.arrayFormat("对象[{}]实例属性[{}]赋值异常",
							new Object[] { def.getName(), def.getFields().get(i).getName() }).getMessage();
					throw new ObjectProxyException(msg, e);
				}
			}
			return obj;
		} else if (signal == 0x01) {
			// #### 0001
			byte tag = in.readByte();
			int ref = readVarInt32(in, tag);
			Object result = ctx.getObjectRef(ref);
			return result;
		}
		throw new UnknowSignalException(type, signal);
	}

	@Override
	public void setValue(Context ctx, Object value) throws IOException {
		ByteBuf out = ctx.getBuffer();
		byte flag = Types.OBJECT;
		int ref = ctx.getObjectRef(value);
		if (ref > 0) {
			// #### 0001
			flag |= 0x01;
			out.writeByte(flag);
			putVarInt32(out, ref);
		} else {
			Class<? extends Object> type = value.getClass();
			TypeDef def = ctx.getTypeDef(type);
			if (def == null || def.getCode() < 0) {
				if (log.isInfoEnabled()) {
					log.info("传输对象类型定义[{}]不存在", type);
				}
				// 类型定义不存在
				// throw new UnknowTypeDefException(type);
				TypeDef mappedDef = ctx.getMappedDef(type);
				List<FieldDef> fields = mappedDef.getFields();
				int size = fields.size();
				Map<Object, Object> map = new HashMap<Object, Object>(size);
				for (int i = 0; i < size; i++) {
					FieldDef fieldDef = fields.get(i);
					String k = fieldDef.getName();
					Object o;
					try {
						o = mappedDef.getValue(value, i);
						if (o != null) {
							map.put(k, o);
						}
					} catch (Exception e) {
						String msg = MessageFormatter.arrayFormat("对象[{}]属性[{}]赋值异常",
								new Object[] { mappedDef.getClass(), i, e }).getMessage();
						throw new ObjectProxyException(msg, e);
					}
				}
				ctx.setValue(map);
			} else {
				// 加入引用表
				ctx.putObjectRef(value);

				// #### 0000
				out.writeByte(flag);

				int code = def.getCode();
				putVarInt32(out, code);

				// 字段数量, 最大255
				List<FieldDef> fields = def.getFields();
				int size = fields.size();
				if (size > 0xFF) {
					String msg = MessageFormatter.arrayFormat("对象[{}]属性数量[{}]超过最大值",
							new Object[] { def.getClass(), size }).toString();
					throw new ObjectProxyException(msg, new RuntimeException(msg));
				}
				out.writeByte((byte) size);
				// 遍历属性
				for (int i = 0; i < size; i++) {
					Object o;
					try {
						o = def.getValue(value, i);
						ctx.setValue(o);
					} catch (Exception e) {
						String msg = MessageFormatter.arrayFormat("对象[{}]属性[{}]:[{}]赋值异常",
								new Object[] { def.getName(), i, def.getFields().get(i).getName(), e }).getMessage();
						throw new ObjectProxyException(msg, e);
					}
				}
			}
		}
	}
}
