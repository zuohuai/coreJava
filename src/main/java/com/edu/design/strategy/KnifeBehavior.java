package com.edu.design.strategy;
/**
 * 武器行为-使用匕首刺杀
 * @author Administrator
 *
 */
public class KnifeBehavior implements WeaponBehavior{

	@Override
	public void useWeapon() {
		System.out.println("武器行为-使用匕首刺杀");
	}

}
