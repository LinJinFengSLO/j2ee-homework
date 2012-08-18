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
	public T find(PK key) throws IllegalStateException, IllegalArgumentException {
		EntityManager entityManager = getEntityManager();
	
		try {
			return entityManager.find(entityClass, key);
		}
		finally {
			entityManager.close();
		}
	}

	@Override
	public T merge(T t) throws IllegalStateException, IllegalArgumentException, TransactionRequiredException {
		EntityManager entityManager = getEntityManager();
		
		// locking the entity for write
		entityManager.lock(t, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
		EntityTransaction transaction = entityManager.getTransaction();
		
		try {
			transaction.begin();  
			T mergedT = entityManager.merge(t);
		    transaction.commit();
			return mergedT;		
		} finally {
		    entityManager.close();
		}
	}

	@Override
	public void persist(T t) throws IllegalStateException, IllegalArgumentException, EntityExistsException, TransactionRequiredException {
		EntityManager entityManager = getEntityManager();
		
		// locking the entity for write
		entityManager.lock(t, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
		EntityTransaction transaction = entityManager.getTransaction();
		
		try {
			transaction.begin();  
			entityManager.persist(t);
		    transaction.commit();	
		} finally {
		    entityManager.close();
		}
	}	
	
	@Override
	public void refresh(T t) throws IllegalStateException, IllegalArgumentException, EntityNotFoundException, TransactionRequiredException {
		EntityManager entityManager = getEntityManager();
		
		// locking the entity for read
		entityManager.lock(t, LockModeType.OPTIMISTIC);
		
		try {
			entityManager.refresh(t);
		} finally {
		    entityManager.close();
		}		
	}

	@Override
	public void remove(T t) throws IllegalStateException, IllegalArgumentException, TransactionRequiredException {
		EntityManager entityManager = getEntityManager();
		
		// locking the entity for write
		entityManager.lock(t, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
		EntityTransaction transaction = entityManager.getTransaction();
		
		try {
			transaction.begin();  
			entityManager.remove(t);
		    transaction.commit();	
		} finally {
		    entityManager.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override 
	public ArrayList<T> search(ISearchCriteria<T>[] searchCriterias) throws IllegalStateException, IllegalArgumentException {
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
			return (ArrayList<T>) entityManager.createQuery(queryString).setLockMode(LockModeType.OPTIMISTIC).getResultList();
		} finally {
		    entityManager.close();
		}
	}

}
