package org.yinyayun.env;

public class LTPRuntimeException extends RuntimeException {
	enum ERR_CODE {
		LINKED_ERR, OTHER
	}

	private static final long serialVersionUID = 1L;
	private ERR_CODE code;

	public LTPRuntimeException(String message) {
		super(message);
		this.code = ERR_CODE.OTHER;
	}

	public LTPRuntimeException(ERR_CODE code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public ERR_CODE getCode() {
		return this.code;
	}

	public boolean isLinedError() {
		return ERR_CODE.LINKED_ERR.equals(code);
	}
}
