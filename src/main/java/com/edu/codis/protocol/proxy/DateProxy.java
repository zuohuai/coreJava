package com.edu.codis.protocol.proxy;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.Date;

import com.edu.codis.protocol.Context;
import com.edu.codis.protocol.Types;
import com.edu.codis.protocol.exception.WrongTypeException;

public class DateProxy extends AbstractProxy<Date> {

	@Override
	public Date getValue(Context ctx, byte flag) throws IOException {
		ByteBuf in = ctx.getBuffer();
		byte type = getFlagTypes(flag);
		if (type != Types.DATE_TIME) {
			throw new WrongTypeException(Types.DATE_TIME, type);
		}

		// byte signal = getFlagSignal(flag);
		// if (signal == 0x00) {
		// #### 0000
		byte tag = in.readByte();
		long timestame = readVarInt64(in, tag);
		return new Date(timestame * 1000);
		// }
		// throw new WrongTypeException();
	}

	@Override
	public void setValue(Context ctx, Date value) throws IOException {
		ByteBuf out = ctx.getBuffer();
		byte flag = Types.DATE_TIME;
		// #### 0000
		out.writeByte(flag);
		long timestame = value.getTime() / 1000;
		if (timestame < 0) {
			// 时间为1970-1-1 0:00:00.000 时, 根据时区信息， 有机会 < 0, 导致出错, 所以要置 0
			timestame = 0;
		}
		putVarInt64(out, timestame);
	}
}
