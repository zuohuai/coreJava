package com.edu.jsonassert;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import com.edu.utils.json.JsonUtils;

/**
 * 
 * ClassName: JSONAssertTest <br/>
 * Function: 对JSON数据的对比 <br/>
 * date: 2017年6月30日 下午1:22:22 <br/>
 * 
 * @author hison.zhang
 * @version
 * @since JDK 1.7
 */
public class JSONAssertTest {

	@Test
	public void testJSONAssertCommon() throws Exception {
		UserVo userVo = new UserVo();
		userVo.setId(1);
		userVo.setName("JSONAssert1");
		String srcJson = JsonUtils.object2String(userVo);

		userVo.setName("JSONAssert1");
		String toJson = JsonUtils.object2String(userVo);
		JSONAssert.assertEquals(srcJson, toJson, true);
	}

	@Test
	public void testJSONAssertCollection() throws Exception {
		UserVo userVo = new UserVo(1, "JSONAssert1");
		UserVo userVo2 = new UserVo(2, "JSONAssert2");
		List<UserVo> srcList = new LinkedList<>();

		srcList.add(userVo);
		srcList.add(userVo2);
		String srcJson = JsonUtils.object2String(srcList);
		System.out.println(srcJson);

		List<UserVo> toList = new LinkedList<>();
		toList.add(userVo2);
		toList.add(userVo);

		String toJson = JsonUtils.object2String(toList);

		JSONAssert.assertEquals(srcJson, toJson, JSONCompareMode.NON_EXTENSIBLE);
	}

	@Test
	public void testJsonCompareMode() throws Exception {
		String srcJson = "[{\"name\":\"JSONAssert1\",\"id\":1},{\"name\":\"JSONAssert2\",\"id\":2}]";
		String toJson = "[{\"name\":\"JSONAssert1\",\"id\":1},{\"name\":\"JSONAssert2\",\"id\":2}]";

		JSONAssert.assertEquals(srcJson, toJson, JSONCompareMode.STRICT_ORDER);

		// JSONCompareMode 跟某个对象中的属性的顺序没关系, 每个{}为有序
		srcJson = "[{\"id\":1, \"name\":\"JSONAssert1\"},{\"name\":\"JSONAssert2\",\"id\":2}]";
		toJson = "[{\"name\":\"JSONAssert1\",\"id\":1},{\"name\":\"JSONAssert2\",\"id\":2}]";
		JSONAssert.assertEquals(srcJson, toJson, JSONCompareMode.STRICT_ORDER);

		srcJson = "[{\"name\":\"JSONAssert2\",\"id\":2}, {\"id\":1, \"name\":\"JSONAssert1\"}]";
		toJson = "[{\"name\":\"JSONAssert1\",\"id\":1},{\"name\":\"JSONAssert2\",\"id\":2}]";
		JSONAssert.assertEquals(srcJson, toJson, JSONCompareMode.LENIENT);
		
		srcJson = "[{\"id\":2,\"name\":\"JSONAssert2\"}, {\"id\":1, \"name\":\"JSONAssert1\"}]";
		toJson = "[{\"name\":\"JSONAssert1\",\"id\":1},{\"name\":\"JSONAssert2\",\"id\":2}]";
		JSONAssert.assertEquals(srcJson, toJson, JSONCompareMode.NON_EXTENSIBLE);
	}

	class RestResult<T> {

		private int code;

		private String msg;

		private T data;

		public RestResult() {

		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public T getData() {
			return data;
		}

		public void setData(T data) {
			this.data = data;
		}

	}

	class UserVo {
		private int id;

		private String name;

		public UserVo() {

		}

		public UserVo(int id, String name) {
			this.id = id;
			this.name = name;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}
}
