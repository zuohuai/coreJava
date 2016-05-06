package com.eyu.snm.module.fight.service.effect;

import java.util.List;
import java.util.Map;

import com.eyu.snm.module.fight.model.report.StageReport;
import com.eyu.snm.module.fight.service.core.Unit;

/**
 * 效果接口，用于抽象具体行为(Action)所构成的独立逻辑运算
 * @author Frank
 */
public interface Effect {

	/**
	 * 效果类型
	 * @return {@link EffectType}
	 */
	EffectType getType();

	/**
	 * 效果执行
	 * @param ret 技能阶段战报
	 * @param context 阶段效果执行上下文
	 * @param content 效果配置
	 * @param owner 施法者
	 * @param targets 受击目标
	 */
	void execute(StageReport ret, Map<String, Object> context, Map<String, Object> content, Unit owner, List<Unit> targets);

}
