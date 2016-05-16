package com.edu.netty.chapter5;

import java.util.stream.Stream;

/**
 * @author doctor
 *
 * @time 2015年7月6日  
 */
public final class NettyUtil {
	public static final String END_OF_LINE = "\n";

	public static String appenEndOfLine(String... message) {
		StringBuilder stringBuilder = new StringBuilder(256);
		Stream.of(message).forEachOrdered(stringBuilder::append);
		stringBuilder.append(END_OF_LINE);
		return stringBuilder.toString();
	}
}