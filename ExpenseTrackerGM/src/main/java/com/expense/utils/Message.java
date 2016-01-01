package com.expense.utils;

public class Message {
	private char type;
	private String message;

	public enum Type {
		WARNING('W'), ERROR('E');

		public char type;

		private Type(char type) {
			this.type = type;
		}
	}

	public Message(char type, String message) {
		this.setType(type);
		this.setMessage(message);
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
