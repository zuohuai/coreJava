package com.edu.game.jct.fight.model;

import org.apache.commons.lang3.StringUtils;

/**
 * 位置对象
 * @author Administrator
 */
public class Position implements Comparable<Position> {

	/** 最小行下标 */
	public static final int ROW_MIN = 0;
	/** 最大行下标 */
	public static final int ROW_MAX = 2;
	/** 行尺寸 */
	public static final int ROW_SIZE = ROW_MAX + 1;

	/** 最小列下标 */
	public static final int COLUMN_MIN = 0;
	/** 最大列下标 */
	public static final int COLUMN_MAX = 1;
	/** 列尺寸 */
	public static final int COLUMN_SIZE = COLUMN_MAX + 1;

	/** 最大的数组空间 */
	public static final int MAX_SPACE = ROW_SIZE * COLUMN_SIZE;

	/** 分隔符 */
	private static final String SPLIT = ",";
	/** 行坐标 */
	private final int row;
	/** 列坐标 */
	private final int column;
	/** 点的字符串表示形式 */
	private final String point;

	/**
	 * 后     	前
	 * 0,1	0,0
	 * 1,1	1,0
	 * 2,1	2,0
	 */
	private static final Position[][] POSITIONS = { 
		{ new Position(0, 0), new Position(0, 1) }, //行:0
		{new Position(1,0) , new Position(1,1)},    //行:1
		{new Position(2,0), new Position(2,1)}      //行:2
	};

	private Position(int row, int column) {
		if (row < ROW_MIN || row > ROW_MAX) {
			throw new IllegalArgumentException("无效行坐标(0-2)" + row);
		}
		if (column < COLUMN_MIN || column > COLUMN_MAX) {
			throw new IllegalArgumentException("无效列坐标(0-1)" + row);
		}
		this.row = row;
		this.column = column;
		this.point = row + SPLIT + column;

	}
	
	public static Position valueOf(int row, int column){
		try {
			return POSITIONS[row][column];
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("行列坐标["+row +SPLIT + column + "]越界", e);
		}
	}
	
	public static Position valueOf(String value){
		if(StringUtils.isBlank(value)){
			return null;
		}
		try{
			String[] arr = value.split(SPLIT);
			int row = Integer.parseInt(arr[0]);
			int column = Integer.parseInt(arr[1]);
			return valueOf(row, column);
		}catch(Exception e){
			throw new IllegalArgumentException("无效字符串内容["+ value +"]", e);
		}
	}

	@Override
	public int compareTo(Position o) {
		if (this.column < o.column) {
			return -1;
		}
		if (this.column > o.column) {
			return 1;
		}

		if (this.row < o.row) {
			return -1;
		}
		if (this.row > o.row) {
			return 1;
		}
		return 0;
	}

	@Override
	public String toString() {
		return point;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public String getPoint() {
		return point;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + row;
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
		if (column != other.column)
			return false;
		if (row != other.row)
			return false;
		return true;
	}
	
	
}
