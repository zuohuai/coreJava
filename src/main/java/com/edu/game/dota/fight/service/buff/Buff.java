package com.eyu.snm.module.fight.service.buff;

import com.eyu.snm.module.fight.model.report.ActionReport;
import com.eyu.snm.module.fight.model.report.EffectReport;
import com.eyu.snm.module.fight.model.report.StageReport;
import com.eyu.snm.module.fight.service.core.Battle;
import com.eyu.snm.module.fight.service.core.Element;
import com.eyu.snm.module.fight.service.core.Position;
import com.eyu.snm.module.fight.service.core.Unit;

/**
 * buff接口用于处理战场buff及战斗单元buff的添加及删除
 * @author shenlong
 */
public interface Buff {

	/** 获取Buff类型 */
	BuffType getType();

	/** 添加Buff */
	void add(BuffState state, Unit owner, Unit target, StageReport ret, EffectReport effect, Position... positions);

	/** 移除 */
	ActionReport remove(Unit owner, Battle battle, String baseId, short buffId, Element buffs);

}
