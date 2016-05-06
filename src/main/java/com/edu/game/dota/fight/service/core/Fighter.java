package com.eyu.snm.module.fight.service.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.eyu.snm.module.fight.model.FighterType;
import com.eyu.snm.module.fight.model.ModelInfo;
import com.eyu.snm.module.fight.model.UnitValue;
import com.eyu.snm.module.fight.resource.InitSkillSetting;
import com.eyu.snm.module.fight.service.op.Operation;
import com.eyu.snm.module.hero.service.mirror.Mirror;
import com.eyu.snm.module.player.model.Country;

/**
 * 战斗单位组
 * @author Frank
 */
public class Fighter implements SkillOwner {

	/** 攻击方标识前缀 */
	private static final String ATTACKER_PREFIX = "A";
	/** 防守方标识前缀 */
	private static final String DENFENER_PREFIX = "D";

	// 基础信息
	/** 所有者 */
	private Battle owner;
	/** 战斗单位标识 */
	private String id;
	/** 资源表ID */
	private String baseId;
	/** 是否攻击方 */
	private boolean attacker;
	/** 战斗单位集合 */
	private TreeMap<Short, Unit> units = new TreeMap<>();
	/** 存活的战斗单位数量 */
	private int live;

	// ----- 模型相关
	private ModelInfo info;

	// ------- 队长技等
	/** 队伍初始技 {@link InitSkillSetting#getId()} */
	private List<String> initEffects = new ArrayList<>();
	/** 战斗中生效的初始技 战斗中确认 */
	private List<String> effectCache = new ArrayList<>();

	// ------ 特殊信息
	/** 连续胜场 */
	private int win;

	/** 回合开始重置战斗单位组数据 */
	public void reset() {
		Iterator<Entry<Short, Unit>> it = units.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Short, Unit> entry = it.next();
			Unit unit = entry.getValue();
//			if (unit.isDead()) {
//				// 移除已经死亡的战斗单位
//				it.remove();
//			} else {
				unit.reset();
//			}
		}
	}

	/**
	 * 战斗单元死亡
	 */
	public void unitDead() {
		live--;
	}

	/** 配置战斗单元 会重新计算活着的人数 */
	public void setUnits(List<Unit> units) {
		live = 0;
		int fight = 0;
		for (Unit unit : units) {
			fight += unit.getFight();
			if (!unit.isDead()) {
				++live;
			}
			this.units.put(unit.getId(), unit);
		}
		info.setFight(fight);
	}

	public Collection<Unit> getUnits() {
		return units.values();
	}

	/**
	 * 返回一个深度克隆的战斗组对象
	 * @param type 战斗组类型 {@link FighterType}
	 * @param isAttacker 是否攻击方
	 * @return 战斗组对象
	 */
	public Fighter clone() {
		Fighter fighter = new Fighter();
		fighter.info = info;
		fighter.initEffects = new ArrayList<>(initEffects);
		fighter.units = new TreeMap<>();
		for (Unit unit : units.values()) {
			Unit u = unit.clone();
			fighter.units.put(u.getId(), u);
		}
		fighter.live = live;
		return fighter;
	}

	/**
	 * 获取战场元素的唯一标识<br>
	 * 攻击方[1,Short.Max]<br>
	 * 防守方[-1,Short.MIN]
	 * @return 唯一标识
	 */
	public short getNextId() {
		return owner.getNextId();
	}

	/**
	 * 设置战斗组单元的标识
	 * @param id
	 */
	public void setId(boolean attacker, long id) {
		this.attacker = attacker;
		this.id = (attacker ? ATTACKER_PREFIX : DENFENER_PREFIX) + id;
	}

	private int getUniqueId() {
		return Integer.valueOf(id.substring(1, id.length()));
	}

	/**
	 * 设置所属战斗
	 * @param battle
	 * @param isManual 是否手操
	 */
	public void setOwner(Battle owner, boolean isManual) {
		this.owner = owner;
		// 重置胜场
		this.win = 0;
		encode(isManual);
	}

	/**
	 * 进行开始战斗前的战斗单元ID修正
	 */
	void encode(boolean isManual) {
		// 编码处理过的战斗单元
		List<Unit> encoded = new ArrayList<>(units.size());
		for (Unit unit : units.values()) {
			short key = (short) (unit.getId() + Constant.MEMBER * getUniqueId());
			unit.changeId(key);
			encoded.add(unit);
			unit.setSKillExecutor();
			unit.setFighter(this);
			unit.setManual(isManual);
		}
		units.clear();
		setUnits(encoded);
	}

	/**
	 * 进行结束战斗时的ID修正
	 */
	void decode() {
		// 解码处理过的战斗单元
		List<Unit> decoded = new ArrayList<>(units.size());
		for (Unit unit : units.values()) {
//			if (!unit.isDead()) {
				/** 清空战斗中产生的buff等 */
				unit.clear();
//			}
			// ====
			short key = (short) (unit.getId() - Constant.MEMBER * getUniqueId());
			unit.changeId(key);
			decoded.add(unit);
		}
		units.clear();
		setUnits(decoded);
	}

	/**
	 * 在战场上增加一个战斗单元,新加入的战斗单元不需要算活着的人数
	 * @param x 出现的X坐标
	 * @param y出现的Y坐标
	 * @param unit 战斗单元
	 */
	public Unit join(int x, int y, Unit unit) {
		// 设置战斗归属
		unit.setFighter(this);
		// 战斗单元上场
		Area area = owner.getArea();
		area.move(unit, x, y, false);

		// 初始化战斗单元的行动
		Operation next = unit.getNextOp();
		owner.addOperation(next);

		return unit;
	}

	/** 获取队伍中需要在初始化阶段执行的技能 */
	public List<String> getEffects() {
		List<String> result = new ArrayList<>();
		if (!effectCache.isEmpty()) {
			result.addAll(effectCache);
		}
		for (Unit unit : units.values()) {
			String correctSkill = unit.getSkillByStage(Stage.CORRECT);
			if (correctSkill != null) {
				result.add(correctSkill);
			}
		}
		return result;
	}

	/**
	 * 添加开场效果
	 * @param initEffectId
	 */
	public void addEffects(String initEffectId) {
		this.initEffects.add(initEffectId);
	}

	public List<String> getInitEffects() {
		return initEffects;
	}

	/** 配置初始化效果 */
	public void setInitEffects(List<String> initEffects) {
		this.initEffects = initEffects;
	}

	/** 获取总血量 */
	public int getTotalHp() {
		int result = 0;
		for (Unit unit : units.values()) {
			result += unit.getValue(UnitValue.HP);
		}
		return result;
	}

	/** 获取总血量最大值 */
	public int getTotalHpMax() {
		int result = 0;
		for (Unit unit : units.values()) {
			// if (!unit.isDead()) {
			result += unit.getValue(UnitValue.HP_MAX);
			// }
		}
		return result;
	}

	/** 作战获胜 */
	public void win() {
		this.win++;
	}

	public Unit getUnit(Short id) {
		return units.get(id);
	}

	@Override
	public boolean isAttacker() {
		return attacker;
	}

	@Override
	public Battle getBattle() {
		return owner;
	}

	// defaults...

	public String getId() {
		return id;
	}

	public List<String> getEffectCache() {
		return new ArrayList<>(effectCache);
	}

	public void setEffectCache(Collection<String> effectCache) {
		this.effectCache = new ArrayList<>(effectCache);
	}

	public String getName() {
		return info.getName();
	}

	public boolean isEmpty() {
		return live <= 0;
	}

	public short getCaptainId() {
		return info.getCaptainId();
	}

	public void setBuffIds(List<String> buffIds) {
		this.initEffects = buffIds;
	}

	public int getLive() {
		return live;
	}

	protected void setAttacker(boolean attacker) {
		this.attacker = attacker;
	}

	public String getBaseId() {
		return baseId;
	}

	public void setBaseId(String baseId) {
		this.baseId = baseId;
	}

	public int getWin() {
		return win;
	}

	public void setWin(int win) {
		this.win = win;
	}

	public ModelInfo getInfo() {
		return info;
	}

	public void setInfo(ModelInfo info) {
		this.info = info;
	}

	/** 配置队伍国家 */
	public void setCountry(Country country) {
		this.info.setCountry(country);
	}

	/**
	 * @param name
	 * @param type
	 * @param attacker
	 * @param units 外部需保证是被克隆过的战斗单元
	 * @return
	 */
	public static Fighter valueOf(ModelInfo info, List<Unit> units, String leaderSkill) {
		Fighter fighter = new Fighter();
		fighter.info = info;
		fighter.setUnits(units);
		if (leaderSkill != null && !leaderSkill.isEmpty()) {
			fighter.initEffects.add(leaderSkill);
		}
		return fighter;
	}

	/**
	 * @param name
	 * @param type
	 * @param attacker
	 * @param units 外部需保证是被克隆过的战斗单元
	 * @return
	 */
	public static Fighter valueOf(ModelInfo info, List<Unit> units, List<String> leaderSkill) {
		Fighter fighter = new Fighter();
		fighter.info = info;
		fighter.setUnits(units);
		if (leaderSkill != null && !leaderSkill.isEmpty()) {
			fighter.initEffects.addAll(leaderSkill);
		}
		return fighter;
	}

	/**
	 * 根据分身创建战斗单元组对象
	 * @param mirror 分身
	 * @param type 战斗对象类型
	 * @param attacker 是否攻击方
	 * @return {@link Fighter}
	 */
	public static Fighter valueOf(Mirror mirror) {
		return valueOf(ModelInfo.valueOf(mirror.getName(), mirror.getLeaderId(), mirror.getLevel(), mirror.getCountry()), mirror.getUnits(), mirror.getSkillId());
	}

}
