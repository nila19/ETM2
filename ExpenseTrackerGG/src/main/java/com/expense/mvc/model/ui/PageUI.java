package com.expense.mvc.model.ui;

import java.util.List;
import java.util.Map;

public class PageUI implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private int pageNum;
	private List<CategoryUI> categories;
	private List<MonthUI> months;

	// Month, SummaryUI
	private Map<Integer, SummaryUI> totals;
	// CategoryId, Month Seq, SummaryUI
	private Map<Integer, Map<Integer, SummaryUI>> data;

	public PageUI(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public List<CategoryUI> getCategories() {
		return categories;
	}

	public void setCategories(List<CategoryUI> categories) {
		this.categories = categories;
	}

	public List<MonthUI> getMonths() {
		return months;
	}

	public void setMonths(List<MonthUI> months) {
		this.months = months;
	}

	public Map<Integer, SummaryUI> getTotals() {
		return totals;
	}

	public void setTotals(Map<Integer, SummaryUI> totals) {
		this.totals = totals;
	}

	public Map<Integer, Map<Integer, SummaryUI>> getData() {
		return data;
	}

	public void setData(Map<Integer, Map<Integer, SummaryUI>> data) {
		this.data = data;
	}
}
