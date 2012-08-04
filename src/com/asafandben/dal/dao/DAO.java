package com.asafandben.dal.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import com.asafandben.bl.core_entities.User;

public class DAO {
	@PersistenceContext
	protected EntityManager em;
	
	public DAO(EntityManager em) {
		super();
		this.em = em;
	}
}
