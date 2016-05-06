package com.eyu.snm.module.fight.service.buff;

import com.eyu.snm.module.fight.service.action.Action;
import com.eyu.snm.module.fight.service.action.BuffAction;
import com.eyu.snm.module.fight.service.core.Battle;
import com.eyu.snm.module.fight.service.core.Element;
import com.eyu.snm.module.fight.service.core.Position;
import com.eyu.snm.module.fight.service.core.Skill;
import com.eyu.snm.module.fight.service.core.SkillFactory;
import com.eyu.snm.module.fight.service.core.Unit;
import com.eyu.snm.module.fight.service.op.ActionOperation;
import com.eyu.snm.module.fight.service.op.Operation;

/**
 * 附着在战场的战斗元素
 * @author shenlong
 */
public class ABuffElement implements Element {

	/** 标识 */
	private Short id;
	/** 施放buff的战斗单元 */
	private Unit origin;
	/** buff 配置 */
	private BuffState state;
	/** buff持有者 */
	private Position[] owner;
	/** 下一个行动 */
	private Operation nextOp;

	@Override
	public short getId() {
		return id;
	}

	@Override
	public void setPosition(Position... position) {
		this.owner = position;
	}

	@Override
	public boolean isValid(Battle battle) {
		return true;
	}

	@Override
	public Action getAction(long timing, Battle battle) {
		nextOp = null;
		// 配置技能执行所需信息 TODO 缓存起来
		Skill buffSkill = SkillFactory.getInstance().getSkill(state.getSkillId());
		buffSkill.setOwner(origin);
		buffSkill.setPositions(owner);
		buffSkill.setExecuteId(id);
		return BuffAction.valueOf(this, buffSkill);
	}

	@Override
	public Operation getNextOp() {
		if (nextOp != null) {
			return nextOp;
		}
		Battle battle = origin.getBattle();
		nextOp = ActionOperation.valueOf(this, battle.getDuration() + state.getInterval(), battle.getNextOpId());
		return nextOp;
	}

	public static ABuffElement valueOf(Short buffId, BuffState config, Unit origin, Position... positions) {
		ABuffElement buff = new ABuffElement();
		buff.id = buffId;
		buff.state = config;
		buff.owner = positions;
		buff.origin = origin;
		return buff;
	}

	public Position[] getOwner() {
		return owner;
	}

	public void setOwner(Position[] owner) {
		this.owner = owner;
	}

}
