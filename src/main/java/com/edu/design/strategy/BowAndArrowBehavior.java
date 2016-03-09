package com.edu.design.strategy;

/**
 * 武器行为-使用弓箭射击
 * @author Administrator
 *
 */
public class BowAndArrowBehavior implements WeaponBehavior{

	@Override
	public void useWeapon() {
		System.out.println("武器行为-使用弓箭射击");
	}

}
