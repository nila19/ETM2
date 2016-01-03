package com.expense.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.expense.mvc.model.entity.Transaction;
import com.expense.mvc.model.ui.CategoryUI;
import com.expense.mvc.model.ui.MonthUI;
import com.expense.mvc.model.ui.SummaryUI;

public class Mapper {

	// CategoryUI, MonthUI, SummaryUI
	public static Map<Integer, Map<Integer, SummaryUI>> map2D_Summary(List<Transaction> trans) {
		Map<Integer, Map<Integer, SummaryUI>> map = new HashMap<Integer, Map<Integer, SummaryUI>>();
		for (Transaction t : trans) {
			CategoryUI cat = new CategoryUI(t.getCategory());
			MonthUI month = new MonthUI(t.getTransMonth());

			SummaryUI sui = null;
			if (map.containsKey(cat.getCategoryId())) {
				Map<Integer, SummaryUI> map2 = map.get(cat.getCategoryId());

				if (map2.containsKey(month.getSeq())) {
					sui = map2.get(month.getSeq());
					sui.setAmount(sui.getAmount() + t.getAmount());
					sui.setCount(sui.getCount() + 1);
				} else {
					sui = new SummaryUI(cat, month, t);
					map2.put(month.getSeq(), sui);
				}
			} else {
				Map<Integer, SummaryUI> map2 = new HashMap<Integer, SummaryUI>();
				sui = new SummaryUI(cat, month, t);
				map2.put(month.getSeq(), sui);
				map.put(cat.getCategoryId(), map2);
			}
		}
		return map;
	}

	// CategoryUI, SummaryUI
	public static Map<Integer, SummaryUI> map_Summary(List<Transaction> trans) {
		Map<Integer, SummaryUI> map = new HashMap<Integer, SummaryUI>();
		for (Transaction t : trans) {
			CategoryUI cat = new CategoryUI(t.getCategory());

			SummaryUI sui = null;
			if (map.containsKey(cat.getCategoryId())) {
				sui = map.get(cat.getCategoryId());
				sui.setAmount(sui.getAmount() + t.getAmount());
				sui.setCount(sui.getCount() + 1);
			} else {
				sui = new SummaryUI(cat, null, t);
				map.put(cat.getCategoryId(), sui);
			}
		}
		return map;
	}

}
