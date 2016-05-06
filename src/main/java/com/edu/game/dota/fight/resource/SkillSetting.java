package com.edu.game.dota.fight.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edu.game.dota.fight.service.core.SkillType;
import com.edu.game.resource.Validate;
import com.edu.game.resource.anno.Id;
import com.edu.game.resource.anno.Resource;

/**
 * 技能配置信息
 * @author Frank
 */
@Resource("dota")
public class SkillSetting implements Validate {

	private static final Logger logger = LoggerFactory.getLogger(SkillSetting.class);

	/** 技能标识 */
	@Id
	private String id;
	/** 技能类型 */
	private SkillType skilltype;
	/** 技能 */
	private short code;
	/** 允许施放的许可条件 */
	private String allow;
	/** 技能阶段配置 */
	private SkillStage[] stages;
	/** 大招黑屏时间 */
	private Integer bigSkillTime;

	@Override
	public boolean isValid() {
		if (stages == null) {
			logger.error("技能[{}]阶段配置错误", id);
			return false;
		}

		return true;
	}

	// Getter and Setter ...

	public String getId() {
		return id;
	}

	public short getCode() {
		return code;
	}

	public String getAllow() {
		return allow;
	}

	public SkillStage[] getStages() {
		return stages;
	}

	public SkillType getSkilltype() {
		return skilltype;
	}

	public Integer getBigSkillTime() {
		return bigSkillTime;
	}

}
