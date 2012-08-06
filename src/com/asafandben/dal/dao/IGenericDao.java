package com.asafandben.dal.dao;

import java.io.Serializable;
import java.util.ArrayList;

import com.asafandben.dal.searchcriteria.ISearchCriteria;

public interface IGenericDao <T, PK extends Serializable> {
	
	/** Find by primary key **/
	/** Throws:
	 *	IllegalStateException - if this EntityManager has been closed.
	 *	IllegalArgumentException - if the first argument does not denote an entity type or the second argument is not a valid type for that entity's primary key
	 */			
	T find(PK key);
	
	/** Merge the state of the given entity into the current persistence context **/
	/** Throws:
	 *	IllegalStateException - if this EntityManager has been closed.
	 *	IllegalArgumentException - if instance is not an entity or is a removed entity
	 *	TransactionRequiredException - if invoked on a container-managed entity manager of type PersistenceContextType.TRANSACTION and there is no transaction.
	 */		
	T merge(T t);
	
    /** Refresh the state of the instance from the database, overwriting changes made to the entity, if any */	
	/** Throws:
	 *	IllegalStateException - if this EntityManager has been closed.
	 *	IllegalArgumentException - if not an entity or entity is not managed
	 *	TransactionRequiredException - if invoked on a container-managed entity manager of type PersistenceContextType.TRANSACTION and there is no transaction.
	 *	EntityNotFoundException - if the entity no longer exists in the database.
	 */	
	void refresh(T t);
	
    /** Remove the entity instance */	
	/** Throws:
	 *	IllegalStateException - if this EntityManager has been closed.
	 *	IllegalArgumentException - if not an entity or if a detached entity
	 *	TransactionRequiredException - if invoked on a container-managed entity manager of type PersistenceContextType.TRANSACTION and there is no transaction.
	 */
	void remove(T t);
	
    /** Make an entity instance managed and persistent */
	/** Throws:
	 *	EntityExistsException - if the entity already exists. (The EntityExistsException may be thrown when the persist operation is invoked, or the EntityExistsException or another PersistenceException may be thrown at flush or commit time.)
	 *	IllegalStateException - if this EntityManager has been closed.
	 *	IllegalArgumentException - if not an entity
	 *	TransactionRequiredException - if invoked on a container-managed entity manager of type PersistenceContextType.TRANSACTION and there is no transaction.
	 */
	void persist(T t);
	
	/** Search for entity by executing query with the specified search criteria/s **/
	ArrayList<T> search(ISearchCriteria[] searchCriteria);
}