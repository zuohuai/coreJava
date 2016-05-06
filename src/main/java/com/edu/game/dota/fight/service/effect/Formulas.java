package com.eyu.snm.module.fight.service.effect;

/**
 * 战斗公式
 * @author shenlong
 */
public interface Formulas {

	/** 公式 - 单位BUFF是否命中 */
	String FIGHT_IS_BUFF_HIT = "FIGHT:IS:BUFF:HIT";
	/** 公式 - 战场buff释放 */
	String FIGHT_AREA_BUFF = "FIGHT:AREA:BUFF";
	/** 公式 - 是否命中 */
	String FIGHT_ISHIT = "FIGHT:ISHIT";
	/** 公式 - 是否暴击 */
	String FIGHT_ISCRIT = "FIGHT:ISCRIT";
	/** 公式 - 伤害 */
	String FIGHT_DAMAGE = "FIGHT:DAMAGE";
	/** 公式 - 暴击伤害 */
	String FIGHT_CRIT_DAMAGE = "FIGHT:CRIT:DAMAGE";
	/** 公式 - 硬直伤害 */
	String FIGHT_HR_DAMAGE = "FIGHT:HR:DAMAGE";
	/** 属性改变值计算 */
	String FIGHT_ATTR_CHANGE = "FIGHT:ATTR:CHANGE";
	/** 属性改变值计算 */
	String FIGHT_ATTR_CRIT_CHANGE = "FIGHT:ATTR:CRIT:CHANGE";
	/** 子弹时间计算 */
	String FIGHT_BULLET_TIME = "FIGHT:BULLET:TIME";
	/** 动作时间计算 */
	String FIGHT_NEXT_TIME = "FIGHT:NEXT:TIME";

	// ==== 非战斗时公式
	/** 战斗单元属性计算公式 */
	String UNIT_ATTR = "UNIT:ATTR";

}
