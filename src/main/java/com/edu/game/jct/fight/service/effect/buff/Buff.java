package com.edu.game.jct.fight.service.effect.buff;

import com.edu.game.jct.fight.model.report.BuffReport;
import com.edu.game.jct.fight.service.core.Context;
import com.edu.game.jct.fight.service.core.Unit;

public interface Buff {

	BuffType getType();

	BuffReport add(BuffState state, Unit owner, Unit target, Context ctx);

	BuffReport remove(BuffState state, Unit owner, Context ctx);

	BuffReport update(BuffState state, Unit owner, Context ctx);

}
