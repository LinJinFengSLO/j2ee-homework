package com.asafandben.dal.dao;

import java.io.Serializable;
import java.util.ArrayList;

import com.asafandben.dal.searchcriteria.ISearchCriteria;

public interface IGenericDao <T, PK extends Serializable> {
	/** Find by primary key **/
	T find(PK key);
	
	/** Merge the state of the given entity into the current persistence context **/
	T merge(T t);
	
    /** Refresh the state of the instance from the database, overwriting changes made to the entity, if any */	
	void refresh(T t);
	
    /** Remove the entity instance */	
	void remove(T t);
	
    /** Make an entity instance managed and persistent */
	void persist(T t);
	
	/** Search for entity by executing query with the specified search criteria/s **/
	ArrayList<T> search(ISearchCriteria[] searchCriteria);
}