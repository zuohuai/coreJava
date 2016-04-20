package com.edu.thread.clone;

//ö�����������ͣ�
public enum Sex {
	// �ö���ʵ�ʵȼ��� Sex MALE = new Sex(1)
	MALE(1), 
	FEMALE(2);

	private int code = 0;

	private Sex(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
