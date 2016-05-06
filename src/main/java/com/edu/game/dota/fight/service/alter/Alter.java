package com.eyu.snm.module.fight.service.alter;


public interface Alter<T, E extends Number> {

	/**
	 * 合并临时变更数值
	 */
	T merge(T current, T value);

	/**
	 * 添加变更数值
	 * @param current
	 * @param value
	 * @return
	 */
	T add(T current, E value);

	/**
	 * 获取两个值中的绝对值最大值
	 */
	E getAbsMax(E n1, E n2);

	/**
	 * 获取指定值的方向数值
	 */
	E getReverse(E n);

	/**
	 * 将字符串表现的值转为对象形式
	 * @param value
	 * @return
	 */
	E toValue(String value);

	/**
	 * 将数值转换为字符串表现形式
	 * @param value
	 * @return
	 */
	String toString(E value);

	/**
	 * 获取某一数值的倍数
	 * @param value 数值
	 * @param multiple 倍数
	 * @return
	 */
	E multiply(E value, int multiple);

	/**
	 * 获取某一数值的倍数
	 * @param value 数值
	 * @param multiple 倍数
	 * @return
	 */
	E multiply(E value, double multiple);
}
