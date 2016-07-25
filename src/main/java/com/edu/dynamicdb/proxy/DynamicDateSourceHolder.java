package com.edu.dynamicdb.proxy;

/**
 * 
 * @author zuohuai
 *
 */
public class DynamicDateSourceHolder {
	/**通过这个线程缺获取不到对应的数据源*/
	private static final ThreadLocal<String> holder = new ThreadLocal<String>();

	public static void putDataSourceName(String name) {
		holder.set(name);
	}

	public static String getDataSourceName() {
		String result = holder.get();
		return result;
	}
	
	public static void removeDataSoruceName(){
		holder.remove();
	}
}
