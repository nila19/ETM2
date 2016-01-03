package com.expense.mvc.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.expense.mvc.model.dao.AccountDAO;
import com.expense.mvc.model.dao.TallyHistoryDAO;
import com.expense.mvc.model.dao.TransactionDAO;
import com.expense.mvc.model.entity.Account;
import com.expense.mvc.model.entity.TallyHistory;
import com.expense.mvc.model.entity.Transaction;
import com.expense.mvc.model.ui.TransactionUI;
import com.expense.utils.FormatUtils;
import com.expense.utils.Props;

@Service
public class TallyService {

	@Autowired
	private AccountDAO accountDAO;

	@Autowired
	private TransactionDAO transactionDAO;

	@Autowired
	private TallyHistoryDAO tallyHistoryDAO;

	public enum Portfolio {
		CREDITS("CREDITS"), DEBITS("DEBITS"), BALANCES("BALANCES");

		public String type;

		private Portfolio(String type) {
			this.type = type;
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void tallyAccount(int accountId) {
		DecimalFormat df = FormatUtils.AMOUNT_NOCOMMA;

		Date dt = new Date();
		Account ac = accountDAO.findById(accountId);
		ac.setTallyBalance(Double.valueOf(df.format(ac.getBalanceAmt())));
		ac.setTallyDate(dt);

		accountDAO.save(ac);

		TallyHistory h = new TallyHistory();
		h.setDataKey(ac.getDataKey());
		h.setAccount(ac);
		h.setTallyDate(dt);
		h.setTallyBalance(ac.getBalanceAmt());
		tallyHistoryDAO.save(h);

		tallyExpenseItems(dt, ac.getTransForFromAccount());
		tallyExpenseItems(dt, ac.getTransForToAccount());
	}

	private void tallyExpenseItems(Date dt, Set<Transaction> trans) {
		for (Transaction t : trans) {
			if (!t.isTallied()) {
				t.setTallyDate(dt);
				t.setTallyInd(Transaction.Tally.YES.status);
				transactionDAO.save(t);
			}
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<TransactionUI> getTransactions(int accountId, boolean pending, int billId, int dataKey) {
		List<Transaction> trans = transactionDAO.findByAccount(dataKey, accountId, pending, billId);

		int LIST_EXPENSES_LIMIT = Integer.valueOf(Props.expense.getString("LIST.EXPENSES.LIMIT"));
		int i = 0;

		List<TransactionUI> uis = new ArrayList<TransactionUI>();
		for (Transaction tran : trans) {
			if (i >= LIST_EXPENSES_LIMIT) {
				break;
			}
			uis.add(new TransactionUI(tran));
			i++;
		}
		return uis;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Map<String, String> calcPFBalance(int dataKey) {
		List<Account> accts = accountDAO.findAllActive(dataKey);

		Map<String, String> map = new HashMap<String, String>();
		double credits = 0;
		double debits = 0;

		for (Account ac : accts) {
			if (ac.getType() == Account.Type.CASH.type) {
				credits += ac.getBalanceAmt();
			} else {
				debits += ac.getBalanceAmt();
			}
		}

		map.put(Portfolio.CREDITS.type, FormatUtils.AMOUNT.format(credits));
		map.put(Portfolio.DEBITS.type, FormatUtils.AMOUNT.format(debits));
		map.put(Portfolio.BALANCES.type, FormatUtils.AMOUNT.format(credits - debits));
		return map;
	}
}
