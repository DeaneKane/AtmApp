package com.deane.atmapp.exception;

public class InvalidBankDetailsException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public InvalidBankDetailsException(String message) {
		super(message);
	}
}
