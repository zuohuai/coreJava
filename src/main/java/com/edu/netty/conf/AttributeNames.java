package com.edu.netty.conf;

public interface AttributeNames {
	/**配置文件*/
	String CONFIG = "config";
	/**bean Id 配置*/
	String ID = "id";
	/**类名*/
	String CLASS = "class";
	/**引用的Bean Name*/
	String REF = "ref";
	/**名字*/
	String NAME = "name";
	/**格式编号*/
	String FORMAT = "format";
	/**是否克隆编码器*/
	String CLONE = "clone";
	/**依赖*/
	String DEPEND_ON = "depend-on";
	/**扫描器中的Bean*/
	String SCAN_BEANS = "scan-beans";
	/**延时回收的SESSION时间(单位:S)*/
	String DEALY_TIME = "delayTime";
}
