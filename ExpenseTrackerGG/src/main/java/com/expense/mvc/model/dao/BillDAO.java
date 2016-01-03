package com.expense.mvc.model.dao;

import java.util.HashMap;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.expense.mvc.model.BaseDAO;
import com.expense.mvc.model.entity.Bill;

@Repository
public class BillDAO extends BaseDAO<Bill, Integer> {

	@Override
	@Autowired
	protected void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;

		entityType = Bill.class;
		idType = Integer.class;
	}

	public List<Bill> findAll(int dataKey) {
		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put("dataKey", dataKey);

		return findByParameters("from Bill where dataKey = :dataKey order by strBillDt desc", parms);
	}

	public List<Bill> findAllOpen(int dataKey) {
		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put("dataKey", dataKey);
		parms.put("status", Bill.Status.OPEN.status);

		return findByParameters("from Bill where dataKey = :dataKey and status = :status order by strBillDt desc",
				parms);
	}

	public List<Bill> findForAcct(int dataKey, int accId) {
		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put("dataKey", dataKey);
		parms.put("accId", accId);

		return findByParameters(
				"from Bill where dataKey = :dataKey and account.accountId = :accId order by strBillDt desc", parms);
	}
}