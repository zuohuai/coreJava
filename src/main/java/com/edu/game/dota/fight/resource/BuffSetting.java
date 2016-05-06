package com.edu.game.dota.fight.resource;

import java.util.HashMap;

import com.edu.game.dota.fight.model.UnitValue;
import com.edu.game.dota.fight.service.buff.BuffState;
import com.edu.game.dota.fight.service.buff.BuffType;
import com.edu.game.dota.fight.service.buff.StateCtxKeys;
import com.edu.game.resource.Validate;
import com.edu.game.resource.anno.Id;
import com.edu.game.resource.anno.Resource;
import com.edu.utils.json.JsonUtils;

/**
 * buff效果配置表
 * @author shenlong
 */
@Resource("dota")
public class BuffSetting implements Validate {

	/** 标识 */
	@Id
	private String id;
	/** 战报编码 */
	private short code;
	/** buff类型 */
	private BuffType type;
	/** buff状态 */
	private BuffState state;

	@SuppressWarnings("unchecked")
	@Override
	public boolean isValid() {
		if (state.getContent() != null && state.getContent().containsKey(StateCtxKeys.ALTERS)) {
			// 配置buff 对应的属性修改内容
			HashMap<UnitValue, Integer> alterMap = JsonUtils.string2Map((String) state.getContent().get(StateCtxKeys.ALTERS), UnitValue.class, Integer.class, HashMap.class);
			state.getContent().put(StateCtxKeys.ALTERS, alterMap);
		}
		return true;
	}

	public String getId() {
		return id;
	}

	public short getCode() {
		return code;
	}

	public BuffType getType() {
		return type;
	}

	public BuffState getState() {
		return state.clone(id);
	}

}
