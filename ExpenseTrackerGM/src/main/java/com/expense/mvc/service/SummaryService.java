package com.expense.mvc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.expense.mvc.helper.SummaryHelper;
import com.expense.mvc.model.dao.TransactionDAO;
import com.expense.mvc.model.entity.Transaction;
import com.expense.mvc.model.ui.CategoryUI;
import com.expense.mvc.model.ui.SummaryUI;
import com.expense.mvc.model.ui.TransactionUI;
import com.expense.utils.FormatUtils;
import com.expense.utils.Mapper;

@Service
public class SummaryService {

	@Autowired
	private LoginService loginService;

	@Autowired
	private TransactionDAO transactionDAO;

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<TransactionUI> search(TransactionUI ui, int dataKey) {
		List<Transaction> trans = this.transactionDAO.findForSearch(dataKey, ui);

		List<TransactionUI> uis = new ArrayList<TransactionUI>();
		for (Transaction tran : trans) {
			uis.add(new TransactionUI(tran));
		}
		return uis;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public SummaryHelper getSummary(char adhoc, boolean forecast, int dataKey) {
		// Get MonthlySummary
		List<Transaction> trans = this.transactionDAO.findForMonthlySummary(dataKey, adhoc);

		// Category, Month, SummaryUI
		Map<Integer, Map<Integer, SummaryUI>> master = Mapper.map2D_Summary(trans);

		// Get Forecast
		// Category, SummaryUI
		Map<Integer, SummaryUI> forecasts = forecast ? getForecast(dataKey) : null;

		List<CategoryUI> categories = this.loginService.getAllCategories(dataKey);

		SummaryHelper helper = new SummaryHelper(master, forecasts, categories);
		return helper;
	}

	// Category, SummaryUI
	private Map<Integer, SummaryUI> getForecast(int dataKey) {
		List<Transaction> trans = this.transactionDAO.findForForecast(dataKey);
		Map<Integer, SummaryUI> map = Mapper.map_Summary(trans);

		for (SummaryUI ui : map.values()) {
			ui.setAmount(ui.getAmount() / 3);
		}

		return map;
	}

	public String getTotals(List<TransactionUI> trans) {
		double total = 0;
		for (TransactionUI tui : trans) {
			total += tui.getAmount();
		}
		return FormatUtils.AMOUNT.format(total);
	}
}
