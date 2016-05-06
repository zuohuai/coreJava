package com.edu.game.dota.fight.model.report;

import io.netty.buffer.ByteBuf;

import java.util.Date;

import com.eyu.common.utils.time.DateUtils;
import com.eyu.snm.module.fight.service.core.Battle;
import com.eyu.snm.module.fight.service.core.Position;
import com.eyu.snm.module.fight.service.core.Unit;

/**
 * 移动动作战报信息
 * @author Kent
 */
public class MoveReport extends ActionReport {

	/** 横坐标 */
	private int x;
	/** 纵坐标 */
	private int y;

	/**
	 * 移动动作编码 [byte:ActionType.MOVE][byte:x][byte:y]
	 */
	@Override
	public void encode(ByteBuf buffer) {
		// x 修正
		x += Report.OFFSET;
		// 坐标(高位为X坐标低位为Y坐标)
		buffer.writeByte((x << 4) | y);
	}

	@Override
	public String toString() {
		return "MoveReport [x=" + x + ", y=" + y + ", timing=" + DateUtils.date2String(new Date(timing), "mm:ss.SSS") + ", owner=" + owner + "]";
	}

	// Getters and Setters...

	MoveReport() {
	}

	@Override
	public ActionType getType() {
		return ActionType.MOVE;
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

	public static MoveReport valueOf(Unit owner) {
		Position pos = owner.getPosition();
		Battle battle = owner.getOwner().getBattle();

		MoveReport report = new MoveReport();
		report.owner = owner.getId();
		report.timing = battle.getDuration();
		report.relateTime = (int) (battle.getDuration() - battle.getReport().getTiming());
		report.x = pos.getX();
		report.y = pos.getY();
		return report;
	}

}
