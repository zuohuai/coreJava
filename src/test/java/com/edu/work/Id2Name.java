package com.edu.work;

public class Id2Name {
	
	private String account;
	
	private long owner;
	
	private String name;
	
	public static Id2Name valueOf(String account, long owner, String name){
		Id2Name vo = new Id2Name();
		vo.account = account;
		vo.owner = owner;
		vo.name = name;
		return vo;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public long getOwner() {
		return owner;
	}

	public void setOwner(long owner) {
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
