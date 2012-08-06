package com.asafandben.dal.cache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import com.asafandben.dal.dao.GenericDao;
import com.asafandben.dal.dao.IGenericDao;
import com.asafandben.dal.searchcriteria.ISearchCriteria;


/**
 * @author ben
 *
 * This class represents the cache system + proxy service to the DAL.
 * We maintain our own private cache, and would always prefer to take data
 * from it before going to the database.
 * 
 * @param <T> - The type of Cache we're starting (must have equivilent DAO).
 * @param <PK> - The primay key type of the DAO in the Database.
 */

public class GenericCache<T extends ICacheable<PK>, PK extends Serializable> {
	private List<T> cache;
	private IGenericDao<T, PK> dao;
	
	private Class entityClass;
	
	
	private int maxSize = 100;
	
	public GenericCache(Class entityClass) {
		initCache();
		initDAO(entityClass);
	}
	
	public GenericCache(Class entityClass, int maxSize) {
		this.maxSize = maxSize;
		initCache();
		initDAO(entityClass);
	}

	/** initDAO - Initilaizes the DAO for Cache of type T.
	 * 
	 */
	private void initDAO(Class entityClass) {
		this.entityClass = entityClass;
		dao = new GenericDao<T, PK>(entityClass);
	}

	
	/** initCache - Initiate the ArrayList that contains the 
	 * 				cache, with maxSize member as the size.
	 */
	private void initCache() {
		synchronized (cache) {
			cache = new ArrayList<T>(maxSize);
		}
	}
	
	
	/** addToCache method adds object to cache, if the cache.size()
	 * is bigger than the cache's max size, it removes the least recent
	 * used object and saves it to the DB.
	 *  
	 * @param cacheableObject
	 */
	private void addToCache(T cacheableObject) {
		if (cache.size() == maxSize) {
			T removeObjectFromCache = cache.get(maxSize-1); 
			
			try {
				dao.merge(removeObjectFromCache);
			}
			catch (EntityNotFoundException e) {
				// We should never be here, this would only happen
				// if we try to add to cache something which isn't
				// already in the DB.
				dao.persist(removeObjectFromCache);
			}
			
			synchronized (cache) {
				cache.remove(removeObjectFromCache);
			}
		}
		synchronized (cache) {
			cache.add(0, cacheableObject);
		}
	}
	
	/** the search method does 3 things:
	 * 	1. Saves all objects in the cache to the DAO.
	 * 	2. Sends the search to the DAO and get ArrayList result.
	 * 	3. Adds all search results to cache and returns the results to user.
	 * 
	 * @param searchCriteria - an array of ISearchCriteria
	 * @return an ArrayList<T> with the search results. 
	 */
	public ArrayList<T> search(ISearchCriteria[] searchCriteria) {
		persistAllCache();
		
		ArrayList<T> returnObjects = dao.search(searchCriteria);
		
		for (T addToCacheObject : returnObjects) {
			addToCache(addToCacheObject);
		}
		
		return returnObjects;
	}
	
	private void persistAllCache() {
		for (T object : cache) {
			try {
				dao.merge(object);
			}
			catch (EntityNotFoundException e) {
				dao.persist(object);
			}
		}
	}
	
	/**
	 * This method trys to find the object in cache, if it doesn't find it,
	 * we look the DAO for it.
	 * 
	 * @param key - the Object's Primary key we're trying to find
	 * @return the Object
	 */
	public T find(PK key) {
		T returnObject = null;
		
		for (T obj : cache) {
			if (obj.getID() == key) {
				returnObject = obj;
				break;
			}
		}
		
		if (returnObject == null) {
			returnObject = dao.find(key);
		}
		
		return returnObject;
		
	}
	

	/**
	 * This method removes object from cache and DAO.
	 * @param object
	 */
	public void remove(T object) {
		synchronized (cache) {
			try {
				cache.remove(object);
			}
			finally {}
		}
		
		dao.remove(object);
	}
	/**
	 * This method saves object to cache and DAO.
	 * @param object
	 */
	public void save(T object) {
		addToCache(object);
		dao.persist(object);
	}
	

	
}
