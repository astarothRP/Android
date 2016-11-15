package com.pfizer.piloto.instruments;

public abstract class CException extends Exception {

	public final int errorCode;
	public final String message;

	protected CException(Throwable e) {
		super(e);
		boolean isInstanceOf = e instanceof CException;
		errorCode = isInstanceOf ? ((CException) e).errorCode : -1;
		message = isInstanceOf ? ((CException) e).message : "";
	}

}
