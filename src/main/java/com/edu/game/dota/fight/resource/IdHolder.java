package com.edu.game.dota.fight.resource;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.edu.game.resource.Storage;
import com.edu.game.resource.anno.Static;

@Component
public class IdHolder {

	private Map<String, Short> skillMapper = new HashMap<>();
	private Map<String, Short> effectMapper = new HashMap<>();
	private Map<String, Short> buffMapper = new HashMap<>();
	private Map<String, Short> initSkillMapper = new HashMap<>();
	private Map<String, Short> unitMapper = new HashMap<>();

	@Static
	private Storage<String, SkillSetting> skillStorage;
	@Static
	private Storage<String, EffectSetting> effectStorage;
	@Static
	private Storage<String, BuffSetting> buffStorage;
	@Static
	private Storage<String, InitSkillSetting> initSkillStorage;
	@Static
	private Storage<String, UnitSetting> unitStorage;

	private static IdHolder instance;

	public static IdHolder getInstance() {
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

	@PostConstruct
	protected void init() {
		initSkills();
		initEffects();
		initBuffs();
		initsSkills();
		initsUnits();

		skillStorage.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				skillMapper.clear();
				initSkills();
			}
		});
		effectStorage.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				effectMapper.clear();
				initEffects();
			}
		});
		buffStorage.addObserver(new Observer() {

			@Override
			public void update(Observable o, Object arg) {
				buffMapper.clear();
				initBuffs();
			}
		});
		initSkillStorage.addObserver(new Observer() {

			@Override
			public void update(Observable o, Object arg) {
				initSkillMapper.clear();
				initsSkills();
			}
		});
		unitStorage.addObserver(new Observer() {

			@Override
			public void update(Observable o, Object arg) {
				unitMapper.clear();
				initsUnits();
			}

		});

		instance = this;
	}

	private void initSkills() {
		for (SkillSetting setting : skillStorage.getAll()) {
			if (skillMapper.containsKey(setting.getId())) {
				throw new RuntimeException("技能[" + setting.getId() + "]战报编码配置重复");
			}
			skillMapper.put(setting.getId(), setting.getCode());
		}
	}

	private void initEffects() {
		for (EffectSetting setting : effectStorage.getAll()) {
			if (effectMapper.containsKey(setting.getId())) {
				throw new RuntimeException("效果[" + setting.getId() + "]战报编码配置重复");
			}
			effectMapper.put(setting.getId(), setting.getCode());
		}
	}

	private void initBuffs() {
		for (BuffSetting setting : buffStorage.getAll()) {
			if (buffMapper.containsKey(setting.getId())) {
				throw new RuntimeException("BUFF[" + setting.getId() + "]战报编码配置重复");
			}
			buffMapper.put(setting.getId(), setting.getCode());
		}
	}

	private void initsSkills() {
		for (InitSkillSetting setting : initSkillStorage.getAll()) {
			if (initSkillMapper.containsKey(setting.getId())) {
				throw new RuntimeException("技能[" + setting.getId() + "]战报编码配置重复");
			}
			initSkillMapper.put(setting.getId(), setting.getCode());
		}
	}

	private void initsUnits() {
		for (UnitSetting setting : unitStorage.getAll()) {
			if (unitMapper.containsKey(setting.getId())) {
				throw new RuntimeException("战斗单位[" + setting.getId() + "]战报编码配置重复");
			}
			unitMapper.put(setting.getId(), setting.getCode());
		}
	}

	/**
	 * 获取技能战报编码
	 * @param id
	 * @return
	 */
	public short getSkillCode(String id) {
		return skillMapper.get(id);
	}

	/**
	 * 获取效果战报编码
	 * @param id
	 * @return
	 */
	public short getEffectCode(String id) {
		return effectMapper.get(id);
	}

	/**
	 * 获取BUFF战报编码
	 * @param id
	 * @return
	 */
	public short getBuffCode(String id) {
		return buffMapper.get(id);
	}

	/**
	 * 获取初始化技能战报编码
	 * @param id
	 * @return
	 */
	public short getInitSkillCode(String id) {
		return initSkillMapper.get(id);
	}

	/**
	 * 获取战斗单元战报编码
	 * @param id
	 * @return
	 */
	public short getUnitCode(String id) {
		return unitMapper.get(id);
	}

}
