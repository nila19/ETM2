package com.expense.mvc.model.ui;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.expense.mvc.model.entity.Transaction;
import com.expense.utils.FormatUtils;

public class SummaryUI implements java.io.Serializable, Comparable<SummaryUI> {
	private static final long serialVersionUID = 1L;

	private CategoryUI category;
	private MonthUI month;
	private Date transMonth;
	private double amount;
	private String fAmount;
	private int count;

	public SummaryUI() {
	}

	public SummaryUI(CategoryUI cat, MonthUI month, Transaction t) {
		this.category = cat;
		this.month = month;
		this.transMonth = t.getTransMonth();
		this.amount = t.getAmount();
		this.fAmount = FormatUtils.AMOUNT.format(this.amount);
		this.count = 1;
	}

	public CategoryUI getCategory() {
		return this.category;
	}

	public void setCategory(CategoryUI category) {
		this.category = category;
	}

	public Date getTransMonth() {
		return this.transMonth;
	}

	public void setTransMonth(Date transMonth) {
		this.transMonth = transMonth;
	}

	public double getAmount() {
		return this.amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
		this.fAmount = FormatUtils.AMOUNT.format(this.amount);
	}

	public MonthUI getMonth() {
		return this.month;
	}

	public void setMonth(MonthUI month) {
		this.month = month;
	}

	public int getCount() {
		return this.count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getfAmount() {
		return this.fAmount;
	}

	public void setfAmount(String fAmount) {
		this.fAmount = fAmount;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int compareTo(SummaryUI o) {
		int i = this.category.getDisplayOrder().compareTo(o.category.getDisplayOrder());
		if (i != 0) {
			return i;
		}

		if (this.transMonth != null && o.transMonth != null) {
			return this.transMonth.compareTo(o.transMonth);
		}

		return 0;
	}
}
