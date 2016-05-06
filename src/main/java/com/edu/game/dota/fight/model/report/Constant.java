package com.edu.game.dota.fight.model.report;

import io.netty.buffer.PooledByteBufAllocator;

public interface Constant {

	/** 战报内容生成器 */
	PooledByteBufAllocator DEFAULT_ALLOCATOR = new PooledByteBufAllocator();
	
	/** 内容分隔标识 */
	byte SPLIT = (byte) 0xFF;
	
	/** 指定字符编码 */
	String CHARSET = "UTF-8";

}
