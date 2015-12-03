package com.edu.codis.protocol.exception;

import java.io.IOException;

public class ObjectConvertException extends IOException{
	private static final long serialVersionUID = -671045745597774362L;

	public ObjectConvertException(Exception e) {
		super("类型转换异常", e);
	}

}
