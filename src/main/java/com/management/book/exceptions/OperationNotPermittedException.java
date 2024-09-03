package com.management.book.exceptions;

public class OperationNotPermittedException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public OperationNotPermittedException(String msg) {
		super(msg);
	}

}
