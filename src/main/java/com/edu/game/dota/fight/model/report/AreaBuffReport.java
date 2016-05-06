package com.edu.game.dota.fight.model.report;

import io.netty.buffer.ByteBuf;

import com.eyu.snm.module.fight.resource.IdHolder;
import com.eyu.snm.module.fight.service.core.Position;

/**
 * 战场BUFF战报信息
 * @author Kent
 */
public class AreaBuffReport {

	/** BUFF配置标识 */
	private short base;
	/** BUFF标识 */
	private short id;
	/** X轴坐标 */
	private int x;
	/** Y轴坐标 */
	private int y;

	/**
	 * 编码战报信息
	 * @param buffer
	 */
	public void encode(ByteBuf buffer) {
		// 配置标识
		buffer.writeShort(base);
		// 标识
		buffer.writeShort(id);
		// 坐标 x 修正
		x += Report.OFFSET;
		buffer.writeByte(x << 4 | y);
	}

	public short getBase() {
		return base;
	}

	public short getId() {
		return id;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public static AreaBuffReport valueOf(String base, short id, Position position) {
		AreaBuffReport report = new AreaBuffReport();
		report.base = IdHolder.getInstance().getBuffCode(base);
		report.id = id;
		if (position != null) {
			report.x = position.getX();
			report.y = position.getY();
		}
		return report;
	}

}
