package com.edu.game.jct.fight.exception;

import com.edu.utils.ManagedException;

/**
 * 战斗模块的父类
 * @see ManagedException
 * @author administrator
 */
public class FightException extends ManagedException {

	private static final long serialVersionUID = -4290826470366832731L;

	public FightException(int code, String message, Throwable cause) {
		super(code, message, cause);
	}

	public FightException(int code, String message) {
		super(code, message);
	}

	public FightException(int code, Throwable cause) {
		super(code, cause);
	}

	public FightException(int code) {
		super(code);
	}

}
