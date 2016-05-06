package com.edu.game.dota.fight.model.report;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;

public abstract class ByteBufHelper {

	/**
	 * 字符串编码 [byte:str.bytes.length][byte:str.bytes]
	 * @param buffer
	 * @param str
	 */
	public static void writeString(ByteBuf buffer, String str) {
		try {
			byte[] bytes = str.getBytes(Constant.CHARSET);
			buffer.writeByte(bytes.length);
			buffer.writeBytes(bytes);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

}
