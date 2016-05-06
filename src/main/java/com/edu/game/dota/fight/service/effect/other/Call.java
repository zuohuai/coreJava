package com.eyu.snm.module.fight.service.effect.other;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eyu.common.utils.json.JsonUtils;
import com.eyu.snm.module.fight.model.report.StageReport;
import com.eyu.snm.module.fight.resource.IdHolder;
import com.eyu.snm.module.fight.service.core.Fighter;
import com.eyu.snm.module.fight.service.core.Position;
import com.eyu.snm.module.fight.service.core.Unit;
import com.eyu.snm.module.fight.service.core.UnitFactory;
import com.eyu.snm.module.fight.service.effect.Effect;
import com.eyu.snm.module.fight.service.effect.EffectType;
import com.eyu.snm.module.fight.service.effect.position.Teleport;

/**
 * 在目标位置召唤效果
 * @author Kent
 */
@Component
public class Call implements Effect {

	@Autowired
	private IdHolder idHolder;

	private static final Logger logger = LoggerFactory.getLogger(Teleport.class);
	private static final String IDS = "ids";

	@Override
	public EffectType getType() {
		return EffectType.CALL;
	}

	@Override
	public void execute(StageReport ret, Map<String, Object> context, Map<String, Object> content, Unit owner, List<Unit> targets) {
		if (targets.size() > 1) {
			logger.error("召唤效果的释放目标数量[" + targets.size() + "]请检查配置是否正确");
		}

		Fighter fighter = owner.getFighter();
		Position position = targets.get(0).getPosition();
		int x = position.getX();
		int y = position.getY();
		// 召唤单位
		String[] ids = JsonUtils.string2Array((String) content.get(IDS), String.class);
		for (String id : ids) {
			Unit unit = UnitFactory.getInstance().getUnit(id);
			unit = fighter.join(x, y, unit);
			ret.addUnitReport(unit, idHolder);
		}
	}

}
