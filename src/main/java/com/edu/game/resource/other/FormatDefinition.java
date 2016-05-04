package com.edu.game.resource.other;

/**
 * 格式信息定义
 * 
 * @author frank
 */
public class FormatDefinition {

	/** 路径 */
	private final String location;
	/** 类型 */
	private final String type;
	/** 后缀 */
	private final String suffix;
	/** 配置信息 */
	private final String config;

	public FormatDefinition(String location, String type, String suffix, String config) {
		this.location = location;
		this.type = type;
		this.suffix = suffix;
		this.config = config;
	}

	// Getter and Setter ...

	public String getLocation() {
		return location;
	}

	public String getType() {
		return type;
	}

	public String getSuffix() {
		return suffix;
	}

	public String getConfig() {
		return config;
	}
	
}
