package com.expense.mvc.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class BaseEntity {

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
