package com.edu.game.dota.fight.model.report;

import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.eyu.snm.module.fight.model.UnitValue;
import com.eyu.snm.module.fight.resource.IdHolder;
import com.eyu.snm.module.fight.service.core.Position;
import com.eyu.snm.module.fight.service.core.Unit;

/**
 * 战斗单位信息
 * @author Frank
 */
public class UnitReport {

	/** 标识 */
	private short id;
	/** 等级 */
	private int level;
	/** 模型标识 */
	private short model;
	/** 横坐标 */
	private int x;
	/** 纵坐标 */
	private int y;
	/** 数值属性 */
	private HashMap<UnitValue, Integer> values = new HashMap<UnitValue, Integer>(UnitValue.values().length);

	/**
	 * 战报编码
	 * @param buffer {@link ByteBuf}
	 */
	public void encode(ByteBuf buffer) {
		// 标识
		buffer.writeShort(id);
		// 等级
		buffer.writeByte(level);
		// 模型标识
		buffer.writeShort(model);
		// 坐标(输出给客户端的x坐标以0开始)
		buffer.writeByte(x << 4 | y);

		// 过滤不需要输出的属性
		Map<UnitValue, Integer> reportable = new HashMap<>();
		for (Entry<UnitValue, Integer> value : values.entrySet()) {
			UnitValue uv = value.getKey();
			if (uv.isReportable()) {
				reportable.put(uv, value.getValue());
			}
		}

		// 数值属性长度
		buffer.writeByte(reportable.size());
		// 数值属性
		for (Entry<UnitValue, Integer> value : reportable.entrySet()) {
			buffer.writeByte(value.getKey().ordinal());
			buffer.writeInt(value.getValue());
		}
	}

	// Getter and Setter ...

	public short getId() {
		return id;
	}

	public void setId(byte id) {
		this.id = id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getModel() {
		return model;
	}

	public void setModel(byte model) {
		this.model = model;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public HashMap<UnitValue, Integer> getValues() {
		return values;
	}

	public void setValues(HashMap<UnitValue, Integer> values) {
		this.values = values;
	}

	public static UnitReport valueOf(Unit unit, IdHolder idHolder) {
		Position pos = unit.getPosition();
		UnitReport info = new UnitReport();
		info.id = unit.getId();
		info.level = unit.getLevel();
		// info.model = unit.getModel().getId();
		info.model = idHolder.getUnitCode(unit.getBaseId());
		info.values = new HashMap<>(unit.getValues());
		info.x = pos.getX();
		info.y = pos.getY();
		return info;
	}

}
