package com.expense.utils;

public enum Errors {
	OK(0), WARNING(10), INPUT(1000), PROCESS(2000);

	private int code;

	Errors(int code) {
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}

	public String toString() {
		return String.valueOf(this.code);
	}
}
