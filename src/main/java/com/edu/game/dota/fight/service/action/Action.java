package com.edu.game.dota.fight.service.action;

import com.edu.game.dota.fight.model.report.ActionReport;

/**
 * 行为/动作，表示具体的战斗元素动作或逻辑运算
 * @author Frank
 */
public interface Action {

	
	ActionReport execute();
	

}
