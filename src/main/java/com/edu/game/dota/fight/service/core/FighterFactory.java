package com.eyu.snm.module.fight.service.core;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.eyu.common.resource.Storage;
import com.eyu.common.resource.anno.Static;
import com.eyu.snm.module.fight.exception.FightException;
import com.eyu.snm.module.fight.exception.FightExceptionCode;
import com.eyu.snm.module.fight.model.FighterType;
import com.eyu.snm.module.fight.model.ModelInfo;
import com.eyu.snm.module.fight.resource.FighterSetting;
import com.eyu.snm.module.fight.resource.UnitSetting;
import com.eyu.snm.module.hero.HeroConfig;
import com.eyu.snm.module.hero.service.Camp;
import com.eyu.snm.module.hero.service.Hero;
import com.eyu.snm.module.hero.service.HeroService;
import com.eyu.snm.module.hero.service.HeroUnitService;
import com.eyu.snm.module.player.model.Country;
import com.eyu.snm.module.player.service.Player;
import com.eyu.snm.module.player.service.PlayerService;
import com.eyu.snm.module.star.service.StarService;

/**
 * 战斗单位工厂
 * @author Kent
 */
@Component
public class FighterFactory {

	@Static
	private Storage<String, FighterSetting> fighterStorage;
	@Static
	private Storage<String, UnitSetting> unitStorage;

	@Autowired
	private PlayerService playerService;
	@Autowired
	private HeroUnitService heroUnitService;
	@Autowired
	private HeroService heroService;
	@Autowired
	private HeroConfig heroConfig;
	@Autowired
	private StarService starService;

	/**
	 * 获取一个战斗组的克隆对象
	 * @param id 战斗组配置对象标识[{@link FighterSetting}]
	 * @param type 战斗组类型[{@link FighterType}]
	 * @param isAttacker 是否攻击方
	 * @return 战斗组对象[{@link Fighter}]
	 */
	public Fighter getFighter(String id) {
		FighterSetting fighterSetting = fighterStorage.get(id, true);
		Fighter fighter = fighterSetting.getCache();
		if (fighter == null) {
			synchronized (fighterSetting) {
				if (fighter == null) {
					String name = fighterSetting.getName();
					int level = fighterSetting.getLevel();
					Country country = fighterSetting.getCountry();
					UnitSetting captainSetting = unitStorage.get(fighterSetting.getCaptainId(), true);
					// 构建战斗单元集合
					String[] uids = fighterSetting.getUnits();
					List<Unit> units = new ArrayList<>(uids.length);
					for (int i = 0; i < uids.length; ++i) {
						Unit unit = UnitFactory.getInstance().getUnit(uids[i]);
						unit.changeId((short) (i + 1));
						units.add(unit);
					}
					// 构建战斗组对象
					ModelInfo modelInfo = ModelInfo.valueOf(name, captainSetting.getModel(), level, country);
					fighter = Fighter.valueOf(modelInfo, units, captainSetting.getCaptainSkill());

					// 更新缓存
					fighterSetting.setCache(fighter);
				}
			}
		}
		return fighter.clone();
	}

	/**
	 * 获取npc的战斗对象
	 * @param id
	 * @param type
	 * @param isAttacker
	 * @param level 战斗单元的等级(世界等级)
	 * @return
	 */
	public Fighter getNpcFighter(String id, int level) {
		FighterSetting fighterSetting = fighterStorage.get(id, true);
		Fighter fighter = fighterSetting.getCacheByLevel(level);
		if (fighter == null) {
			synchronized (fighterSetting) {
				if (fighter == null) {
					String name = fighterSetting.getName();
					Country country = fighterSetting.getCountry();
					UnitSetting captainSetting = unitStorage.get(fighterSetting.getCaptainId(), true);

					// 构建战斗单元集合
					String[] uids = fighterSetting.getUnits();
					List<Unit> units = new ArrayList<>(uids.length);
					for (int i = 0; i < uids.length; ++i) {
						Unit unit = UnitFactory.getInstance().getUnit(uids[i], level);
						unit.changeId((short) (i + 1));
						units.add(unit);
					}
					// 构建战斗组对象
					ModelInfo modelInfo = ModelInfo.valueOf(name, captainSetting.getModel(), level, country);
					fighter = Fighter.valueOf(modelInfo, units, captainSetting.getCaptainSkill());

					// 更新缓存
					fighterSetting.setCache(fighter, level);
				}
			}
		}
		return fighter.clone();

	}

	/**
	 * 获取玩家的战斗单元组
	 * @param id 玩家标识
	 * @param type 战斗单元类型
	 * @param isAttacker 是否攻击方
	 * @return {@link Fighter}
	 */
	public Fighter getPlayerFighter(long id) {
		List<Unit> unit = heroUnitService.getHeroUnit(id);
		List<Unit> units = new ArrayList<>();
		for (Unit u : unit) {
			units.add(u.clone());
		}
		// 检查攻击者武将上阵数量
		if (CollectionUtils.isEmpty(units)) {
			throw new FightException(FightExceptionCode.BLOCK_BY_ATTACKER_HERO_AMOUNT);
		}
		// 获取主将信息
		Player player = playerService.load(id);
		Camp camp = heroService.loadCamp(player);
		Long captainId = camp.getCaptain();
		long masterId = camp.getMaster();
		Hero master = heroService.loadHero(player, masterId);
		String captainSkill = null;
		short modelId = heroConfig.getHeroModelId(master.getBase());
		if (captainId != null) {
			Hero captain = heroService.loadHero(player, captainId);
			modelId = heroConfig.getHeroModelId(captain.getBase());
			captainSkill = heroConfig.getCaptainSkill(captain);
		}
		String name = player.getName();
		ModelInfo modelInfo = ModelInfo.valueOf(name, modelId, player.getLevel(), player.getCountry());
		Fighter fighter = Fighter.valueOf(modelInfo, units, captainSkill);

		String icon = starService.getPvpIcon(id);
		if (!StringUtils.isBlank(icon)) {
			fighter.addEffects(icon);
		}

		return fighter;
	}

	// 返回深度克隆对象
	private static FighterFactory instance;

	protected FighterFactory() {
	}

	@PostConstruct
	protected void init() {
		instance = this;
	}

	public static FighterFactory getInstance() {
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
