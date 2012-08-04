package com.asafandben.dal.cache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
	
	
	private int maxSize = 100;
	
	public GenericCache() {
		initCache();
		initDAO();
	}
	
	public GenericCache(int maxSize) {
		this.maxSize = maxSize;
		initCache();
		initDAO();
	}

	/** initDAO - Initilaizes the DAO for Cache of type T.
	 * 
	 */
	private void initDAO() {
		dao = new GenericDao<T, PK>();
	}

	
	/** initCache - Initiate the ArrayList that contains the 
	 * 				cache, with maxSize member as the size.
	 */
	private void initCache() {
		cache = new ArrayList<T>(maxSize);
	}
	
	
	/** addToCache method adds object to cache, if the cache.size()
	 * is bigger than the cache's max size, it removes the least recent
	 * used object and saves it to the DB.
	 *  
	 * @param cacheableObject
	 */
	public void addToCache(T cacheableObject) {
		if (cache.size() == maxSize) {
			T removeObjectFromCache = cache.get(maxSize-1); 
			dao.persist(removeObjectFromCache);
			cache.remove(removeObjectFromCache);
		}
		cache.add(0, cacheableObject);
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
			dao.persist(object);
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

	
}
