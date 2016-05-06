package com.eyu.snm.module.fight.service.effect;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.eyu.common.resource.Storage;
import com.eyu.common.resource.anno.Static;
import com.eyu.snm.module.fight.model.UnitValue;
import com.eyu.snm.module.fight.resource.Formula;
import com.eyu.snm.module.fight.service.core.Unit;

/**
 * 公式帮助类
 * @author Ramon
 */
@Component
public final class FormulaHelper {

	/** 所有者 */
	public static final String OWNER = "owner";
	/** 目标 */
	public static final String TARGET = "target";
	/** 效果内容 */
	public static final String CONTENT = "content";
	/** 战斗单元属性类型 */
	public static final String TYPE = "type";
	/** 公式系数 */
	public static final String RADIO = "radio";
	/** 格子(计算子弹速度用,手游用) */
	public static final String GRID = "grid";
	/** 等级系数(不同等级对应不同怪物强度) */
	public static final String LEVEL = "levelRadio";

	// -----------

	/** 公式 */
	@Static
	protected Storage<String, Formula> formulaStorage;

	private static FormulaHelper inst;

	@PostConstruct
	protected void init() {
		FormulaHelper.inst = this;
	}

	// Static Method's ...

	@SuppressWarnings("unchecked")
	public static <T> T calculate(String formulaId, Map<String, Object> ctx) {
		if (inst == null) {
			while (true) {
				Thread.yield();
				if (inst != null) {
					break;
				}
			}
		}
		Formula formula = inst.formulaStorage.get(formulaId, true);
		return (T) formula.calculate(ctx);
	}

	/** 构造技能效果公式上下文 */
	public static Map<String, Object> toSkillEffectCtx(Unit owner, Unit target, Map<String, Object> content) {
		HashMap<String, Object> ctx = new HashMap<String, Object>();
		ctx.put(OWNER, owner);
		ctx.put(TARGET, target);
		ctx.put(CONTENT, content);
		return ctx;
	}

	/** 构造数值变更效果公式上下文 */
	public static Map<String, Object> toAlterCtx(Unit owner, Unit target, Map<String, Object> content, UnitValue type, Integer radio) {
		HashMap<String, Object> ctx = new HashMap<String, Object>();
		ctx.put(OWNER, owner);
		ctx.put(TARGET, target);
		ctx.put(CONTENT, content);
		ctx.put(TYPE, type);
		ctx.put(RADIO, radio);
		return ctx;
	}

	/** 构建计算子弹速度上下文 */
	public static Map<String, Object> toBulletSpeedCtx(Map<String, Object> content, Integer grid) {
		HashMap<String, Object> ctx = new HashMap<String, Object>();
		ctx.put(CONTENT, content);
		ctx.put(GRID, grid);
		return ctx;
	}

	/** 构造战斗单元属性计算公式上下文 */
	public static Map<String, Object> toAttrCtx(int radio, int level) {
		HashMap<String, Object> ctx = new HashMap<String, Object>();
		ctx.put(RADIO, radio);
		ctx.put(LEVEL, level);
		return ctx;
	}

	/** 构造计算动作时间上下文 */
	public static Map<String, Object> toNextTimeCtx(Unit owner, int nextTime) {
		HashMap<String, Object> ctx = new HashMap<String, Object>();
		ctx.put(OWNER, owner);
		ctx.put(CONTENT, nextTime);
		return ctx;
	}

	private FormulaHelper() {
	}

}
