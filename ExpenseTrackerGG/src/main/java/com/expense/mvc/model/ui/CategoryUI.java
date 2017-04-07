package com.expense.mvc.model.ui;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.expense.mvc.model.entity.Category;
import com.expense.utils.Utils;

public class CategoryUI implements java.io.Serializable, Comparable<CategoryUI> {
	private static final long serialVersionUID = 1L;

	private static final String SEP = " ~ ";

	private int categoryId;
	private String mainCategory = new String("");
	private String subCategory = new String("");
	private char status;
	private Short displayOrder;

	public CategoryUI() {
	}

	public CategoryUI(Category category) {
		Utils.copyBean(this, category);
	}

	public CategoryUI(String category) {
		this.mainCategory = category.split(SEP)[0];
		this.subCategory = category.split(SEP)[1];
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getMainCategory() {
		return mainCategory;
	}

	public void setMainCategory(String mainCategory) {
		this.mainCategory = mainCategory;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Short getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Short displayOrder) {
		this.displayOrder = displayOrder;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public String getCategoryDesc() {
		return this.mainCategory + SEP + this.subCategory;
	}

	@Override
	public int compareTo(CategoryUI o) {
		return this.displayOrder.compareTo(o.displayOrder);
	}

	@Override
	public int hashCode() {
		return categoryId;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		return categoryId == ((CategoryUI) o).categoryId;
	}
}
