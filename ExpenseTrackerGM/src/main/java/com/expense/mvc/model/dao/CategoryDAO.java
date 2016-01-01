package com.expense.mvc.model.dao;

import java.util.HashMap;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.expense.mvc.model.BaseDAO;
import com.expense.mvc.model.entity.Category;

@Repository
public class CategoryDAO extends BaseDAO<Category, Integer> {

	@Override
	@Autowired
	protected void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;

		entityType = Category.class;
		idType = Integer.class;
	}

	public List<Category> findAllActive(int dataKey) {
		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put("dataKey", dataKey);
		parms.put("status", Category.Status.ACTIVE.status);

		return findByParameters("from Category where dataKey = :dataKey and status = :status order by displayOrder",
				parms);
	}
}
