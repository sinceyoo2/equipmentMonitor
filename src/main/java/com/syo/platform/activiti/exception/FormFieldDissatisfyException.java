package com.syo.platform.activiti.exception;

public class FormFieldDissatisfyException extends RuntimeException {
	
	private String fieldName;
	
	private String fieldNameCommon;
	
	private String cause;

	public FormFieldDissatisfyException(String fieldName, String cause) {
		super();
		this.fieldName = fieldName;
		this.cause = cause;
	}

	public String getExceptionMessage() {
		return fieldNameCommon + " : " +  cause;
	}
}
