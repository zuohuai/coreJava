package com.eyu.snm.module.fight.service.effect;

/**
 * 效果类型
 * @author shenlong
 */
public enum EffectType {

	// 属性类

	/** 伤害输出效果 */
	DAMAGE,
	/** 目标属性改变效果 */
	TARGET_ATTR,
	/** 自身属性改变效果 */
	OWN_ATTR,
	/** 根据目标数量修改自身属性 */
	ALT_ATTR_BY_TARGET_NUM,

	// BUFF类

	/** 战斗单元BUFF释放 */
	UNIT_BUFF,
	/** 对自己释放BUFF */
	SELF_BUFF,
	/** 固定位置释放BUFF */
	POSITION_BUFF,
	/** 场景BUFF释放 */
	AREA_BUFF,
	/** 子弹类型buff施放 */
	BULLET_BUFF,

	// 位置类
	/** 向指定目标位移 */
	TOWARDS_TARGET,
	/** 击退效果 */
	REPEL,
	/** 施法者与目标互换位置 */
	EXCHANGE,
	/** 拖拽目标到施法者位置 */
	DRAG,
	/** 施法者瞬移到目标位置 */
	TELEPORT,

	// 时间调整类
	/** 下一次出手时间 */
	NEXT_TIME,
	/** 单位行动速度 */
	SPEED,

	// 其他
	/** 打断 */
	BREAK,
	/** 召唤 */
	CALL,
	/** 子弹(手游用) */
	BULLET;

}
