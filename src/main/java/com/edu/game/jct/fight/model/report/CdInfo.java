package com.edu.game.jct.fight.model.report;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * 技能CD更新信息
 * @author Frank
 */
public class CdInfo {

	/** 战斗单元标识 */
	private String id;
	/** 技能标识:冷却值 */
	private Map<String, Integer> cds;

	// Getter and Setter ...

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, Integer> getCds() {
		return cds;
	}

	public void setCds(Map<String, Integer> cds) {
		this.cds = cds;
	}

	/** 构造方法 */
	public static CdInfo valueOf(String id, Collection<SkillState> values) {
		CdInfo result = new CdInfo();
		result.id = id;
		Map<String, Integer> cds = new HashMap<String, Integer>(values.size());
		for (SkillState state : values) {
			cds.put(state.getId(), state.getCd());
		}
		result.cds = cds;
		return result;
	}
}
