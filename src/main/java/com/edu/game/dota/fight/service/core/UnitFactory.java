package com.eyu.snm.module.fight.service.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eyu.common.resource.Storage;
import com.eyu.common.resource.anno.Static;
import com.eyu.snm.module.fight.model.Model;
import com.eyu.snm.module.fight.model.UnitType;
import com.eyu.snm.module.fight.model.UnitValue;
import com.eyu.snm.module.fight.resource.LevelRadioSetting;
import com.eyu.snm.module.fight.resource.ModelSetting;
import com.eyu.snm.module.fight.resource.UnitSetting;
import com.eyu.snm.module.fight.service.effect.FormulaHelper;
import com.eyu.snm.module.fight.service.effect.Formulas;
import com.eyu.snm.module.fight.service.move.MoveType;
import com.eyu.snm.module.hero.service.HeroUnitService;

@Component
public class UnitFactory {

	@Autowired
	private HeroUnitService unitService;
	@Static
	private Storage<String, UnitSetting> unitStorage;
	@Static
	private Storage<Short, ModelSetting> modelStorage;
	@Static
	private Storage<Integer, LevelRadioSetting> levelRadioStorage;

	/**
	 * 获取一个战斗单元的克隆对象
	 * @param id 战斗单元的配置标识 {@link UnitSetting}
	 * @param fighter 战斗组对象
	 * @param isAttacker 是否攻击方
	 * @return
	 */
	public Unit getUnit(String id) {
		UnitSetting unitSetting = unitStorage.get(id, true);
		Unit unit = unitSetting.getUnit();
		if (unit == null) {
			synchronized (unitSetting) {
				if (unit == null) {
					ModelSetting modelSetting = modelStorage.get(unitSetting.getModel(), true);

					// 所有技能
					Map<String, Skill> all = unitSetting.getSkillsAll();
					// 构建普通攻击
					String normalId = unitSetting.getNormal();

					// 构建大招
					String uniqueId = unitSetting.getUnique();
					// 构建技能阶段
					Map<Stage, String> stages = unitSetting.getStages();
					// 配置相应属性信息
					UnitType unitType = modelSetting.getType();
					MoveType moveType = unitType.toMoveType();
					int level = unitSetting.getLevel();
					int order = modelSetting.getOrder();
					short modelId = modelSetting.getId();
					Model model = Model.valueOf(modelId, unitType, order);
					Map<UnitValue, Integer> values = new HashMap<>(unitSetting.getValues());
					unit = Unit.valueOf(id, level, model, values, moveType, unitSetting.getSequence(), uniqueId, stages, normalId, all, 0);
				// 计算战斗力
					int fight = unitService.calcUnitFight(unit);
					unit.setFight(fight);
					unitSetting.setUnit(unit);
				}
			}
		}
		return unit.clone();
	}

	/**
	 * 根据等级获取一个战斗单元的克隆对象
	 * @param id 战斗单元的配置标识 {@link UnitSetting}
	 * @param fighter 战斗组对象
	 * @param isAttacker 是否攻击方
	 * @return
	 */
	public Unit getUnit(String id, int level) {
		UnitSetting unitSetting = unitStorage.get(id, true);
		Unit unit = unitSetting.getUnitByLevel(level);
		int levelRadio = levelRadioStorage.get(level, true).getRadio();
		if (unit == null) {
			synchronized (unitSetting) {
				if (unit == null) {
					ModelSetting modelSetting = modelStorage.get(unitSetting.getModel(), true);

					// 所有技能
					Map<String, Skill> all = unitSetting.getSkillsAll();
					// 构建普通攻击
					String normalId = unitSetting.getNormal();

					// 构建大招
					String uniqueId = unitSetting.getUnique();
					// 构建技能阶段
					Map<Stage, String> stages = unitSetting.getStages();
					// 配置相应属性信息
					UnitType unitType = modelSetting.getType();
					MoveType moveType = unitType.toMoveType();
					int order = modelSetting.getOrder();
					short modelId = modelSetting.getId();
					Model model = Model.valueOf(modelId, unitType, order);
					Map<UnitValue, Integer> values = new HashMap<>(unitSetting.getValues());
					// 根据等级进行属性修正
					Map<UnitValue, Integer> radios = unitSetting.getRadios();
					if (radios != null) {
						for (Entry<UnitValue, Integer> entry : radios.entrySet()) {
							Map<String, Object> ctx = FormulaHelper.toAttrCtx(entry.getValue(), levelRadio);
							int changeValue = FormulaHelper.calculate(Formulas.UNIT_ATTR, ctx);
							values.put(entry.getKey(), changeValue);
						}
					}
					unit = Unit.valueOf(id, level, model, values, moveType, unitSetting.getSequence(), uniqueId, stages, normalId, all, 0);
					// 计算战斗力
					int fight = unitService.calcUnitFight(unit);
					unit.setFight(fight);
					unitSetting.setUnit(unit, level);
				}
			}
		}
		return unit.clone();
	}

	private static UnitFactory instance;

	protected UnitFactory() {
	}

	@PostConstruct
	protected void init() {
		UnitFactory.instance = this;
	}

	public static UnitFactory getInstance() {
		if (instance == null) {
			while (true) {
				Thread.yield();
				if (instance != null) {
					break;
				}
			}
		}
		return instance;
	}
}
