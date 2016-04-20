package com.edu.netty;

public class SocketException extends RuntimeException{
	
	private static final long serialVersionUID = -4197404426810842945L;

	public SocketException(){
		super();
	}
	
	public SocketException(String message, Throwable cause){
		super(message, cause);
	}
	
	public SocketException(String message){
		super(message);
	}
	
	public SocketException(Throwable cause){
		super(cause);
	}
}
