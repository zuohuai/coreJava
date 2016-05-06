package com.edu.codis.redis;

public interface RedisConstant {
	/**设置超时key,不存在时添加*/
	String NX_NOT_EXIST = "NX";
	/**设置超时key,存在时添加*/
	String XX_EXIST = "XX";
	/**设置超时的时间单位秒*/
	String EX_S = "EX";
	/**设置超时的时间单位为毫秒*/
	String PX_MINS = "PX";
}
