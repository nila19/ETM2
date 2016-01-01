package com.expense.utils;

public class AjaxBean {

	private int code;
	private String message;
	private Object data;
	private Object header;

	public AjaxBean() {
	}

	public AjaxBean(Errors code, String message) {
		this.code = code.getCode();
		this.message = message;
	}

	public AjaxBean(Errors code, String message, Object data) {
		this.code = code.getCode();
		this.message = message;
		this.data = data;
	}

	public AjaxBean(Errors code, String message, Object data, Object header) {
		this.code = code.getCode();
		this.message = message;
		this.data = data;
		this.header = header;
	}

	public int getCode() {
		return code;
	}

	public void setCode(Errors code) {
		this.code = code.getCode();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Object getHeader() {
		return header;
	}

	public void setHeader(Object header) {
		this.header = header;
	}
}
