package com.edu.design.strategy;

/**
 * 武器行为-使用斧头砍劈
 * @author Administrator
 *
 */
public class AxeBehavior implements WeaponBehavior{

	@Override
	public void useWeapon() {
		System.out.println("武器行为-使用斧头砍劈");
	}

}
