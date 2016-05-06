package com.edu.game.dota.fight.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 验证结果
 * @author shenlong
 */
public class VerifyResult {

	/** 验证类型 */
	private VerifyType type;
	/** 消费 */
	private List<CostItemResult> costs;
	/** 奖励 */
	private List<RewardResult> rewards;
	/** 附加信息(具体信息玩法确定) */
	private Map<String, Object> ctx;

	public Object addContext(String key, Object value) {
		if (ctx == null) {
			ctx = new HashMap<>();
		}
		return ctx.put(key, value);
	}

	// Getter & Setter

	public VerifyType getType() {
		return type;
	}

	public void setType(VerifyType type) {
		this.type = type;
	}

	public Map<String, Object> getCtx() {
		return ctx;
	}

	public void setCtx(Map<String, Object> ctx) {
		this.ctx = ctx;
	}

	public List<CostItemResult> getCosts() {
		return costs;
	}

	public void setCosts(List<CostItemResult> costs) {
		this.costs = costs;
	}

	public List<RewardResult> getRewards() {
		return rewards;
	}

	public void setRewards(List<RewardResult> rewards) {
		this.rewards = rewards;
	}

	// static

	public static VerifyResult valueOf(VerifyType type) {
		VerifyResult result = new VerifyResult();
		result.type = type;
		return result;

	}

}
