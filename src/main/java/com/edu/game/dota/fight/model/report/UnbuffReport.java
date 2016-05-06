package com.edu.game.dota.fight.model.report;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

import com.eyu.snm.module.fight.resource.IdHolder;
import com.eyu.snm.module.fight.service.core.Battle;

/**
 * BUFF移除战报
 * @author Kent
 */
public class UnbuffReport extends ActionReport {

	/** BUFF配置标识 */
	private short base;
	/** buff 持有者 如果是战场buff 持有者是0 */
	private short id;
	/** 属性变更 */
	private List<Alter> alters;

	/**
	 * 添加属性变更值
	 * @param alters
	 */
	public void addAlters(List<Alter> alters) {
		this.alters.addAll(alters);
	}

	@Override
	public ActionType getType() {
		return ActionType.UNBUFF;
	}

	@Override
	public void encode(ByteBuf buffer) {
		/*
		 * // 配置标识
		 * buffer.writeShort(base);
		 */
		// 标识
		buffer.writeShort(id);
		// 变更值
		List<Alter> reportable = new ArrayList<>();
		for (Alter alter : alters) {
			if (alter.getType().isReportable()) {
				reportable.add(alter);
			}
		}
		buffer.writeByte(reportable.size());
		for (Alter alter : reportable) {
			buffer.writeByte(alter.getType().ordinal());
			buffer.writeInt(alter.getValue());
		}
	}

	UnbuffReport() {
	}

	public short getBase() {
		return base;
	}

	public short getId() {
		return id;
	}

	public List<Alter> getAlters() {
		return alters;
	}

	/**
	 * @param battle
	 * @param base buff的配置ID
	 * @param id buff的自增ID
	 * @param owner buff 持有者 如果是战场buff 持有者是0
	 * @return
	 */
	public static UnbuffReport valueOf(Battle battle, String base, short id, short owner) {
		UnbuffReport report = new UnbuffReport();
		report.owner = id;
		report.timing = battle.getDuration();
		report.base = IdHolder.getInstance().getBuffCode(base);
		report.relateTime = (int) (battle.getDuration() - battle.getReport().getTiming());
		report.id = owner;
		report.alters = new ArrayList<>();
		return report;
	}
}
