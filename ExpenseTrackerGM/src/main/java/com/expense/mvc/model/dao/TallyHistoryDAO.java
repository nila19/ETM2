package com.expense.mvc.model.dao;

import java.util.HashMap;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.expense.mvc.model.BaseDAO;
import com.expense.mvc.model.entity.TallyHistory;

@Repository
public class TallyHistoryDAO extends BaseDAO<TallyHistory, Integer> {

	@Override
	@Autowired
	protected void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;

		entityType = TallyHistory.class;
		idType = Integer.class;
	}

	public List<TallyHistory> findAll(int accountId) {
		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put("accountId", accountId);

		return findByParameters("from TallyHistory where accountId = :accountId order by tallySeq desc", parms);
	}
}
