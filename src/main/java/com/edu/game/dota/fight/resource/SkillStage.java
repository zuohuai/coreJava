package com.edu.game.dota.fight.resource;

/**
 * 技能阶段配置对象
 * @author Frank
 */
public class SkillStage {

	/** 选择器链 */
	private String selector;
	/** 执行效果标识 */
	private String[] effects;
	/** 能否被打断 */
	private boolean canBreak;
	/** 能否被硬直 */
	private boolean canHr;

	// Getter and Setter ...

	public String getSelector() {
		return selector;
	}

	public void setSelector(String selector) {
		this.selector = selector;
	}

	public String[] getEffects() {
		return effects;
	}

	public void setEffects(String[] effects) {
		this.effects = effects;
	}

	public boolean isCanBreak() {
		return canBreak;
	}

	public boolean isCanHr() {
		return canHr;
	}

}
