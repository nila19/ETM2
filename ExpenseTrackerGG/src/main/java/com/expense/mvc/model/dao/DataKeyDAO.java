package com.expense.mvc.model.dao;

import java.util.HashMap;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.expense.mvc.model.BaseDAO;
import com.expense.mvc.model.entity.DataKey;

@Repository
public class DataKeyDAO extends BaseDAO<DataKey, Integer> {

	@Override
	@Autowired
	protected void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;

		entityType = DataKey.class;
		idType = Integer.class;
	}

	public DataKey findDefault() {
		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put("defaultInd", DataKey.DefaultInd.YES.defaultInd);

		return findByParameters("from DataKey where defaultInd = :defaultInd", parms).get(0);
	}

	@Override
	public List<DataKey> findAll() {
		HashMap<String, Object> parms = new HashMap<String, Object>();
		return findByParameters("from DataKey order by strStartDt desc", parms);
	}

	public List<DataKey> findAllActive() {
		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put("status", DataKey.Status.ACTIVE.status);

		return findByParameters("from DataKey where status = :status order by strStartDt desc", parms);
	}
}
