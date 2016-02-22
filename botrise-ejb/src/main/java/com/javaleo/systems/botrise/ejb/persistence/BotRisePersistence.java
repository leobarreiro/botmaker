package com.javaleo.systems.botrise.ejb.persistence;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.javaleo.libs.jee.core.model.IEntityBasic;
import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;
import org.javaleo.libs.jee.core.persistence.PersistenceBasic;

@Named
@Stateless
public class BotRisePersistence<T extends IEntityBasic> extends PersistenceBasic<T> implements IPersistenceBasic<T> {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

}
