package com.edu.game.dota.fight.resource;

import com.edu.game.dota.fight.model.Model;
import com.edu.game.dota.fight.model.UnitType;
import com.edu.game.resource.anno.Id;
import com.edu.game.resource.anno.Resource;

/**
 * 模型配置信息
 * @author Frank
 */
@Resource("dota")
public class ModelSetting {

	/** 模型标识 */
	@Id
	private short id;
	/** 战斗单位类型 */
	private UnitType type;
	/** 排序值 */
	private int order;

	/** 模型对象缓存 */
	private volatile Model model;

	/**
	 * 获取对应的模型对象
	 * @return
	 */
	public Model getModel() {
		if (model == null) {
			synchronized (this) {
				if (model == null) {
					model = Model.valueOf(id, type, order);
				}
			}
		}
		return model;
	}

	// Getter and Setter ...

	public short getId() {
		return id;
	}

	void setId(short id) {
		this.id = id;
	}

	public UnitType getType() {
		return type;
	}

	void setType(UnitType type) {
		this.type = type;
	}

	public int getOrder() {
		return order;
	}

	void setOrder(int order) {
		this.order = order;
	}
}
