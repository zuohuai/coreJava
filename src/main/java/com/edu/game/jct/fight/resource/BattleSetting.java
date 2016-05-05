package com.edu.game.jct.fight.resource;

import com.edu.game.jct.fight.service.config.BattleType;
import com.edu.game.resource.anno.Id;
import com.edu.game.resource.anno.Resource;

/**
 * 战斗类型的配置对象
 * @author Frank
 */
@Resource("fight")
public class BattleSetting {

	@Id
	private BattleType id;
	/** 最大回合数 */
	private int round;
	/** 能否不等待回合时间 */
	private boolean noWait;
	/** 能否跳过战斗 */
	private boolean skip;
	/** 战斗超时配置 */
	private Integer battleTimeOut;
	/** 回合超时配置 */
	private Integer roundTimeOut;
	/** 恢复超时配置 */
	private Integer restoreTimeOut;
	/** 比率 */
	private Double percent;

	// Getter and Setter ...

	public BattleType getId() {
		return id;
	}

	public int getRound() {
		return round;
	}

	public boolean isNoWait() {
		return noWait;
	}

	public boolean isSkip() {
		return skip;
	}

	public Integer getBattleTimeOut() {
		return battleTimeOut;
	}

	public Integer getRoundTimeOut() {
		return roundTimeOut;
	}

	public Integer getRestoreTimeOut() {
		return restoreTimeOut;
	}

	public Double getPercent() {
		return percent;
	}
}
