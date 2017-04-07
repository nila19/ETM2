package com.expense.mvc.model.ui;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	private String login;
	private String password;
	private boolean authenticated;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
