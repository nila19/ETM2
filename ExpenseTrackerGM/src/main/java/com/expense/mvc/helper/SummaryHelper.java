package com.expense.mvc.helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;

import com.expense.mvc.model.ui.CategoryUI;
import com.expense.mvc.model.ui.MonthUI;
import com.expense.mvc.model.ui.PageUI;
import com.expense.mvc.model.ui.SummaryUI;
import com.expense.utils.Props;

public class SummaryHelper implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private int PAGE_SIZE = Integer.valueOf(Props.expense.getString("PAGE.SIZE"));

	// Category, Month, SummaryUI
	private Map<Integer, Map<Integer, SummaryUI>> master;
	// Category, SummaryUI
	private Map<Integer, SummaryUI> forecast;
	private List<CategoryUI> categories;

	// Month, SummaryUI
	private Map<Integer, SummaryUI> totals = new HashMap<Integer, SummaryUI>();;

	private List<MonthUI> months = new ArrayList<MonthUI>();

	// Pages start from 0. Behaves like index.
	private int pages;
	// private int page;

	private final String[] darkSet = { "sum-headerBrown", "sum-dataGrey" };
	private final String[] lightSet = { "sum-headerBrownLight", "sum-dataGreyLight" };

	public enum ColorSet {
		DARK('D'), LIGHT('L');

		public char type;

		private ColorSet(char type) {
			this.type = type;
		}
	}

	private String prevCategory = "";
	private ColorSet colorSet = ColorSet.DARK;

	public SummaryHelper(Map<Integer, Map<Integer, SummaryUI>> master, Map<Integer, SummaryUI> forecast,
			List<CategoryUI> categories) {
		this.master = master;
		this.forecast = forecast;
		this.categories = categories;

		processData();
	}

	// ********************************************************************************************
	// ************************************** Process Methods *************************************

	private void processData() {
		synchCategories();
		if (forecast != null) {
			forecast();
		}
		calcMonthlyTotal();
		calcYearlyTotal();
		calcYearlySummary();
		calcMonthsList();
	}

	private void synchCategories() {
		HashMap<Integer, CategoryUI> cats = new HashMap<Integer, CategoryUI>();
		for (CategoryUI cat : categories) {
			cats.put(cat.getCategoryId(), cat);
		}

		for (Map<Integer, SummaryUI> map : master.values()) {
			for (SummaryUI sui : map.values()) {
				cats.put(sui.getCategory().getCategoryId(), sui.getCategory());
			}
		}

		categories = new ArrayList<CategoryUI>(cats.values());
		Collections.sort(categories);
	}

	private void forecast() {
		for (CategoryUI cat : categories) {
			Map<Integer, SummaryUI> map = master.get(cat.getCategoryId());
			if (map != null) {
				MonthUI mui = new MonthUI(DateUtils.truncate(new Date(), Calendar.MONTH));
				SummaryUI ui = map.get(mui.getSeq());
				SummaryUI fui = forecast.get(cat.getCategoryId());

				if (fui != null) {
					if (ui != null) {
						if (ui.getAmount() < fui.getAmount()) {
							ui.setAmount(fui.getAmount());
							ui.setCount(fui.getCount());
						}
					} else {
						fui.setMonth(mui);
						fui.setTransMonth(mui.getTransMonth());
						map.put(mui.getSeq(), fui);
					}
				}
			}
		}
	}

	private void calcMonthlyTotal() {
		// Add a column for current month.
		MonthUI mui = new MonthUI(DateUtils.truncate(new Date(), Calendar.MONTH));
		SummaryUI dui = new SummaryUI();
		dui.setMonth(mui);
		dui.setTransMonth(mui.getTransMonth());
		totals.put(mui.getSeq(), dui);

		// Calculate totals
		for (Map<Integer, SummaryUI> map : master.values()) {
			for (int mseq : map.keySet()) {
				SummaryUI ui = map.get(mseq);
				SummaryUI sui = totals.containsKey(mseq) ? totals.get(mseq) : new SummaryUI();
				sui.setMonth(ui.getMonth());
				sui.setTransMonth(ui.getTransMonth());
				sui.setAmount(sui.getAmount() + ui.getAmount());
				sui.setCount(sui.getCount() + ui.getCount());
				totals.put(mseq, sui);
			}
		}
	}

	private void calcYearlyTotal() {
		Map<Integer, SummaryUI> ytotals = new HashMap<Integer, SummaryUI>();
		for (int mseq : totals.keySet()) {
			SummaryUI mui = totals.get(mseq);
			if (mui.getMonth().getType() == MonthUI.Type.MONTH.type) {
				Date year = DateUtils.truncate(mui.getTransMonth(), Calendar.YEAR);
				MonthUI yui = new MonthUI(year, MonthUI.Type.YEAR);

				SummaryUI ui = ytotals.containsKey(yui.getSeq()) ? ytotals.get(yui.getSeq()) : new SummaryUI();

				ui.setMonth(yui);
				ui.setTransMonth(yui.getTransMonth());
				ui.setAmount(ui.getAmount() + totals.get(mseq).getAmount());
				ui.setCount(ui.getCount() + totals.get(mseq).getCount());
				ytotals.put(yui.getSeq(), ui);
			}
		}

		for (int yseq : ytotals.keySet()) {
			totals.put(yseq, ytotals.get(yseq));
		}
	}

	private void calcYearlySummary() {
		for (int catId : master.keySet()) {
			Map<Integer, SummaryUI> map = master.get(catId);
			Map<Integer, SummaryUI> ymap = new HashMap<Integer, SummaryUI>();

			for (int mseq : map.keySet()) {
				SummaryUI mui = map.get(mseq);
				Date year = DateUtils.truncate(mui.getTransMonth(), Calendar.YEAR);

				MonthUI yui = new MonthUI(year, MonthUI.Type.YEAR);
				SummaryUI ui = ymap.containsKey(yui.getSeq()) ? ymap.get(yui.getSeq()) : new SummaryUI();

				ui.setMonth(yui);
				ui.setCategory(mui.getCategory());
				ui.setTransMonth(yui.getTransMonth());
				ui.setAmount(ui.getAmount() + map.get(mseq).getAmount());
				ui.setCount(ui.getCount() + map.get(mseq).getCount());
				ymap.put(yui.getSeq(), ui);
			}

			for (int yseq : ymap.keySet()) {
				map.put(yseq, ymap.get(yseq));
			}
		}
	}

	private void calcMonthsList() {
		for (int mseq : totals.keySet()) {
			months.add(totals.get(mseq).getMonth());
		}

		// Sort in Desc order. Years will get aligned ahead of the corresponding months.
		Collections.sort(months);
		Collections.reverse(months);

		pages = (int) Math.ceil((float) months.size() / (float) PAGE_SIZE);
	}

	// ********************************************************************************************
	// ************************************ Pagination Methods ************************************

	public boolean isPageValid(int page) {
		return page >= 0 && page < this.pages;
	}

	public PageUI getDataForPg(int page) {
		PageUI ui = new PageUI(page);
		ui.setMonths(getMonthsForCurrPg(page));
		ui.setCategories(categories);
		ui.setTotals(totals);
		ui.setData(master);

		return ui;
	}

	private List<MonthUI> getMonthsForCurrPg(int page) {
		int from = page * PAGE_SIZE;
		int to = (from + PAGE_SIZE) > months.size() ? months.size() : (from + PAGE_SIZE);

		return months.subList(from, to);
	}

	public List<CategoryUI> getCategories() {
		return categories;
	}

	// ********************************************************************************************
	// ************************************ Formatting Methods ************************************

	public String[] getColorSet(CategoryUI cat) {
		if (!cat.getMainCategory().trim().equalsIgnoreCase(prevCategory)) {
			colorSet = (colorSet == ColorSet.DARK) ? ColorSet.LIGHT : ColorSet.DARK;
			prevCategory = cat.getMainCategory().trim();
		}
		return (colorSet == ColorSet.DARK) ? darkSet : lightSet;
	}
}
