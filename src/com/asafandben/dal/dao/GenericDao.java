package com.asafandben.dal.dao;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.EntityManager;

import com.asafandben.dal.searchcriteria.ISearchCriteria;

public class GenericDao<T, PK extends Serializable> implements IGenericDao<T, PK> {

	private EntityManager entityManager;  
	
	public GenericDao() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public T find(PK key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T merge(T t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refresh(T t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(T t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void persist(T t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<T> search(ISearchCriteria[] searchCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

}
