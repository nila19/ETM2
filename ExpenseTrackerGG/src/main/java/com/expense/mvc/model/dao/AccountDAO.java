package com.expense.mvc.model.dao;

import java.util.HashMap;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.expense.mvc.model.BaseDAO;
import com.expense.mvc.model.entity.Account;

@Repository
public class AccountDAO extends BaseDAO<Account, Integer> {

	@Override
	@Autowired
	protected void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;

		entityType = Account.class;
		idType = Integer.class;
	}

	public List<Account> findAll(int dataKey) {
		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put("dataKey", dataKey);

		return findByParameters("from Account where dataKey = :dataKey order by status, displayOrder", parms);
	}

	public List<Account> findAllActive(int dataKey) {
		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put("dataKey", dataKey);
		parms.put("status", Account.Status.ACTIVE.status);

		return findByParameters("from Account where dataKey = :dataKey and status = :status order by displayOrder",
				parms);
	}

	public List<Account> find(int dataKey, int acctId) {
		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put("dataKey", dataKey);
		parms.put("acctId", acctId);

		return findByParameters("from Account where dataKey = :dataKey and accountId = :acctId", parms);
	}
}
