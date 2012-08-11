package com.asafandben.dal.dao;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TransactionRequiredException;

import com.asafandben.dal.searchcriteria.ISearchCriteria;

public interface IGenericDao <T, PK extends Serializable> {
	
	/** Find entity by primary key.
	 *	@param key - the primary key type of entity T.
	 *	@return The entity with the given key, or null if no such entity.
	 *	@throws IllegalStateException - if the entity manager factory is closed.
	 *	@throws IllegalArgumentException - if the first argument does not denote an entity type or the second argument is not a valid type for that entity's primary key. */
	T find(PK key) throws IllegalStateException, IllegalArgumentException;
	
	/** Merge the state of the given entity into the current persistence context.
	 *  @param t - an entity instance.
	 *  @return The merged entity.
	 *	@throws IllegalStateException - if the entity manager factory is closed.
	 *	@throws IllegalArgumentException - if instance is not an entity or is a removed entity.
	 *	@throws TransactionRequiredException - if invoked on a container-managed entity manager of type PersistenceContextType.TRANSACTION and there is no transaction. */	
	T merge(T t) throws IllegalStateException, IllegalArgumentException, TransactionRequiredException;

    /** Make an entity instance managed and persistent.
	 *  @param t - an entity instance.
	 * 	@throws IllegalStateException - if the entity manager factory is closed.
	 *	@throws IllegalArgumentException - if not an entity.
	 *	@throws EntityExistsException - if the entity already exists. (The EntityExistsException may be thrown when the persist operation is invoked, or the EntityExistsException or another PersistenceException may be thrown at flush or commit time.)
	 *	@throws TransactionRequiredException - if invoked on a container-managed entity manager of type PersistenceContextType.TRANSACTION and there is no transaction. */
	void persist(T t) throws IllegalStateException, IllegalArgumentException, EntityExistsException, TransactionRequiredException;
	
    /** Refresh the state of the instance from the database, overwriting changes made to the entity, if any.
	 *  @param t - an entity instance.
     * 	@throws IllegalStateException - if the entity manager factory is closed.
	 *	@throws IllegalArgumentException - if not an entity or entity is not managed.
	 *	@throws EntityNotFoundException - if the entity no longer exists in the database.
	 *	@throws TransactionRequiredException - if invoked on a container-managed entity manager of type PersistenceContextType.TRANSACTION and there is no transaction. */	
	void refresh(T t) throws IllegalStateException, IllegalArgumentException, EntityNotFoundException, TransactionRequiredException;
	
    /** Remove the entity instance.
	 *  @param t - an entity instance.
     * 	@throws IllegalStateException - if the entity manager factory is closed.
	 *	@throws IllegalArgumentException - if not an entity or if a detached entity.
	 *	@throws TransactionRequiredException - if invoked on a container-managed entity manager of type PersistenceContextType.TRANSACTION and there is no transaction. */
	void remove(T t) throws IllegalStateException, IllegalArgumentException, TransactionRequiredException;

	/** Search for entity by executing query with the specified search criteria/s.
	 *  @param searchCriterias - a list of the search criterias.
	 *  @return ArrayList<T> of all entities that has all criterias.
     * 	@throws IllegalStateException - if the entity manager factory is closed.
	 *	@throws IllegalArgumentException - if query string is not valid. */
	ArrayList<T> search(ISearchCriteria<T>[] searchCriterias) throws IllegalStateException, IllegalArgumentException;
}