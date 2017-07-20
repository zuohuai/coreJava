package com.edu.reflect.service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;
import org.springframework.util.ClassUtils;

import com.edu.reflect.model.RestResult;
import com.edu.reflect.model.UserDetailVo;
import com.edu.reflect.model.UserVo;
import com.edu.utils.json.JsonUtils;

public class CompareTest {

	@SuppressWarnings("rawtypes")
	@Test
	public void testGetType() throws Exception {
		UserVo userVo = buildUserVo();
		String json = JsonUtils.object2String(RestResult.valueOf(200, "调用成功", userVo));
		RestResult restResult = JsonUtils.string2Object(json, RestResult.class);
		Object data = restResult.getData();

		if (data instanceof Map) {
			Map<Object, Object> objMap = Map.class.cast(data);
			for (Entry<Object, Object> entry : objMap.entrySet()) {
				System.out.println(entry.getKey());
				System.out.println("key:" + entry.getKey() + ", value:" + entry.getValue().getClass().getSimpleName());
			}
		}
	}

	@Test
	public void testCompare() {
		UserVo srcUserVo = buildUserVo();
		String json = JsonUtils.object2String(RestResult.valueOf(200, "调用成功", srcUserVo));
		RestResult srcRestResult = JsonUtils.string2Object(json, RestResult.class);

		UserVo toUserVo = buildUserVo();
		json = JsonUtils.object2String(RestResult.valueOf(200, "调用成功", srcUserVo));
		RestResult toRestResult = JsonUtils.string2Object(json, RestResult.class);
	}

	public void compare(Object src, Object to) {

	}

	/**
	 * 
	 * compareOther: 对于基础的对象，可以直接equal 的对象; 包装类型, String <br/>
	 * 
	 * @author hison.zhang
	 * @param msg
	 * @param srcOther
	 * @param toOther
	 * @return
	 */
	public boolean compareOther(String msg, Object srcOther, Object toOther) {
		if (srcOther == null || toOther == null) {
			return false;
		}
		return srcOther.equals(toOther);
	}

	/**
	 * 
	 * compareList:对于List 对象的比较； 适用Collection (List, 数组， Set 支持上面有缺陷) <br/>
	 * 
	 * @author hison.zhang
	 * @param msg
	 * @param srcList
	 * @param toList
	 * @return
	 */
	public boolean compareList(String msg, List<Object> srcList, List<Object> toList) {

		if (srcList == null || toList == null) {
			return false;
		}

		Collections.sort(srcList, (o1, o2) -> o1.hashCode() - o2.hashCode());
		Collections.sort(toList, (o1, o2) -> o1.hashCode() - o2.hashCode());
		if (srcList.size() != toList.size()) {
			return false;
		}
		int index = srcList.size() - 1;
		while (index >= 0) {
			Object srcObj = srcList.get(index);
			Type srcType = getType(srcObj);
			Object toObj = toList.get(index);
			Type toType = getType(toObj);
			if (srcType != toType) {
				return false;
			}
			switch (srcType) {
			case COLLECTIOIN:
				break;
			case MAP:
				break;
			case OTHER:
				break;
			default:
				break;
			}
			index--;
		}
		boolean flag = false;
		return flag;
	}

	public boolean compareMap(Map<String, Object> srcMap, Map<String, Object> toMap) {
		boolean flag = false;
		return flag;
	}

	enum Type {
		COLLECTIOIN, MAP, OTHER, NULL;
	}

	public Type getType(Object obj) {
		if (obj == null) {
			return Type.NULL;
		}
		if (ClassUtils.isAssignableValue(Collection.class, obj)) {
			return Type.COLLECTIOIN;
		}
		if (ClassUtils.isAssignableValue(Map.class, obj)) {
			return Type.MAP;
		}
		return Type.OTHER;
	}

	public class CompareResult {
		/** 错误码 0 表示成功 , -1表示失败 */
		private int resultCode = -1;
		/** 字段链路 */
		private String msg;
		/** 来源值 */
		private Object src;
		/** 比较值 */
		private String to;

		public int getResultCode() {
			return resultCode;
		}

		public void setResultCode(int resultCode) {
			this.resultCode = resultCode;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public Object getSrc() {
			return src;
		}

		public void setSrc(Object src) {
			this.src = src;
		}

		public String getTo() {
			return to;
		}

		public void setTo(String to) {
			this.to = to;
		}

	}

	private UserVo buildUserVo() {
		List<String> addresses = new LinkedList<>();
		addresses.add("广州");
		addresses.add("上海");
		addresses.add("深圳");
		Map<String, UserDetailVo> userDetailVoMap = new HashMap<>();
		userDetailVoMap.put("100", UserDetailVo.valueOf(111, 22));

		Set<String> myNum = new HashSet<>();
		myNum.add("1222");
		UserVo userVo = UserVo.valueOf(1, "hison.zhang", new int[] { 1, 2, 3 }, addresses, userDetailVoMap, myNum);
		return userVo;
	}

	public void check(Object object) {

	}

}
