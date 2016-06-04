package com.edu.bit;

import org.junit.Test;

/**
 * 
 * @author zuohuai
 *
 */
public class BitTest {

	@Test
	public void test_bit() throws Exception {
		int a = 15;
		System.out.println(Integer.toBinaryString(a));
		System.out.println(Integer.toBinaryString(a >> 2));

		int b = -15;
		System.out.println(Integer.toBinaryString(b) + "\t" + b);
		System.out.println(Integer.toBinaryString(b >> 2) + "\t" + (b >> 2));
	}

	/**
	 * 第一步  a^=b 即a=(a^b)
	第二步  b^=a 即b=b^(a^b)，由于^运算满足交换律，b^(a^b)=b^b^a。由于一个数和自己异或的结果为0并且任何数与0异或都会不变的，所以此时b被赋上了a的值。
	第三步 a^=b 就是a=a^b，由于前面二步可知a=(a^b)，b=a，所以a=a^b即a=(a^b)^a。故a会被赋上b的值。
	再来个实例说明下以加深印象。int a = 13, b = 6;
	a的二进制为 13=8+4+1=1101(二进制)
	b的二进制为 6=4+2=110(二进制)
	第一步 a^=b  a = 1101 ^ 110 = 1011;
	第二步 b^=a  b = 110 ^ 1011 = 1101;即b=13
	第三步 a^=b  a = 1011 ^ 1101 = 110;即a=6
	 */
	@Test
	public void test_swap() {
		int a = -100;
		int b = 200;
		if (a != b) {
			a ^= b;
			b ^= a;
			a ^= b;
		}
		System.out.println("a=" + a + ", b=" + b);
	}
}
