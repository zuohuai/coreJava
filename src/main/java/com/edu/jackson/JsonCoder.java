package com.edu.jackson;

import java.io.IOException;
import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * JSON 格式转换
 * @author jy
 */
@Component
public class JsonCoder {

	private Logger logger = LoggerFactory.getLogger(JsonCoder.class);

	private static final TypeFactory typeFactory = TypeFactory.defaultInstance();
	private static final ObjectMapper objectMapper = new ObjectMapper();

	public byte[] encode(Object value) {
		try {
			byte[] result = null;
			long start = System.currentTimeMillis();
			result = objectMapper.writeValueAsBytes(value);
			long end = System.currentTimeMillis();
			logger.error("JSON编码耗时:" + (end - start));
			return result;
		} catch (JsonProcessingException e) {
			logger.error("对象Json转换错误");
			throw new IllegalArgumentException("数据格式不正常");
		}
	}

	public Object doDecode(byte[] bytes, Object type) {
		Object result = null;
		if (type.equals(void.class)) {
			return null;
		}
		try {
			long start = System.currentTimeMillis();
			if (type instanceof JavaType) {
				result = decodeByJavaType(bytes, (JavaType) type);
			} else if (type instanceof Type) {
				result = decodeByType(bytes, (Type) type);
			}
			long end = System.currentTimeMillis();
			logger.error("JSON解码耗时:" + (end - start));
		} catch (Exception e) {
			String message = "JSON解码失败:" + e.getMessage();
			if (logger.isErrorEnabled())
				logger.info(message);
			throw new IllegalArgumentException("数据格式不正常");
		}
		return result;
	}

	/** 使用 {@link Type} 描述进行解码 */
	private Object decodeByType(byte[] bytes, Type type) throws JsonParseException, JsonMappingException, IOException {
		return objectMapper.readValue(bytes, 0, bytes.length, typeFactory.constructType(type));
	}

	/** 使用 {@link JavaType} 描述进行解码 */
	private Object decodeByJavaType(byte[] bytes, JavaType type) throws JsonParseException, JsonMappingException, IOException {
		return objectMapper.readValue(bytes, 0, bytes.length, type);
	}
}
