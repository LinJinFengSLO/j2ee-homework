package com.asafandben.dal.dao;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TransactionRequiredException;

import com.asafandben.dal.searchcriteria.ISearchCriteria;

public interface IGenericDao <T, PK extends Serializable> {
	
	/** Find by primary key **/
	/** Throws:
	 *	IllegalArgumentException - if the first argument does not denote an entity type or the second argument is not a valid type for that entity's primary key
	 */			
	T find(PK key) throws IllegalArgumentException;
	
	/** Merge the state of the given entity into the current persistence context **/
	/** Throws:
	 *	IllegalArgumentException - if instance is not an entity or is a removed entity
	 *	TransactionRequiredException - if invoked on a container-managed entity manager of type PersistenceContextType.TRANSACTION and there is no transaction.
	 */		
	T merge(T t) throws IllegalArgumentException, TransactionRequiredException;
	
    /** Refresh the state of the instance from the database, overwriting changes made to the entity, if any */	
	/** Throws:
	 *	IllegalArgumentException - if not an entity or entity is not managed
	 *	EntityNotFoundException - if the entity no longer exists in the database.
	 *	TransactionRequiredException - if invoked on a container-managed entity manager of type PersistenceContextType.TRANSACTION and there is no transaction.
	 */	
	void refresh(T t) throws IllegalArgumentException, EntityNotFoundException, TransactionRequiredException;
	
    /** Remove the entity instance */	
	/** Throws:
	 *	IllegalArgumentException - if not an entity or if a detached entity
	 *	TransactionRequiredException - if invoked on a container-managed entity manager of type PersistenceContextType.TRANSACTION and there is no transaction.
	 */
	void remove(T t) throws IllegalArgumentException, TransactionRequiredException;
	
    /** Make an entity instance managed and persistent */
	/** Throws:
	 *	IllegalArgumentException - if not an entity
	 *	EntityExistsException - if the entity already exists. (The EntityExistsException may be thrown when the persist operation is invoked, or the EntityExistsException or another PersistenceException may be thrown at flush or commit time.)
	 *	TransactionRequiredException - if invoked on a container-managed entity manager of type PersistenceContextType.TRANSACTION and there is no transaction.
	 */
	void persist(T t) throws IllegalArgumentException, EntityExistsException, TransactionRequiredException;
	
	/** Search for entity by executing query with the specified search criteria/s **/
	/** Throws:
	 *	IllegalArgumentException - if query string is not valid
	 */			
	ArrayList<T> search(ISearchCriteria[] searchCriteria) throws IllegalArgumentException;
}