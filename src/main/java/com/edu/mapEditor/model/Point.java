package com.edu.mapEditor.model;

import org.apache.commons.lang3.builder.CompareToBuilder;

/**
 * 用来标识坐标的的类
 * @author Administrator
 */
public class Point implements Comparable<Point>{
	/** x坐标 */
	private int x;
	/** y坐标 */
	private int y;

	public static Point valueOf(int x, int y) {
		Point point = new Point();
		point.x = x;
		point.y = y;
		return point;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
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
		Point other = (Point) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.x+","+this.y;
	}
	@Override
	public int compareTo(Point o) {
		return new CompareToBuilder().append(this.x, o.getX()).append(this.y, o.getY()).toComparison();
	}
}
