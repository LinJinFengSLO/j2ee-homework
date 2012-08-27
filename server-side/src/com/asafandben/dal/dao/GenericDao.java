package com.asafandben.dal.dao;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import javax.persistence.LockModeType;
import javax.persistence.Persistence;
import javax.persistence.TransactionRequiredException;

import org.hibernate.Hibernate;

import com.asafandben.dal.searchcriteria.ISearchCriteria;

/**	GenericDAO is mainly a wrapper for the EE built-in DAO, the EntityManager, but it manages access only for entities of type T.
 *	GenericDAO offers enhanced interface and thread safety on all operations 
 *  @param T - an entity type. **/
public class GenericDao<T, PK extends Serializable> implements IGenericDao<T, PK> {

	// Static Members & Methods
	private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("TaskManagement");
	
	public static void closeEntityManagerFactory() {
		synchronized(entityManagerFactory) {
			if (entityManagerFactory.isOpen())
				entityManagerFactory.close();
		}
		
	}
	
	// Members
	private Class<T> entityClass;
	
	// Constructors
	public GenericDao(Class<T> entityClass) {
		this.entityClass = entityClass;
		
	}
	
	// Methods
	private EntityManager getEntityManager() {
		EntityManager entityManager;
		synchronized(entityManagerFactory) {
			// Should never fail since the entityManagerFactory is closed only in server shutdown.
			entityManager = entityManagerFactory.createEntityManager();
			
		}
		return entityManager;
	}
	
	@Override
	public synchronized T find(PK key) throws IllegalStateException, IllegalArgumentException {
		EntityManager entityManager = getEntityManager();
	
		try {
			return entityManager.find(entityClass, key);
		}
		finally {
			// TODO: Possibly close?
			//entityManager.close();
		}
	}

	@Override
	public synchronized T merge(T t) throws IllegalStateException, IllegalArgumentException, TransactionRequiredException {
		EntityManager entityManager = getEntityManager();
				
		try {
			entityManager.getTransaction().begin();
			T mergedT = entityManager.merge(t);
			entityManager.getTransaction().commit();	
			return mergedT;		
		} finally {
			// TODO: Possibly close?
			//entityManager.close();
		}
	}

	@Override
	public synchronized void persist(T t) throws IllegalStateException, IllegalArgumentException, EntityExistsException, TransactionRequiredException {
		EntityManager entityManager = getEntityManager();
		
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(t);
			entityManager.getTransaction().commit();	
		} finally {
			// TODO: Possibly close?
			//entityManager.close();
		}
	}	
	
	@Override
	public synchronized void refresh(T t) throws IllegalStateException, IllegalArgumentException, EntityNotFoundException, TransactionRequiredException {
		EntityManager entityManager = getEntityManager();
		
		try {
			entityManager.refresh(t);
		} finally {
			// TODO: Possibly close?
			//entityManager.close();
		}		
	}

	@Override
	public synchronized void remove(T t) throws IllegalStateException, IllegalArgumentException, TransactionRequiredException {
		EntityManager entityManager = getEntityManager();
		
		try {
			entityManager.getTransaction().begin();
			entityManager.remove(t);
			entityManager.getTransaction().commit();	
		} finally {
			// TODO: Possibly close?
			//entityManager.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override 
	public synchronized ArrayList<T> search(ISearchCriteria<T>[] searchCriterias) throws IllegalStateException, IllegalArgumentException {
		EntityManager entityManager = getEntityManager();

		try {		
			// Build query string
			String queryString = "SELECT t FROM " + entityClass.getName() + " t";
			String whereClause = "";
			if (searchCriterias != null) {
				whereClause = " WHERE t." + searchCriterias[0].getQueryString();
				for (int i=1; i < searchCriterias.length; ++i) {
					whereClause += " AND " + searchCriterias[i].getQueryString();
				}
			}
			queryString += whereClause;

			// Running the query (all entities read by query are locked for read)
			return (ArrayList<T>) entityManager.createQuery(queryString).getResultList();
		} finally {
			// TODO: Possibly close?
			//entityManager.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<T> getAll() {
		EntityManager entityManager = getEntityManager();

		try {		
			// Build query string
			String queryString = "SELECT t FROM " + entityClass.getName() + " t";
			return (ArrayList<T>) entityManager.createQuery(queryString).getResultList();
		} finally {
			// TODO: Possibly close?
			//entityManager.close();
		}
	}
}
