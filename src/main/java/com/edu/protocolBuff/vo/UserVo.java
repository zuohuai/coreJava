package com.edu.protocolBuff.vo;

public class UserVo {
	/** 名称 */
	private String name;
	/** 性别 */
	private Sex sex;

	public static UserVo valueOf(String name, Sex sex) {
		UserVo vo = new UserVo();
		vo.name = name;
		vo.sex = sex;
		return vo;
	}

	public String getName() {
		return name;
	}

	public Sex getSex() {
		return sex;
	}
}
