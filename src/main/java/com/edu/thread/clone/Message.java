package com.edu.thread.clone;

public class Message implements Cloneable {

	private int id;
	private String professional;

	public static Message valueOf(int id, String professional) {
		Message message = new Message();
		message.id = id;
		message.professional = professional;
		return message;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public int getId() {
		return id;
	}

	public String getProfessional() {
		return professional;
	}
}
