package com.eyu.snm.module.fight.service.effect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eyu.snm.module.fight.model.report.StageReport;
import com.eyu.snm.module.fight.resource.EffectSetting;
import com.eyu.snm.module.fight.service.core.Unit;

/**
 * 效果执行时的配置信息
 * @author Frank
 */
public class EffectConfig {

	/** 效果配置 */
	private Effect effect;
	/** 效果类型 */
	private EffectType type;
	/** 效果配置值 */
	private Map<String, Object> content;

	/**
	 * 返回一个效果执行时配置信息的克隆副本
	 */
	public EffectConfig clone() {
		EffectConfig ec = new EffectConfig();
		ec.effect = effect;
		ec.type = type;
		ec.content = new HashMap<>(content);
		return ec;
	}

	/**
	 * 获取指定的配置值
	 * @param key
	 * @param clz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getValue(String key, Class<T> clz) {
		return (T) content.get(key);
	}

	/**
	 * 检查指定的配置值是否存在
	 * @param key
	 * @return
	 */
	public boolean hasValue(String key) {
		return content.containsKey(key);
	}

	/**
	 * 执行技能效果
	 * @param ret
	 * @param owner
	 * @param targets
	 * @return
	 */
	public void execute(StageReport ret, Map<String, Object> context, Unit owner, List<Unit> targets) {
		effect.execute(ret, context, content, owner, targets);
	}

	// Getter and Setter ...

	public Effect getEffect() {
		return effect;
	}

	public void setEffect(Effect effect) {
		this.effect = effect;
	}

	public EffectType getType() {
		return type;
	}

	void setType(EffectType type) {
		this.type = type;
	}

	Map<String, Object> getContent() {
		return content;
	}

	void setContent(Map<String, Object> content) {
		this.content = content;
	}

	public static EffectConfig valueOf(EffectSetting effectSetting, Effect effect) {
		EffectConfig config = new EffectConfig();
		config.effect = effect;
		config.type = effectSetting.getType();
		config.content = new HashMap<>(effectSetting.getConfig());
		return config;
	}

}
