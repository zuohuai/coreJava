package com.edu.game.dota.fight.service.op;

import org.apache.commons.lang3.builder.CompareToBuilder;

/**
 * 抽象的执行时间点对象
 * @author Kent
 */
public abstract class TimingOperation implements Operation {

	/** 执行时间点 */
	protected long timing;
	/** 标识ID */
	protected int id;

	@Override
	public long getTiming() {
		return timing;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public int compareTo(Operation o) {
		return new CompareToBuilder()
				.append(timing, o.getTiming())
				.append(id, o.getId())
				.toComparison();
	}
}
