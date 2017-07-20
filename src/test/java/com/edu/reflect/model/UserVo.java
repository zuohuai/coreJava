package com.edu.reflect.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserVo {

	private Integer id;

	private String name;

	private int[] friendIds;

	private List<String> addresses;

	private Map<String, UserDetailVo> detailMap;

	private Set<String> myNum;

	public static UserVo valueOf(Integer id, String name, int[] friendIds, List<String> addresses,
			Map<String, UserDetailVo> detailMap, Set<String> myNum) {
		UserVo userVo = new UserVo();
		userVo.id = id;
		userVo.name = name;
		userVo.friendIds = friendIds;
		userVo.addresses = addresses;
		userVo.detailMap = detailMap;
		userVo.myNum = myNum;
		return userVo;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int[] getFriendIds() {
		return friendIds;
	}

	public void setFriendIds(int[] friendIds) {
		this.friendIds = friendIds;
	}

	public List<String> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<String> addresses) {
		this.addresses = addresses;
	}

	public Map<String, UserDetailVo> getDetailMap() {
		return detailMap;
	}

	public void setDetailMap(Map<String, UserDetailVo> detailMap) {
		this.detailMap = detailMap;
	}

	public Set<String> getMyNum() {
		return myNum;
	}

	public void setMyNum(Set<String> myNum) {
		this.myNum = myNum;
	}

}
