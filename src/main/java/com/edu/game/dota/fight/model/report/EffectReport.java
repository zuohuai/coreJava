package com.edu.game.dota.fight.model.report;

import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.eyu.snm.module.fight.model.AlterType;
import com.eyu.snm.module.fight.resource.IdHolder;
import com.eyu.snm.module.fight.service.core.Position;
import com.eyu.snm.module.fight.service.effect.DamageState;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 技能效果战报信息
 * @author Kent
 */
@JsonInclude(Include.NON_NULL)
public class EffectReport {

	/** 受击目标标识 */
	private short id;
	/** 值变更战报 */
	private Map<AlterType, Alter> effects = new HashMap<>();
	/** 战斗单元Buff战报 */
	private Map<Integer, Alter> unitBuffs = new HashMap<>();
	/** 驱散战斗单元Buff战报 */
	private Map<Short, Alter> unBuffs = new HashMap<>();

	/** 受击状态 */
	private Map<DamageState, Integer> states = new HashMap<>();
	/** 抵抗次数 */
	private int time;

	/**
	 * 战报编码
	 * @param buffer
	 */
	void encode(ByteBuf buffer) {
		// 受击目标标识 [受击目标标识:2]
		buffer.writeShort(id);
		// 效果值变更数量 [属性变更数量:1]
		int writeAbleNum = 0;
		for (AlterType type : effects.keySet()) {
			if (type.isReportable()) {
				writeAbleNum++;
			}
		}
		buffer.writeByte(writeAbleNum);
		// [属性变更:*]
		for (Alter alter : effects.values()) {
			if (alter.isReportable()) {
				buffer.writeByte(alter.getType().ordinal());
				if (alter.getType() == AlterType.POSISTION) {
					buffer.writeByte(alter.getValue() + Report.OFFSET);
				} else {
					buffer.writeInt(alter.getValue());
				}
			}
		}
		// [战斗单元Buff数量:1]
		buffer.writeByte(unitBuffs.size());
		// [战斗单元Buff数据:*]
		for (Alter alter : unitBuffs.values()) {
			buffer.writeInt(alter.getValue());
		}
		// [驱散战斗单元Buff数量:1]
		buffer.writeByte(unBuffs.size());
		// [Buff标识数据:*]
		for (Alter alter : unBuffs.values()) {
			buffer.writeShort(alter.getValue());
		}

		// [暴击次数:4b][伤害次数:4b][免疫次数:4b][闪避次数:4b]
		buffer.writeShort(mergeDamageState());
		// 抵抗次数
//		buffer.writeByte(time);

	}

	public void addAlter(Alter changeAlter) {
		Alter alter = effects.get(changeAlter.getType());
		if (alter == null) {
			alter = changeAlter;
		} else {
			alter.merge(changeAlter);
		}
		effects.put(alter.getType(), alter);
	}

	public void addPositionReport(Position position) {
		int value = position.getX() << 4 | position.getY();
		Alter alter = new Alter(AlterType.POSISTION, value, value);
		effects.put(alter.getType(), alter);
	}

	public void addUnitBuffReport(String base, short id) {
		addBuffReport(AlterType.UNIT_BUFF, base, id);
	}

	public void addUnitUnbuffReport(short id) {
		Alter alter = new Alter(AlterType.UNIT_UNBUFF, id, id);
		unBuffs.put(id, alter);
	}

	private void addBuffReport(AlterType type, String base, short id) {
		int value = IdHolder.getInstance().getBuffCode(base) << 16 | id;
		Alter alter = new Alter(type, value, value);
		unitBuffs.put(value, alter);
	}

	public void addDamageState(DamageState state) {
		Integer value = states.get(state);
		if (value == null) {
			value = 0;
		}
		value++;
		states.put(state, value);
	}

	public void addResist() {
		time++;
	}

	public int mergeDamageState() {
		int result = 0;
		for (Entry<DamageState, Integer> entry : states.entrySet()) {
			int value = entry.getValue() << entry.getKey().getOffset();
			result = result | value;
		}
		return result;
	}

	// Getters...

	EffectReport(short id) {
		this.id = id;
	}

	public short getId() {
		return id;
	}

	public Map<AlterType, Alter> getEffects() {
		return effects;
	}

	public Map<Integer, Alter> getUnitBuffs() {
		return unitBuffs;
	}

	public Map<Short, Alter> getUnBuffs() {
		return unBuffs;
	}

	public Map<DamageState, Integer> getStates() {
		return states;
	}

}
