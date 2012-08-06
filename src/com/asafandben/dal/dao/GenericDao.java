package com.asafandben.dal.dao;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.EntityManager;

import com.asafandben.dal.searchcriteria.ISearchCriteria;

public class GenericDao<T, PK extends Serializable> implements IGenericDao<T, PK> {

	private EntityManager entityManager;  
	private Class<T> entityClass;
	
	public GenericDao(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	@Override
	public T find(PK key) {
		return entityManager.find(entityClass, key);
	}

	@Override
	public T merge(T t) {
		return entityManager.merge(t);
	}

	@Override
	public void refresh(T t) {
		entityManager.refresh(t);
	}

	@Override
	public void remove(T t) {
		entityManager.remove(t);
	}

	@Override
	public void persist(T t) {
		entityManager.persist(t);
	}

	@Override 
	public ArrayList<T> search(ISearchCriteria[] searchCriteria) {
		// Basic query string
		String selectClause = "SELECT t FROM " + entityClass.getName() + " t";

		// building where clause
		String whereClause = "";
		if (searchCriteria != null) {
			whereClause = " WHERE t." + searchCriteria[0].getQueryString();
			for (int i=1; i < searchCriteria.length; ++i) {
				whereClause += " AND " + searchCriteria[i].getQueryString();
			}
		}
	
		return (ArrayList<T>) entityManager.createQuery(selectClause + whereClause).getResultList();
	}

}
