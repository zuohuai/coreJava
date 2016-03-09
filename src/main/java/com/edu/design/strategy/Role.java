package com.edu.design.strategy;

/**
 * 角色的抽象类
 * @author Administrator
 */
public  class Role {
	/** 武器的行为 */
	private WeaponBehavior weaponBehavior;

	public void setWeaponBehavior(WeaponBehavior weaponBehavior) {
		this.weaponBehavior = weaponBehavior;
	}

	public WeaponBehavior getWeaponBehavior() {
		return weaponBehavior;
	}
}
