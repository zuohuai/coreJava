package com.edu.game.dota.fight.model.info;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.edu.game.dota.fight.model.ModelInfo;
import com.edu.game.dota.fight.service.core.Fighter;
import com.edu.game.dota.fight.service.core.Unit;

/**
 * @author shenlong
 */
public class FighterVo {

	/** 战斗单位标识 */
	private String id;
	/** 显示信息 */
	private ModelInfo modelInfo;
	/** 战斗单位集合 */
	private Set<UnitVo> units = new HashSet<>();
	/** 战斗中生效的初始技 */
	private List<String> effectCache = new ArrayList<>();

	public static FighterVo valueOf(Fighter fighter) {
		FighterVo result = new FighterVo();
		result.id = fighter.getId();
		result.modelInfo = fighter.getInfo();
		result.effectCache = fighter.getEffectCache();
		for (Unit unit : fighter.getUnits()) {
			if (!unit.isDead()) {
				result.units.add(UnitVo.valueOf(unit));
			}
		}
		return result;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Set<UnitVo> getUnits() {
		return units;
	}

	public void setUnits(Set<UnitVo> units) {
		this.units = units;
	}

	public List<String> getEffectCache() {
		return effectCache;
	}

	public void setEffectCache(List<String> effectCache) {
		this.effectCache = effectCache;
	}

	public ModelInfo getModelInfo() {
		return modelInfo;
	}

	public void setModelInfo(ModelInfo modelInfo) {
		this.modelInfo = modelInfo;
	}

}
