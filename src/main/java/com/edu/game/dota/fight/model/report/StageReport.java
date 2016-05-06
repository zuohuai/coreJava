package com.edu.game.dota.fight.model.report;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.eyu.common.utils.time.DateUtils;
import com.eyu.snm.module.fight.model.AlterType;
import com.eyu.snm.module.fight.resource.IdHolder;
import com.eyu.snm.module.fight.service.core.Battle;
import com.eyu.snm.module.fight.service.core.Position;
import com.eyu.snm.module.fight.service.core.Unit;

/**
 * 技能阶段战报信息
 * @author Kent
 */
public class StageReport extends ActionReport {

	/** 技能标识 */
	private String skill;
	/** 阶段下标 */
	private byte stage;
	/** 技能效果战报 */
	private Map<Short, EffectReport> effects = new HashMap<>();
	/** 技能释放者的数值变更 */
	private List<Alter> alters = new ArrayList<>();
	/** 战场BUFF战报 */
	private List<AreaBuffReport> areas = new ArrayList<>();
	/** 驱散战场Buff战报 */
	private Map<Short, Alter> unAreas = new HashMap<>();
	/** 新增战斗单元战报 */
	private List<UnitReport> units = new ArrayList<>();
	/** 被硬直目标标识 */
	private Set<Short> hrs = new HashSet<>();
	/** 被打断目标标识 */
	private Set<Short> breaks = new HashSet<>();
	/** 下一个阶段的时间 */
	private int nextStageTime;

	/**
	 * 技能阶段动作编码
	 */
	@Override
	public void encode(ByteBuf buffer) {
		// 技能标识 [技能标识:2]
		buffer.writeShort(IdHolder.getInstance().getSkillCode(skill));
		// 阶段下标 [阶段下际:1]
		buffer.writeByte(stage);
		// 技能效果战报 [目标数量:1]
		buffer.writeByte(effects.size());
		// [目标数据:*]
		for (EffectReport effect : effects.values()) {
			// 效果战报
			effect.encode(buffer);
		}
		// 释放者数值变更 [技能释放者的属性变更值数量:1]
		buffer.writeByte(alters.size());
		// [技能释放者的属性变更值数量:1]
		for (Alter alter : alters) {
			if (alter.isReportable()) {
				buffer.writeByte(alter.getType().ordinal());
				buffer.writeInt(alter.getValue());
			}
		}
		// 战场BUFF战报 [技能释放者的属性变更值数量:1]
		buffer.writeByte(areas.size());
		// [战场BUFF战报:*]
		for (AreaBuffReport area : areas) {
			area.encode(buffer);
		}
		// 移除战场BUFF战报
		buffer.writeByte(unAreas.size());
		for (Alter alter : unAreas.values()) {
			buffer.writeShort(alter.getCurrent());
		}

		// 新增战斗单元战报 [新增战斗单元战报数量:1]
		buffer.writeByte(units.size());
		// [新增战斗单元战报:*]
		for (UnitReport unit : units) {
			unit.encode(buffer);
		}
		// [被硬直目标数量:1]
		buffer.writeByte(hrs.size());
		// [被硬直目标标识:*(每个目标2)]
		for (short b : hrs) {
			buffer.writeShort(b);
		}
		// 被打断目标标识 [被打断目标数量:1]
		buffer.writeByte(breaks.size());
		// [被打断目标标识:*(每个目标2)]
		for (short b : breaks) {
			buffer.writeShort(b);
		}
	}

	public EffectReport getEffectReport(short id) {
		EffectReport effect = effects.get(id);
		if (effect == null) {
			effect = new EffectReport(id);
			effects.put(id, effect);
		}
		return effect;
	}

	public void addUnitReport(Unit unit,IdHolder idHolder) {
		UnitReport report = UnitReport.valueOf(unit,idHolder);
		units.add(report);
	}

	public void addAreaBuffReport(String base, short id, Position position) {
		AreaBuffReport area = AreaBuffReport.valueOf(base, id, position);
		areas.add(area);
	}

	// 改变自身

	public void addAlter(Alter alter) {
		alters.add(alter);
	}

	public void addValueReport(AlterType type, int value, int current) {
		Alter alter = new Alter(type, value, current);
		alters.add(alter);
	}

	public void addPositionReport(Position position) {
		int value = (position.getX() + Report.OFFSET) << 4 | position.getY();
		Alter alter = new Alter(AlterType.POSISTION, value, value);
		alters.add(alter);
	}

	public void addBreak(short id) {
		breaks.add(id);
	}

	public void addHr(short id) {
		hrs.add(id);
	}

	// Getters and Setters...

	@Override
	public String toString() {
		return "StageReport [skill=" + skill + "stage=" + stage + ", effects=" + effects + ", addition=" + addition + ", timing=" + DateUtils.date2String(new Date(timing), "mm:ss.SSS") + ", owner=" + owner + "]";
	}

	StageReport() {
	}

	@Override
	public ActionType getType() {
		return ActionType.STAGE;
	}

	public String getSkill() {
		return skill;
	}

	public byte getStage() {
		return stage;
	}

	public Map<Short, EffectReport> getEffects() {
		return effects;
	}

	public List<Alter> getAlters() {
		return alters;
	}

	public List<AreaBuffReport> getAreas() {
		return areas;
	}

	public List<UnitReport> getUnits() {
		return units;
	}

	public Set<Short> getBreaks() {
		return breaks;
	}

	public Set<Short> getHrs() {
		return hrs;
	}

	public void setHrs(Set<Short> hrs) {
		this.hrs = hrs;
	}

	public Map<Short, Alter> getUnAreas() {
		return unAreas;
	}

	public int getNextStageTime() {
		return nextStageTime;
	}

	public void setNextStageTime(int nextStageTime) {
		this.nextStageTime = nextStageTime;
	}

	public void addDamage(int value) {
		super.report.add(relateTime, value);
	}

	public static StageReport valueOf(Unit owner, Short executeId, String skill, byte stage) {
		Battle battle = owner.getOwner().getBattle();
		StageReport report = new StageReport();
		report.owner = executeId;
		report.timing = battle.getDuration();
		report.relateTime = (int) (battle.getDuration() - battle.getReport().getTiming());
		report.skill = skill;
		report.stage = stage;
		// -----------
		report.report = battle.getReport();
		return report;
	}
}
