package com.edu.game.dota.fight.exception;

import com.edu.utils.ManagedException;

public class FightException extends ManagedException {

	private static final long serialVersionUID = -6191735740860294445L;

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
