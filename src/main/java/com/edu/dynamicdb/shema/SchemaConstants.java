package com.edu.dynamicdb.shema;

/**
 * xml 解析的常量定义
 * @author zuohuai
 *
 */
public interface SchemaConstants {
	/**用来去解析动态的数据源来*/
	String DS="ds";
	/**db节点配置*/
	String DB= "db";
	/**db的driver类*/
	String DB_CLASS = "class";
	/**db连接信息的路径*/
	String DB_PATH = "path";
	/**db连接的公用参数*/
	String DB_PROPERTIES  ="properties";
	/**db节点默认值*/
	String DB_DEFAULT = "default";
	/** db编号*/
	String DB_ALIAS = "alias";
	/** db的url*/
	String DB_URL = "url";
	/** db的用户名*/
	String DB_USERNAME = "username";
	/** db的密码*/
	String DB_PASSWORD = "password";
}
