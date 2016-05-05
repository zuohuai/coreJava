package com.edu.game.jct.fight.service.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edu.game.Player;
import com.edu.game.jct.fight.exception.FightException;
import com.edu.game.jct.fight.exception.FightExceptionCode;
import com.edu.game.jct.fight.model.FighterInfo;
import com.edu.game.jct.fight.model.UnitState;
import com.edu.game.jct.fight.model.UnitValue;
import com.edu.game.jct.fight.model.report.CdInfo;
import com.edu.game.jct.fight.service.Round;
import com.edu.game.jct.fight.service.effect.round.RoundSkill;
import com.edu.game.jct.fight.service.effect.round.RoundSkillFactory;
import com.edu.game.jct.fight.service.effect.round.RoundSkillState;

/**
 * 战斗单位对象 通过该对象来标识战斗中的攻击方和防御方
 * @author Administrator
 */
public class Fighter {
	private static final Logger LOGGER = LoggerFactory.getLogger(Fighter.class);

	/** 攻击方标识前缀 */
	public static final String ATTACKER_PREFIX = "A:";
	/** 防御方标识前缀 */
	public static final String DEFENDER_PREFIX = "D:";
	/** 战斗单位标识 */
	private String id;
	/** 玩家和战斗单元的关系 */
	private Map<Player, Unit> owners = new HashMap<>(0);
	/** 主将战斗单位 */
	private List<Unit> majors = new LinkedList<>();
	/** 当前的战斗单元 */
	private Unit[][] currents;
	/** 当前全部的战斗单元 */
	private List<Unit[][]> units;
	/** 当前的战斗单元序号 */
	private int idx;
	/** 附加信息 */
	private Map<String, Object> addtions;
	/** 技能状态 */
	private Map<String, RoundSkillState> skillStates;

	/**
	 * 检查玩家战斗单元是否已经选择了技能
	 * @return
	 */
	public boolean isReady() {
		for (Unit unit : owners.values()) {
			if (unit.getChoseSkill() == null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 获取当前武将战斗单位
	 * @return
	 */
	public Unit getCurrentMajor() {
		Unit major = null;
		OUTER:
		for (Unit[] us : currents) {
			for (Unit u : us) {
				if (u == null) {
					continue;
				}
				if (u.getOwner() != null && u.getPlayerId() != null) {
					major = u;
					break OUTER;
				}
			}
		}
		return major;
	}

	/**
	 * 设置主出战单元选中的技能
	 * @param skill 技能标识
	 */
	public void choseMajorSkill(Player player, String skill) {
		Unit unit = owners.get(player);
		unit.choseSkill(skill);
	}

	/**
	 * 设置当前出战单位主角的技能
	 * @param skill
	 */
	public void choseMajorSkill(String skill) {
		Unit major = getCurrentMajor();
		if (major == null) {
			throw new FightException(FightExceptionCode.SKILL_CANNOT_CHOSE);
		}
		major.choseSkill(skill);
	}

	/**
	 * 获取回合技能信息
	 * @param skill
	 * @return
	 */
	public RoundSkillState getRoundSkillState(String skill) {
		if (skillStates == null) {
			return null;
		}
		return skillStates.get(skill);
	}

	/**
	 * 选中回合技能
	 * @param round
	 * @param skill
	 */
	public void choseRoundSkill(Round round, String skill) {
		RoundSkillState state = this.getRoundSkillState(skill);
		if (state == null) {
			throw new FightException(FightExceptionCode.SKILL_NOT_FOUND);
		}
		RoundSkillFactory factory = RoundSkillFactory.getInstance();
		RoundSkill roundSkill = factory.getRoundSkill(skill);
		// 验证技能是否合法
		if (!roundSkill.isChoseVaild(state, round)) {
			throw new FightException(FightExceptionCode.SKILL_CANNOT_CHOSE);
		}
		state.choose();
	}

	/**
	 * 获取下一波援军
	 */
	public void next() {
		idx++;
		currents = units.get(idx);
	}

	/**
	 * 是否有援军
	 * @return
	 */
	public boolean hasRelief() {
		if ((idx + 1) < units.size()) {
			return true;
		}
		return false;
	}

	/**
	 * 刷新技能冷却CD
	 * @return
	 */
	public Collection<CdInfo> refreshSkillCd() {
		for (Unit[] units : currents) {
			for (Unit u : units) {
				if (u == null) {
					continue;
				}
				u.refreshSkillCd();
			}
		}
		Collection<CdInfo> result = new ArrayList<CdInfo>(owners.size());
		for (Unit unit : owners.values()) {
			CdInfo info = unit.getCdInfo();
			result.add(info);
		}
		return result;
	}

	/**
	 * 是否全部战斗单元全部已经死亡
	 * @return
	 */
	public boolean isAllDead() {
		for (Unit[] us : currents) {
			for (Unit u : us) {
				if (u == null)
					continue;
				if (!u.isDead())
					return false;
			}
		}
		return true;
	}

	/**
	 * 获取全部活着的出战单元
	 * @return 不会返回null
	 */
	public List<Unit> getAllLive() {
		List<Unit> result = new ArrayList<Unit>();
		for (Unit[] us : currents) {
			for (Unit u : us) {
				if (u == null)
					continue;
				if (!u.isDead())
					result.add(u);
			}
		}
		return result;
	}

	/**
	 * 复活战斗单位
	 * @return false标识战斗单位里面存在没有死亡的单位,true标识复活成功
	 */
	public boolean revive() {
		if (!isAllDead()) {
			return false;
		}
		for (Unit[] us : currents) {
			for (Unit u : us) {
				if (u == null) {
					continue;
				}
				u.setValue(UnitValue.HP, u.getValue(UnitValue.HP_MAX));
				u.removeState(UnitState.DEAD);
			}
		}
		return true;
	}

	public String getId() {
		return id;
	}

	public Map<Player, Unit> getOwners() {
		return owners;
	}

	public List<Unit> getMajors() {
		return majors;
	}

	public Unit[][] getCurrents() {
		return currents;
	}

	public List<Unit[][]> getUnits() {
		return units;
	}

	public int getIdx() {
		return idx;
	}

	public Map<String, Object> getAddtions() {
		return addtions;
	}

	public Map<String, RoundSkillState> getSkillStates() {
		return skillStates;
	}

	/**
	 * 获取战斗单位信息
	 * @return
	 */
	public FighterInfo createFighterInfo() {
		return FighterInfo.valueOf(this);
	}
}
