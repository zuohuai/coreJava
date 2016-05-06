package com.edu.game.jct.fight.service;

import java.util.Collection;
import java.util.Map;

import com.edu.game.jct.fight.model.ReturnVo;
import com.edu.game.jct.fight.service.core.Fighter;
import com.edu.game.jct.fight.service.core.Unit;

/**
 * 战斗回调接口模版
 * @author Frank
 */
public abstract class CallbackTemplate implements BattleCallback {

	@Override
	public Object getLockObject() {
		return null;
	}

	@Override
	public Collection<Unit> startRefresh(Fighter defender) {
		return null;
	}

	@Override
	public void endRefresh(Fighter defender, Battle battle) {
	}

	@Override
	public void restoreTimeOut(Battle battle) {
	}

	@Override
	public Object onCancel(Battle battle) {
		return null;
	}

	@Override
	public Map<Long, ReturnVo> onError(RuntimeException ex) {
		return null;
	}

	@Override
	public void onRevive(Battle battle) {
	}
}
