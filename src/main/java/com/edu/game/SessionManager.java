package com.edu.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edu.utils.json.JsonUtils;

/**
 * 目标管理器
 * @author Administrator
 *
 */
public class SessionManager {
	private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);

	public void send(Request<?> request, Object... ids) {
		logger.error("给目标[{}], [{}] \n;内容是:[{}]", new Object[]{ids, JsonUtils.object2String(request.getBody())});
	}
}
