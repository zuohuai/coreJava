package com.edu.design.strategy;

/**
 * 武器行为-使用宝剑挥舞
 * @author Administrator
 *
 */
public class SwordBehavior implements WeaponBehavior{

	@Override
	public void useWeapon() {
		System.out.println("武器行为-使用宝剑挥舞");
	}

}
