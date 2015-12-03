package com.edu.codis.protocol.exception;

import java.io.IOException;

public class MalformedVarintException extends IOException{
	private static final long serialVersionUID = 8408961463958421623L;

	public MalformedVarintException(long value) {
		super("无效的变长整数值[" + value + "]");
	}
	
	public MalformedVarintException(int max, int except) {
		super("最大的变长整数位数[" + max + "], 当前位数[" + except + "]无效");
	}
}
