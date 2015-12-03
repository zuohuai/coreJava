package com.edu.codis.protocol.exception;

import java.io.IOException;

public class ObjectProxyException extends IOException {
	private static final long serialVersionUID = -671045745597774362L;

	public ObjectProxyException(String message, Exception e) {
		super("类型代理异常 - " + message, e);
	}

}
