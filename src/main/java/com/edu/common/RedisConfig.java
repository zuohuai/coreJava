package com.edu.common;

public class RedisConfig {
	/** 唯一ID主键 */
	private int id;
	/** 地址(IP:端口) */
	private String address;
	/** 代理 */
	private String proxy;
	/**密码*/
	private String password;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProxy() {
		return proxy;
	}

	public void setProxy(String proxy) {
		this.proxy = proxy;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
