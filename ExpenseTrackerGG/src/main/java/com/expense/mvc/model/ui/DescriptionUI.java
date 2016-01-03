package com.expense.mvc.model.ui;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class DescriptionUI implements java.io.Serializable, Comparable<DescriptionUI> {
	private static final long serialVersionUID = 1L;

	private String description = "";

	public DescriptionUI(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int compareTo(DescriptionUI o) {
		return description.compareTo(o.description);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		return StringUtils.equalsIgnoreCase(description, ((DescriptionUI) o).description);
	}
}
