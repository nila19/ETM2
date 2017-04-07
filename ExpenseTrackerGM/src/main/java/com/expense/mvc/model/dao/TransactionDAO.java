package com.expense.mvc.model.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.expense.mvc.model.BaseDAO;
import com.expense.mvc.model.entity.Transaction;
import com.expense.mvc.model.ui.TransactionUI;
import com.expense.utils.FormatUtils;

@Repository
public class TransactionDAO extends BaseDAO<Transaction, Integer> {
	private static final double PCT_75 = 0.75;
	private static final double PCT_125 = 1.25;

	@Override
	@Autowired
	protected void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;

		entityType = Transaction.class;
		idType = Integer.class;
	}

	public List<Transaction> findAll(int dataKey) {
		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put("dataKey", dataKey);

		return findByParameters("from Transaction where dataKey = :dataKey order by transSeq desc", parms);
	}

	public List<Transaction> findByAccount(int dataKey, int accountId, boolean pending, int billId) {
		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put("dataKey", dataKey);
		parms.put("accountId", accountId);
		parms.put("billId", billId);
		parms.put("tallyInd", Transaction.Tally.NO.status);

		String query = "from Transaction where dataKey = :dataKey";
		if (billId > 0) {
			query += " and (fromBill.billId = :billId or toBill.billId = :billId )";
		} else if (accountId > 0 && pending) {
			query += " and ((fromAccount.accountId = :accountId and tallyInd = :tallyInd)"
					+ " or (toAccount.accountId = :accountId and tallyInd = :tallyInd))";
		} else if (accountId > 0) {
			query += " and (fromAccount.accountId = :accountId or toAccount.accountId = :accountId )";
		} else if (pending) {
			query += " and ((fromAccount.accountId != 0 and tallyInd = :tallyInd)"
					+ " or (toAccount.accountId != 0 and tallyInd = :tallyInd))";
		}
		query += " order by transSeq desc";

		return findByParameters(query, parms);
	}

	public List<Transaction> findForSearch(int dataKey, TransactionUI ui) {
		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put("dataKey", dataKey);
		parms.put("accountId", ui.getFromAccountId());
		parms.put("categoryId", ui.getCategoryId());
		parms.put("description", "%" + ui.getDescription() + "%");
		parms.put("amount_75", ui.getAmount() * TransactionDAO.PCT_75);
		parms.put("amount_125", ui.getAmount() * TransactionDAO.PCT_125);
		if (ui.getEntryMonth() != null) {
			parms.put("entryMonth", FormatUtils.yyyyMMdd.format(ui.getEntryMonth()));
		}
		if (ui.getTransMonth() != null) {
			parms.put("transMonth", FormatUtils.yyyyMMdd.format(ui.getTransMonth()));
		}
		parms.put("adhocInd", ui.getAdhocInd());
		parms.put("adjustInd", ui.getAdjustInd());

		String query = "from Transaction where dataKey = :dataKey";

		if (ui.getFromAccountId() > 0) {
			query += " and (fromAccount.accountId = :accountId or toAccount.accountId = :accountId )";
		}

		if (ui.getCategoryId() > 0) {
			query += " and category.categoryId = :categoryId";
		}

		if (StringUtils.isNotBlank(ui.getDescription())) {
			query += " and description like :description";
		}

		if (ui.getAmount() > 0) {
			query += " and (amount between :amount_75 and :amount_125)";
		}

		if (ui.getEntryMonth() != null) {
			query += " and strEntryMonth = :entryMonth";
		}

		if (ui.getTransMonth() != null) {
			query += " and strTransMonth = :transMonth";
		}

		if (ui.getAdhocInd() == Transaction.Adhoc.YES.type || ui.getAdhocInd() == Transaction.Adhoc.NO.type) {
			query += " and adhocInd = :adhocInd";
		}

		if (ui.getAdjustInd() == Transaction.Adjust.YES.type || ui.getAdjustInd() == Transaction.Adjust.NO.type) {
			query += " and adjustInd = :adjustInd";
		}
		query += " order by transSeq desc";

		return findByParameters(query, parms);
	}

	public List<Transaction> findForMonthlySummary(int dataKey, char adhoc) {
		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put("dataKey", dataKey);
		parms.put("adjustInd", Transaction.Adjust.NO.type);
		parms.put("adhocInd", adhoc);

		String query = "from Transaction where dataKey = :dataKey and adjustInd = :adjustInd";
		if (adhoc == Transaction.Adhoc.YES.type || adhoc == Transaction.Adhoc.NO.type) {
			query += " and adhocInd = :adhocInd";
		}
		query += " order by transSeq desc";

		return findByParameters(query, parms);
	}

	public List<Transaction> findForForecast(int dataKey) {
		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put("dataKey", dataKey);
		parms.put("adjustInd", Transaction.Adjust.NO.type);
		parms.put("adhocInd", Transaction.Adhoc.NO.type);
		// Get Transactions for the last 3 months excluding the current month.
		Date curr_month = DateUtils.truncate(new Date(), Calendar.MONTH);
		parms.put("beginMon", FormatUtils.yyyyMMdd.format(DateUtils.addMonths(curr_month, -3)));
		parms.put("endMon", FormatUtils.yyyyMMdd.format(DateUtils.addMonths(curr_month, -1)));

		String query = "from Transaction where dataKey = :dataKey and adjustInd = :adjustInd and adhocInd = :adhocInd and strTransMonth between :beginMon and :endMon";
		return findByParameters(query, parms);
	}

	@SuppressWarnings("unchecked")
	public List<String> findAllEntryMonths(int dataKey) {
		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put("dataKey", dataKey);

		String query = "select distinct strEntryMonth from Transaction where dataKey = :dataKey order by ENTRY_MONTH desc";
		return sessionFactory.getCurrentSession().createQuery(query).setProperties(parms).list();
	}

	@SuppressWarnings("unchecked")
	public List<String> findAllTransMonths(int dataKey) {
		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put("dataKey", dataKey);

		String query = "select distinct strTransMonth from Transaction where dataKey = :dataKey order by TRANS_MONTH desc";
		return sessionFactory.getCurrentSession().createQuery(query).setProperties(parms).list();
	}

	@SuppressWarnings("unchecked")
	public List<String> findAllDescription(int dataKey) {
		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put("dataKey", dataKey);

		String query = "select description from Transaction where dataKey = :dataKey group by DESCRIPTION order by COUNT(*) desc";
		return sessionFactory.getCurrentSession().createQuery(query).setProperties(parms).list();
	}
}
