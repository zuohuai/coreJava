package com.eyu.snm.module.fight.service.core;

import static com.eyu.snm.module.fight.service.core.Area.*;

/**
 * 位置
 * @author Frank
 */
public class Position {

	/** 合法位置对象缓存 */
	private static Position[][] POSITIONS = new Position[X_SIZE][Y_SIZE];
	/** 攻击方出场位置对象缓存 */
	private static Position[] LEFT_POSITIONS = new Position[Y_SIZE];
	/** 防守方出场位置对象缓存 */
	private static Position[] RIGHT_POSITIONS = new Position[Y_SIZE];

	static {
		for (int x = 0; x < X_SIZE; x++) {
			for (int y = 0; y < Y_SIZE; y++) {
				POSITIONS[x][y] = new Position(x, y);
			}
		}
		// X轴值-1和X_SIZE表示位置在战场之外
		for (int y = 0; y < Y_SIZE; y++) {
			LEFT_POSITIONS[y] = new Position(-1, y);
			RIGHT_POSITIONS[y] = new Position(X_SIZE, y);
		}
	}

	public static Position valueOf(int x, int y) {
		if (x < 0) {
			return LEFT_POSITIONS[y];
		}
		if (x > X_MAX) {
			return RIGHT_POSITIONS[y];
		}
		return POSITIONS[x][y];
	}

	private final int x;
	private final int y;

	/**
	 * 构造方法
	 * @param x X坐标
	 * @param y Y坐标
	 */
	private Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * 获取X坐标
	 * @return
	 */
	public int getX() {
		return x;
	}

	/**
	 * 获取Y坐标
	 * @return
	 */
	public int getY() {
		return y;
	}

	/**
	 * 检查位置是否合法的场景位置
	 * @return
	 */
	public boolean isValid() {
		return x >= 0 && x <= X_MAX;
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
		Position other = (Position) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[" + x + "," + y + "]";
	}

}
