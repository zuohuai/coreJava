package com.edu.game.jct.fight.service.effect;

import java.util.HashMap;
import java.util.Map;

import com.edu.game.jct.fight.service.core.Unit;
import com.edu.game.jct.fight.service.effect.buff.BuffState;
import com.edu.game.jct.fight.service.effect.init.InitEffectState;
import com.edu.game.jct.fight.service.effect.passive.PassiveState;
import com.edu.game.jct.fight.service.effect.skill.EffectState;


/**
 * 公式帮助类
 * @author frank
 */
public final class FormulaHelper {
	
	/** 所有者 */
	public static final String OWNER = "owner";
	/** 目标 */
	public static final String TARGET = "target";
	/** 值 */
	public static final String VALUE = "value";

	private FormulaHelper() {
	}

	// Static Method's ...

	/** 构造技能效果公式上下文 */
	public static Map<String, Object> toSkillEffectCtx(EffectState state, Unit owner, Unit target) {
		HashMap<String, Object> ctx = state.cloneCtx();
		ctx.put(OWNER, owner);
		ctx.put(TARGET, target);
		return ctx;
	}

	/** 构造被动效果公式上下文 */
	public static Map<String, Object> toPassiveCtx(PassiveState state, Unit owner, Unit target, Number value) {
		HashMap<String, Object> ctx = state.cloneCtx();
		ctx.put(OWNER, owner);
		ctx.put(TARGET, target);
		ctx.put(VALUE, value);
		return ctx;
	}

	/** 构造BUFF效果公式上下文 */
	public static Map<String, Object> toBuffEffectCtx(BuffState state, Unit owner, Unit target) {
		HashMap<String, Object> ctx = state.cloneCtx();
		ctx.put(OWNER, owner);
		ctx.put(TARGET, target);
		return ctx;
	}

	/** 构造初始化效果公式上下文 */
	public static Map<String, Object> toInitEffectCtx(InitEffectState state, Unit owner, Unit target) {
		HashMap<String, Object> ctx = state.cloneCtx();
		ctx.put(OWNER, owner);
		ctx.put(TARGET, target);
		return ctx;
	}

}
