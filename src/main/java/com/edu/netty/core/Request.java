package com.edu.netty.core;

/**
 * 请求对象
 * @author Administrator
 */
public class Request<T> {
	/**序号*/
	private long sn;
	/**格式:默认0*/
	private byte format = 0;
	/**会话标识*/
	private long session;
	/**状态*/
	private int state;
	
	public long getSn() {
		return sn;
	}
	public void setSn(long sn) {
		this.sn = sn;
	}
	public byte getFormat() {
		return format;
	}
	public void setFormat(byte format) {
		this.format = format;
	}
	public long getSession() {
		return session;
	}
	public void setSession(long session) {
		this.session = session;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (sn ^ (sn >>> 32));
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Request other = (Request) obj;
		if (sn != other.sn)
			return false;
		return true;
	}
}
