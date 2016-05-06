package com.edu.game.dota.fight.model.report;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import com.eyu.snm.module.fight.model.ResultType;
import com.eyu.snm.module.fight.service.BattleType;
import com.eyu.snm.module.fight.service.config.BattleConfig;

/**
 * 完整战报对象
 * @author Frank
 */
public class Report {

	/** 客户端用位移修正值 */
	public static int OFFSET = 1;

	/** 编码后的战报信息 */
	private byte[] codes;

	/** 战斗类型 */
	private BattleType type;
	/** 开场时间 */
	private long timing;
	/** 每个回合的最大时长 */
	private int roundTime;
	/** 回合战报信息 */
	private List<RoundReport> rounds;
	/** 总结果 */
	private ResultType result;

	// 快速比较器
	private QuickCompare compare = new QuickCompare();
	private TreeMap<Integer, List<Integer>> randomValues = new TreeMap<>();

	public void addRoundReport(RoundReport round) {
		rounds.add(round);
	}

	public RoundReport lastRoundReport() {
		if (rounds.isEmpty()) {
			return null;
		}
		return rounds.get(rounds.size() - 1);
	}

	/** 获取编码后的战报信息 */
	public byte[] toBytes() {
		if (codes == null) {
			ByteBuf buffer = Constant.DEFAULT_ALLOCATOR.buffer();
			encode(buffer);
			codes = new byte[buffer.readableBytes()];
			buffer.readBytes(codes);
		}
		byte[] dest = new byte[codes.length];
		System.arraycopy(codes, 0, dest, 0, codes.length);
		return dest;
	}

	// 对战报结果进行编码
	private void encode(ByteBuf buffer) {
		// 战斗类型
		buffer.writeByte(type.ordinal());
		// 战斗开始时间
		buffer.writeLong(timing);
		/** 每个回合的最大时长 */
		// buffer.writeShort(roundTime);
		// 回合战报
		buffer.writeByte(rounds.size());
		for (RoundReport round : rounds) {
			round.encode(buffer);
		}
		// 总结果
		buffer.writeByte(result.ordinal());
	}

	// Getter and Setter ...

	Report() {
	}

	public BattleType getType() {
		return type;
	}

	public List<RoundReport> getRounds() {
		return rounds;
	}

	public ResultType getResult() {
		return result;
	}

	public void setResult(ResultType result) {
		this.result = result;
	}

	public long getTiming() {
		return timing;
	}

	public int getRoundTime() {
		return roundTime;
	}

	public TreeMap<Integer, List<Integer>> getRandomValues() {
		return randomValues;
	}

	public void setRandomValues(TreeMap<Integer, List<Integer>> randomValues) {
		this.randomValues = randomValues;
	}

	public QuickCompare getCompare() {
		return compare;
	}

	public void add(int relateTime, int value) {
		compare.add(relateTime, value);
	}

	public void addRandomValue(int relateTime, int randomValue) {
		List<Integer> timeValues = randomValues.get(relateTime);
		if (timeValues == null) {
			timeValues = new ArrayList<>();
		}
		timeValues.add(randomValue);
		randomValues.put(relateTime, timeValues);
	}

	public static Report valueOf(BattleConfig config, long timing) {
		Report report = new Report();
		report.type = config.getType();
		report.rounds = new LinkedList<>();
		report.timing = timing;
		report.roundTime = config.getBattleOvertime() / 1000;
		return report;
	}

}
