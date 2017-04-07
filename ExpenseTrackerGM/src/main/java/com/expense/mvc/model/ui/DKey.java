package com.expense.mvc.model.ui;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class DKey implements Serializable {
	private static final long serialVersionUID = 1L;

	private int dKey;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public int getdKey() {
		return dKey;
	}

	public void setdKey(int dKey) {
		this.dKey = dKey;
	}
}
