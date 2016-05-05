package com.edu.game.jct.fight.service;

import java.util.Collection;
import java.util.Map;

import com.edu.game.jct.fight.model.ReturnVo;
import com.edu.game.jct.fight.model.report.RoundReport;
import com.edu.game.jct.fight.service.core.Fighter;
import com.edu.game.jct.fight.service.core.Unit;

/**
 * 战斗的回调接口<br/>
 * 战斗锁次序:synchronized(Battle) -> ChainLock(Player...) -> synchronized(LockObject)
 * @author Frank
 */
public interface BattleCallback {

	/**
	 * 战斗结束时的回调
	 * @param battle 当前的战斗对象
	 * @param last 最后一个回合的战报
	 * @return true:战斗结束,false:战斗继续
	 */
	boolean onBattleEnd(Battle battle, RoundReport last);

	/**
	 * 获取战斗中的回合锁
	 * @return 不存在返回null
	 */
	Object getLockObject();

	/**
	 * 回合开始的防守方信息刷新
	 * @param defender 防守方战斗单位
	 * @return 返回有刷新变更的战斗单元集合
	 */
	Collection<Unit> startRefresh(Fighter defender);

	/**
	 * 回合结束的防守方信息刷新
	 * @param defender 防守方战斗单位
	 * @param battle 当前的战斗对象
	 */
	void endRefresh(Fighter defender, Battle battle);

	/**
	 * 战斗过程中发生异常的回调处理
	 * @param ex
	 * @return
	 */
	Map<Long, ReturnVo> onError(RuntimeException ex);

	/**
	 * 战斗恢复超时处理
	 * @param battle
	 */
	void restoreTimeOut(Battle battle);

	/**
	 * 当取消战斗时收到的通知方法
	 * @return 要在推送时提供的信息，没有可以返回null
	 */
	Object onCancel(Battle battle);

	/**
	 * 复活战斗单位
	 * @param battle
	 */
	void onRevive(Battle battle);
}
