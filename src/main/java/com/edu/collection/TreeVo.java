package com.edu.collection;

public class TreeVo implements Comparable<TreeVo> {
	/** ID */
	private int id;
	/** 比较器 */
	private int comparator;

	public static TreeVo valueOf(int id, int comparator) {
		TreeVo vo = new TreeVo();
		vo.id = id;
		vo.comparator = comparator;
		return vo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TreeVo other = (TreeVo) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getComparator() {
		return comparator;
	}

	public void setComparator(int comparator) {
		this.comparator = comparator;
	}

	@Override
	public int compareTo(TreeVo o) {
		if(this.comparator == o.getComparator()){
			return 0;
		}else if(this.comparator > o.getComparator()){
			return 1;
		}else if(this.comparator < o.getComparator()){
			return -1;
		}
		return 0;
	}

}
