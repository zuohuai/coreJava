package com.eyu.snm.module.fight.service.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eyu.snm.module.fight.model.Model;
import com.eyu.snm.module.fight.model.UnitState;
import com.eyu.snm.module.fight.model.UnitType;
import com.eyu.snm.module.fight.model.UnitValue;
import com.eyu.snm.module.fight.model.report.Alter;
import com.eyu.snm.module.fight.service.action.Action;
import com.eyu.snm.module.fight.service.action.DeadSkillAction;
import com.eyu.snm.module.fight.service.action.SkillAction;
import com.eyu.snm.module.fight.service.action.UniqueAction;
import com.eyu.snm.module.fight.service.buff.BuffState;
import com.eyu.snm.module.fight.service.buff.StateCtxKeys;
import com.eyu.snm.module.fight.service.effect.BreakType;
import com.eyu.snm.module.fight.service.move.MoveType;
import com.eyu.snm.module.fight.service.op.ActionOperation;
import com.eyu.snm.module.fight.service.op.Operation;

/**
 * 战斗单位，用于表示战斗中的出场单位
 * @author Frank
 */
public class Unit implements Element, SkillOwner, Comparable<Unit> {

	private static final Logger logger = LoggerFactory.getLogger(Unit.class);

	/** 攻击方标识前缀 */
	public static final String ATTACKER_PREFIX = "A";
	/** 防守方标识前缀 */
	public static final String DEFENDER_PREFIX = "D";

	/** 战斗单元表配置标识 */
	private String baseId;
	/** 所有者 */
	private Fighter owner;

	/** 标识 */
	private short id;
	/** 等级 */
	private int level;
	/** 模型信息 */
	private Model model;
	/** 所在位置 */
	private Position position;
	/** 战斗单元原始数据 */
	private HashMap<UnitValue, Integer> origin = new HashMap<UnitValue, Integer>(UnitValue.values().length);
	/** 数值属性 */
	private HashMap<UnitValue, Integer> values = new HashMap<UnitValue, Integer>(UnitValue.values().length);
	/** 状态 */
	private int state = UnitState.NORMAL;
	/** buff状态 <buffId,BuffState> */
	private Map<Short, BuffState> buffStates = new HashMap<>();
	/** 硬直冷却时间 */
	private Long coolTime;
	/** 移动 */
	private MoveType moveType;
	/** 所有技能 */
	private HashMap<String, Skill> all;
	/** 普通攻击 */
	private String normal;
	/** 技能执行序列 */
	private String[] skills;
	/** 必杀技能效果 */
	private String unique;
	/** 阶段技能效果 */
	private HashMap<Stage, String> stages;

	/** 前一个返回操作 */
	private Operation nextOp;
	/** 下次行动的时间点 */
	private Long nextTiming;
	/** 当前正在施放的技能下标(null:不存在施放中的技能,-1:必杀技施放中,其它:主动技能下标) */
	private String current;

	/** 行动计数器 */
	private int counter;

	/** 武将战斗力 */
	private int fight;

	/** 是否手操 */
	private boolean isManual;

	/**
	 * 修改ID
	 * @param id
	 */
	public void changeId(short id) {
		this.id = id;
	}

	public Unit clone() {
		Unit unit = new Unit();
		unit.id = id;
		unit.baseId = baseId;
		unit.level = level;
		unit.model = model;
		unit.values = new HashMap<>(values);
		unit.origin = new HashMap<>(origin);
		unit.state = UnitState.NORMAL;
		unit.buffStates = new HashMap<>();
		unit.moveType = moveType;
		unit.unique = unique;
		unit.normal = normal;
		unit.skills = skills;
		unit.all = new HashMap<>();
		unit.stages = new HashMap<>(stages);
		for (Entry<String, Skill> entry : all.entrySet()) {
			String skillId = entry.getKey();
			Skill skill = entry.getValue().clone(unit);
			skill.setOwner(unit);
			unit.all.put(skillId, skill);
		}
		unit.fight = fight;
		return unit;
	}

	/**
	 * 回合开始重置战斗单位数据
	 */
	public void reset() {
		resetData();
		Iterator<Entry<Short, BuffState>> iteor = buffStates.entrySet().iterator();
		while (iteor.hasNext()) {
			Entry<Short, BuffState> buffEntry = iteor.next();
			if (buffEntry.getValue().isForever()) {
				continue;
			}
			removeAlterValue(buffEntry.getKey());
			iteor.remove();
			mergeMergeBuffEffect();
		}
	}

	// 战斗结束后清空buff,为重用fighter
	public void clear() {
		resetData();
		// 当buff影响HP最大值时保留相应比例HP,保留生命=(剩余最大生命*原生命)/原最大值,按比比例保存hp
		int hp = (int) ((getOrigin(UnitValue.HP_MAX) * 1.0 / getValue(UnitValue.HP_MAX)) * getValue(UnitValue.HP));
		if (hp <= 0) {
			hp = 1;
		}
		setValue(UnitValue.HP, hp);
		buffStates.clear();
	}

	// 重置数据
	public void resetData() {
		state = UnitState.NORMAL;
		position = null;
		coolTime = null;
		nextOp = null;
		nextTiming = null;
		if (current != null) {
			currentSkill().interrupt();
		}
		current = null;
		counter = 0;
	}

	/**
	 * 进入战场
	 * @param x X坐标
	 * @param y Y坐标
	 */
	public void enter(int x, int y, long enterTime) {
		long duration = getBattle().getDuration();
		// 设置出场前的坐标
		this.position = Position.valueOf(x, y);
		// 准备开场技能
		String skill = stages.get(Stage.ROUND_START);
		if (skill != null) {
			this.current = skill;
			this.nextOp = ActionOperation.valueOf(this, duration + Constant.FIRST_ENTER + Constant.INTERVAL_ENTER, getBattle().getNextOpId());
			this.nextTiming = duration;
			return;
		}
		this.nextTiming = enterTime;

	}

	/**
	 * 获取下次行动
	 * @param duration 当前战斗的持续时长
	 * @return
	 */
	public Operation getNextOp() {
		if (state == UnitState.DEAD) {
			return null;
		}
		if (nextOp != null) {
			return nextOp;
		}
		if (nextTiming != null) {
			nextOp = ActionOperation.valueOf(this, nextTiming, getBattle().getNextOpId());
		} else {
			long timing = owner.getBattle().getDuration() + getValue(UnitValue.INTERVAL);
			nextOp = ActionOperation.valueOf(this, timing, getBattle().getNextOpId());
		}
		return nextOp;
	}

	@Override
	public Action getAction(long timing, Battle battle) {

		// 更新执行时间点信息
		nextOp = null;
		nextTiming = null;
		// 获取死亡阶段技能
		if (state == UnitState.DEAD) {
			return DeadSkillAction.valueOf(this, currentSkill());
		}
		// 禁魔状态直接返回普通攻击
		if (hasState(UnitState.FORBID)) {
			current = normal;
			return SkillAction.valueOf(this, currentSkill());
		}
		// 能否施放必杀的标记
		boolean flag = false;
		// 是否执行自动发大招
		if (!isManual) {
			if (unique != null && getValue(UnitValue.MP) == Constant.MP_MAX
					&& getBattle().getRoundTime() > Constant.UNIQUE_TIME && (state & UnitState.BIG_FORBID) == 0) {
				flag = true;
			}
			if (flag && current != null) {
				// 检查能否打断当前技能的施放
				if (!all.get(current).canBreak()) {
					flag = false;
				} else {
					all.get(current).interrupt();
				}
			}
		}
		// 初始施放大招,多数大招需要配置消耗MP 100
		if (flag) {
			current = unique;
			return UniqueAction.valueOf(this, all.get(unique));
		}
		// 继续原来施放中的技能施放
		if (current != null) {
			if (current.equals(unique)) {
				// 继续必杀技能施放
				return UniqueAction.valueOf(this, all.get(unique));
			} else {
				// 继续主动技能施放
				return SkillAction.valueOf(this, currentSkill());
			}
		}
		// 通过技能计数器选择技能施放
		counter = counter % skills.length;
		String skill = skills[counter];
		current = skill;
		counter++;

		return SkillAction.valueOf(this, currentSkill());
	}

	/** 技能执行完毕时调用 */
	public void finishSkill() {
		this.current = null;
	}

	/**
	 * 移动到指定位置
	 * @param x
	 * @param y
	 * @param initiative 是否主动发生位移,主动位移会修正下一次行动时间
	 */
	public void move(int x, int y, boolean initiative) {
		this.position = Position.valueOf(x, y);
		if (initiative) {
			nextOp = null;
			nextTiming = owner.getBattle().getDuration() + getValue(UnitValue.MOVE);
		}
	}

	/**
	 * 移除单位即将执行的行动(对于未死的单位,需要在调用此方法后主动生成该单位新的行动)
	 */
	public void removeNextOp() {
		Battle battle = owner.getBattle();
		if (nextOp == null) {
			logger.error("单位打断了自己!");
			return;
		}
		battle.removeOperation(nextOp);
		nextOp = null;
	}

	/**
	 * 受到硬直伤害攻击时调用的方法(要在伤害效果之后调用)
	 * @param HRHurt 硬直伤害
	 * @return
	 */
	public boolean doHR(int HRHurt) {
		int unhr = values.get(UnitValue.UNHR); // 霸体值
		int unhrTime = values.get(UnitValue.UNHR_TIME);// 霸体冷却时间
		Battle battle = owner.getBattle();
		long duration = battle.getDuration();
		// 战斗单元处于施法中及霸体保护时间内时不会造成硬直
		if (coolTime != null && coolTime + unhrTime > duration) {
			return false;
		}
		// 硬直伤害没有达到霸体值时不会造成硬直
		if (HRHurt > unhr) {
			if (current != null) {
				if (!currentSkill().canHr()) {
					return false;
				}
				currentSkill().interrupt();
			}
			coolTime = battle.getDuration();
			int delayTime = values.get(UnitValue.HR_TIME);
			long nextTime = duration + delayTime;
			// 单位行动间隔时硬直时间影响到下一个技能施放
			if (nextTime > nextOp.getTiming()) {
				removeNextOp();
				nextTiming = nextTime;
				nextOp = ActionOperation.valueOf(this, nextTiming, battle.getNextOpId());
				battle.addOperation(nextOp);
			}
			return true;
		}
		return false;
	}

	/**
	 * 受到打断效果时调用
	 */
	public boolean doBreak(BreakType type) {
		if (current != null) {
			if (type == BreakType.SKILL) {
				if (current.equals(normal)) {
					return false;
				}
			}
			if (currentSkill().canBreak()) {
				currentSkill().interrupt();
				current = null;
				return true;
			}
		}
		return false;
	}

	@Override
	public void setPosition(Position... positions) {
		this.position = positions[0];
	}

	@Override
	public boolean isAttacker() {
		return owner.isAttacker();
	}

	/**
	 * 获取战斗对象
	 * @return
	 */
	@Override
	public Battle getBattle() {
		return getFighter().getBattle();
	}

	@Override
	public boolean isValid(Battle battle) {
		if (isDead()) {
			String deadSkill = stages.get(Stage.UNIT_DEAD);
			if (deadSkill != null && deadSkill.equals(current)) {
				return true;
			}
			return false;
		}
		return true;
	}

	/** 设置死亡状态 */
	public void dead() {
		// 死亡直接设置死亡状态，没有任何其他效果
		if (state == UnitState.DEAD) {
			logger.error("单位已经死亡,目标选择器选中了死亡单位想要让他再死一次 = . =");
			return;
		}
		state = UnitState.DEAD;
		owner.unitDead();
		Battle battle = owner.getBattle();
		if (nextOp != null) {
			battle.removeOperation(nextOp);
		} else {
			logger.error("单位的伤害技能打死了自己 = . =");
		}
		// 施放死亡技能
		String deadSkill = stages.get(Stage.UNIT_DEAD);
		if (deadSkill != null) {
			nextOp = ActionOperation.valueOf(this, owner.getBattle().getDuration(), battle.getNextOpId());
			battle.addOperation(nextOp);
			current = deadSkill;
		}

	}

	/** 设置存活状态 */
	public void live() {
		removeState(UnitState.DEAD);
	}

	// 状态相关的逻辑方法 ...

	/** 合并特殊状态效果 */
	public void mergeMergeBuffEffect() {
		int result = 0;
		for (BuffState buff : buffStates.values()) {
			result = result | buff.getSpecialEffect();
		}
		// 添加效果到原来的单位状态
		state = (state & 0b0000_1111) | result;
	}

	/**
	 * 移除buff效果
	 * @param effctID
	 * @return
	 */
	public List<Alter> removeBuffEffect(short effctID) {
		List<Alter> alters = removeAlterValue(effctID);
		buffStates.remove(effctID);
		mergeMergeBuffEffect();
		return alters;
	}

	/**
	 * 移除buff改变的属性值
	 * @param effctID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Alter> removeAlterValue(short effctID) {
		BuffState state = buffStates.get(effctID);
		HashMap<String, Object> buffCtx = state.getContent();
		List<Alter> alters = new ArrayList<>();
		if (buffCtx != null && buffCtx.containsKey(StateCtxKeys.ALTER_VALUE)) {
			Map<UnitValue, Integer> alterValue = (Map<UnitValue, Integer>) state.getContent().get(
					StateCtxKeys.ALTER_VALUE);
			for (Entry<UnitValue, Integer> entry : alterValue.entrySet()) {
				if (entry.getKey() == UnitValue.HP) { // 移除BUFF时不该变HP属性
					continue;
				} else if (entry.getKey() == UnitValue.HP_MAX) { // 当buff影响HP最大值时保留相应比例HP,保留生命=(剩余最大生命*原生命)/原最大值,按比比例保存hp TODO
																													// 溢出问题
					int hp = ((getValue(UnitValue.HP_MAX) - entry.getValue()) * getValue(UnitValue.HP)) / getValue(UnitValue.HP_MAX);
					if (hp <= 0) {
						hp = 1;
					}
					setValue(UnitValue.HP, hp);
				}
				Alter alter = increaseValue(entry.getKey(), -entry.getValue());
				alters.add(alter);
			}
		}
		return alters;
	}

	/**
	 * 给战斗单元添加buff
	 * @param buffId
	 * @param buff
	 */
	public void addBuff(short buffId, BuffState buff) {
		buffStates.put(buffId, buff);
		mergeMergeBuffEffect();
	}

	/** 添加状态 */
	public void addState(int status) {
		state = state | status;
	}

	/** 移除状态 */
	public void removeState(int status) {
		if (hasState(status)) {
			state = state ^ status;
		}
	}

	/** 检查是否有某种状态 */
	public boolean hasState(int status) {
		return (state & status) == status ? true : false;
	}

	/** 检查是否死亡 */
	public boolean isDead() {
		return hasState(UnitState.DEAD) || getValue(UnitValue.HP) == 0;
	}

	/**
	 * 增加/减少属性值(累加关系)
	 * @param type 约定的键名
	 * @param value 增量
	 * @return 最新值
	 */
	public Alter increaseValue(UnitValue type, int value) {
		int current = getValue(type);
		current += value;
		switch (type) {
		// 该属性变更不会致死
		case HP:
			if (current <= 0) {
				current = 1;
			} else if (current > getValue(UnitValue.HP_MAX)) {
				current = getValue(UnitValue.HP_MAX);
			}
			break;
		case MP:
			if (current <= 0) {
				current = 0;
			} else if (current > Constant.MP_MAX) {
				current = Constant.MP_MAX;
			}
			break;
		case NT_EFFECT:
			if (current < Constant.NT_EFFECT_MIN) {
				current = Constant.NT_EFFECT_MIN;
			} else if (current > Constant.NT_EFFECT_MAX) {
				current = Constant.NT_EFFECT_MAX;
			}
			break;
		case MOVE_EFFECT:
			if (current < Constant.MOVE_EFFECT_MIN) {
				current = Constant.MOVE_EFFECT_MIN;
			} else if (current > Constant.MOVE_EFFECT_MAX) {
				current = Constant.MOVE_EFFECT_MAX;
			}
			break;
		default:
			current += value;
			if (current < 0) {
				current = 0;
			}
			break;
		}
		setValue(type, current);
		// 记录战报信息
		return new Alter(type.toAlterType(), value, current);
	}

	/**
	 * 受到伤害时调用
	 * @param value
	 */
	public Alter hurt(int value) {
		if (value > 0) {
			throw new IllegalArgumentException("伤害必须为负数");
		}
		if (state == UnitState.DEAD) {
			return new Alter(UnitValue.HP.toAlterType(), value, 0);
		}
		int current = getValue(UnitValue.HP);
		current += value;
		if (current <= 0) {
			current = 0;
			dead();
		}
		setValue(UnitValue.HP, current);
		return new Alter(UnitValue.HP.toAlterType(), value, current);
	}

	/**
	 * 改变当前战斗单元的位置
	 * @param x
	 * @param y
	 */
	public Position relocate(int x, int y) {
		Area area = getArea();
		area.move(this, x, y, false);
		return position;
	}

	/**
	 * 获取指定的数值属性
	 * @param type 属性类型
	 * @return
	 */
	public int getValue(UnitValue type) {
		Integer ret = values.get(type);
		if (ret == null) {
			return 0;
		} else {
			return ret;
		}
	}

	/**
	 * 获取指定的原始数值属性
	 * @param type 属性类型
	 * @return
	 */
	public int getOrigin(UnitValue type) {
		Integer ret = origin.get(type);
		if (ret == null) {
			return 0;
		} else {
			return ret;
		}
	}

	public void updateNextTiming(int time) {
		nextTiming = owner.getBattle().getDuration() + time;
	}

	/**
	 * 冷却时间
	 * @return
	 */
	public Long getCoolTime() {
		return coolTime;
	}

	public void setCoolTime(Long coolTime) {
		this.coolTime = coolTime;
	}

	/**
	 * 获取指定的数值属性
	 * @param type 属性类型
	 * @return
	 */
	public int getValue(String type) {
		return getValue(UnitValue.valueOf(type));
	}

	/**
	 * 设置指定的数值属性
	 * @param type 属性类型
	 * @param value 值
	 * @return
	 */
	public void setValue(UnitValue type, int value) {
		values.put(type, value);
	}

	/**
	 * 获取战斗场景
	 * @return
	 */
	public Area getArea() {
		return getBattle().getArea();
	}

	/**
	 * 获取战斗组对象
	 * @return
	 */
	public Fighter getFighter() {
		return owner;
	}

	/** 获取阶段技能 */
	public String getSkillByStage(Stage stage) {
		return stages.get(stage);
	}

	/** 获取当前技能 */
	public Skill currentSkill() {
		Skill currentSkill = all.get(current);
		if (currentSkill == null) {
			logger.error("武将不存在技能:" + current);
		}
		return currentSkill;
	}

	// ======
	/** 获取大招 */
	public Action getUniqueAction(long timing, Battle battle) {
		// 眩晕,沉默等限制;
		if ((state & UnitState.BIG_FORBID) == 0) {
			logger.error("异常状态限制不能施放大招timing[{}]", timing);
			return null;
		}
		if (unique != null && getValue(UnitValue.MP) == Constant.MP_MAX
				&& getBattle().getRoundTime() > Constant.UNIQUE_TIME && !isDead()) {
			if (current != null) {
				// 检查能否打断当前技能的施放 TODO 跟前端商议暂时取消打断验证
				// if (!all.get(current).canBreak()) {
				// throw new FightException(FightExceptionCode.JUSTIFY_FAILED);
				// } else {
				all.get(current).interrupt();
				// }
			}
			removeNextOp();
			current = unique;
			nextTiming = null;
			return UniqueAction.valueOf(this, all.get(unique));
		}
		// throw new FightException(FightExceptionCode.JUSTIFY_FAILED); 如果直接判定认证失败,不太友好可以默认跳过
		logger.error("未达到大招施放要求(mp不足等)单位[{}]timing[{}]", baseId, timing);
		return null;
	}

	// private void setSkillExecuteId() {
	// for (Skill skill : all.values()) {
	// skill.setExecuteId(id);
	// }
	// }

	/** 重置战斗单元数值信息 */
	public void updateValues(Map<UnitValue, Integer> values) {
		this.values.clear();
		this.values.putAll(values);
		this.origin = new HashMap<>(values);
		this.origin.put(UnitValue.HP, values.get(UnitValue.HP_MAX));
		this.origin.put(UnitValue.MP, 0);

	}

	@Override
	public String toString() {
		return "[id=" + id + ", level=" + level + ", position=" + position + ", state=" + state + ", buffStates="
				+ buffStates + ", nextOp=" + nextOp + ", nextTiming=" + nextTiming + ", current=" + current
				+ ", counter=" + counter + "]";
	}

	// Getter and Setter ...

	public Fighter getOwner() {
		return owner;
	}

	protected void setOwner(Fighter owner) {
		this.owner = owner;
	}

	public short getId() {
		return id;
	}

	protected void setId(short id) {
		this.id = id;
	}

	public int getLevel() {
		return level;
	}

	public Position getPosition() {
		return position;
	}

	public Model getModel() {
		return model;
	}

	public Long getNextTiming() {
		return nextTiming;
	}

	public void setNextTiming(long nextTiming) {
		this.nextTiming = nextTiming;
	}

	public MoveType getMoveType() {
		return moveType;
	}

	/** 目前如果需要克隆,统一在外部克隆 */
	public HashMap<UnitValue, Integer> getValues() {
		return values;
	}

	/** 重置战斗单元数值信息 */
	public void setValues(HashMap<UnitValue, Integer> values) {
		this.values = values;
	}

	public String getNormal() {
		return normal;
	}

	public String[] getSkills() {
		return skills;
	}

	public String getUnique() {
		return unique;
	}

	public HashMap<Stage, String> getStages() {
		return stages;
	}

	String getCurrent() {
		return current;
	}

	void setCurrent(String current) {
		this.current = current;
	}

	public int getFight() {
		return fight;
	}

	public void setFighter(Fighter fighter) {
		// id = fighter.selfIncrease();
		owner = fighter;
	}

	/** 配置技能执行时拥有者 */
	public void setSKillExecutor() {
		for (Skill skill : all.values()) {
			skill.setOwner(this); // TODO 临时
			skill.setExecuteId(id);
		}
	}

	public boolean isManual() {
		return isManual;
	}

	public void setManual(boolean isManual) {
		this.isManual = isManual;
	}

	public void setFight(int fight) {
		this.fight = fight;
	}

	public String getBaseId() {
		return baseId;
	}

	public int getState() {
		return state;
	}

	public HashMap<UnitValue, Integer> getOrigin() {
		return origin;
	}

	@Override
	public int compareTo(Unit o) {
		UnitType t1 = this.getModel().getType();
		UnitType t2 = o.getModel().getType();
		int ret = t1.compareTo(t2);
		if (ret != 0) {
			return ret;
		}
		ret = this.getModel().getOrder() - o.getModel().getOrder();
		if (ret != 0) {
			return ret;
		}
		return this.getId() - o.getId();
	}

	/** 重新构建Unit的构造方法 */
	public static Unit valueOf(String baseId, int level, Model model, Map<UnitValue, Integer> values, MoveType moveType,
			String[] skills, String unique, Map<Stage, String> stages, String normal, Map<String, Skill> all, int fight) {
		Unit unit = new Unit();
		unit.baseId = baseId;
		unit.level = level;
		unit.model = model;
		unit.values = new HashMap<>(values);
		unit.origin = new HashMap<>(values);
		unit.origin.put(UnitValue.HP, values.get(UnitValue.HP_MAX));
		unit.origin.put(UnitValue.MP, 0);
		unit.moveType = moveType;
		unit.skills = skills;
		unit.unique = unique;
		unit.normal = normal;
		unit.stages = new HashMap<>(stages);
		unit.all = new HashMap<>();
		for (Skill skill : all.values()) {
			unit.all.put(skill.getId(), skill);
		}
		unit.fight = fight;
		return unit;
	}

}
