package com.expense.mvc.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.type.Type;

public abstract class BaseDAO<T, ID extends Serializable> {

	protected Class<T> entityType;
	protected Class<ID> idType;
	protected SessionFactory sessionFactory;

	public BaseDAO() {
	}

	public BaseDAO(Class<T> entityType) {
		this.entityType = entityType;
	}

	public BaseDAO(Class<T> entityType, Class<ID> idType) {
		this.entityType = entityType;
	}

	protected SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	protected abstract void setSessionFactory(SessionFactory sessionFactory);

	// ********************************************************************************************
	// ************************************** DELETE METHODS **************************************

	public void delete(T entity) {
		sessionFactory.getCurrentSession().delete(entity);
		sessionFactory.getCurrentSession().flush();
	}

	@SuppressWarnings("unchecked")
	public void deleteById(ID id) {
		T entity = (T) sessionFactory.getCurrentSession().load(this.entityType, id);
		if (null != entity) {
			sessionFactory.getCurrentSession().delete(entity);
			sessionFactory.getCurrentSession().flush();
		}
	}

	public void deleteAll(Collection<T> list) {
		for (T entity : list) {
			sessionFactory.getCurrentSession().delete(entity);
		}
		sessionFactory.getCurrentSession().flush();
	}

	// ********************************************************************************************
	// *************************************** SAVE METHODS ***************************************

	public T save(T entity) {
		sessionFactory.getCurrentSession().saveOrUpdate(entity);
		sessionFactory.getCurrentSession().flush();
		return entity;
	}

	public List<T> saveAll(List<T> list) {
		for (T entity : list) {
			sessionFactory.getCurrentSession().saveOrUpdate(entity);
		}
		sessionFactory.getCurrentSession().flush();
		return list;
	}

	// ********************************************************************************************
	// *************************************** FIND METHODS ***************************************

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		return sessionFactory.getCurrentSession().createQuery("from " + this.entityType.getName()).list();
	}

	@SuppressWarnings("unchecked")
	public T findById(ID id) {
		return (T) sessionFactory.getCurrentSession().get(this.entityType, id);
	}

	@SuppressWarnings("unchecked")
	public List<T> findByParameters(String query, HashMap<String, Object> parms) {
		return sessionFactory.getCurrentSession().createQuery(query).setProperties(parms).list();
	}

	@SuppressWarnings("unchecked")
	public List<T> findByParameters(String query, Object[] parms, Type[] types) {
		return sessionFactory.getCurrentSession().createQuery(query).setParameters(parms, types).list();
	}

	@SuppressWarnings("unchecked")
	public List<T> findByQuery(String query) {
		return sessionFactory.getCurrentSession().createQuery(query).list();
	}
}
