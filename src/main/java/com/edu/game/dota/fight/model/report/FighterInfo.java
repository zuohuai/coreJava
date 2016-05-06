package com.edu.game.dota.fight.model.report;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

import com.eyu.snm.module.fight.model.ModelInfo;
import com.eyu.snm.module.fight.resource.IdHolder;
import com.eyu.snm.module.fight.service.core.Fighter;
import com.eyu.snm.module.fight.service.core.Unit;

/**
 * 作战单位信息
 * @author Frank
 */
public class FighterInfo {

	/** 作战单位标识 */
	private String id;
	/** 模型ID */
	private Short modelId;
	/** 作战单位名称 */
	private String name;
	/** 作战单位显示信息 */
	private ModelInfo info;

	// TODO 战斗里现在没用到,先删除
	// /** 作战单位类型 */
	// private FighterType type;
	/** 战斗单位信息 */
	private List<UnitReport> units;
	/** 初始化技能标识 */
	private List<String> initSkills;

	/**
	 * 战报编码 [string:id][byte:type]
	 * @param buffer
	 */
	public void encode(ByteBuf buffer) {
		// 模型
		// buffer.writeByte(type.ordinal());
		buffer.writeShort(info.getCaptainId());
		// 名称
		ByteBufHelper.writeString(buffer, info.getName());
		// 队伍等级
		buffer.writeByte(info.getLevel());
		// 队伍国家
		buffer.writeByte(info.getCountry().ordinal());
		// 战斗单位
		buffer.writeByte(units.size());
		for (UnitReport unit : units) {
			unit.encode(buffer);
		}
		// 初始化技能标识
		buffer.writeByte(initSkills.size());
		for (String initSkill : initSkills) {
			buffer.writeShort(IdHolder.getInstance().getInitSkillCode(initSkill));
		}
	}

	// Getter and Setter ...

	FighterInfo() {
	}

	public Object getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// public FighterType getType() {
	// return type;
	// }
	//
	// public void setType(FighterType type) {
	// this.type = type;
	// }

	public List<UnitReport> getUnits() {
		return units;
	}

	public void setUnits(List<UnitReport> units) {
		this.units = units;
	}

	public Short getModelId() {
		return modelId;
	}

	public void setModelId(Short modelId) {
		this.modelId = modelId;
	}

	public ModelInfo getInfo() {
		return info;
	}

	public void setInfo(ModelInfo info) {
		this.info = info;
	}

	public static FighterInfo valueOf(Fighter fighter, IdHolder idHolder) {
		FighterInfo info = new FighterInfo();
		info.id = fighter.getId();
		info.name = fighter.getName();
		info.modelId = fighter.getCaptainId();
		info.initSkills = fighter.getEffectCache();
		info.info = fighter.getInfo();
		info.units = new ArrayList<>();
		for (Unit unit : fighter.getUnits()) {
			UnitReport u = UnitReport.valueOf(unit, idHolder);
			info.units.add(u);
		}
		return info;
	}

}
