package com.asafandben.dal.cache;

/**
 * This interface means we can cache any type which implements it.
 * 
 * @param <PK> - The type of the primary key of the Class we're going to cache
 */
public interface ICacheable<PK> {

	/**
	 * @return the primary key of type PK of a certain object.
	 */
	public PK getID();
}
