package org.javaleo.grandpa.ejb.business;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.javaleo.grandpa.ejb.entities.User;
import org.javaleo.grandpa.ejb.entities.UserPreference;
import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;

@Stateless
public class UserPreferenceBusiness implements IUserPreferenceBusiness {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private IPersistenceBasic<UserPreference> persistence;
	
	@Inject
	private IPersistenceBasic<User> persistenceUser;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<UserPreference> listPreferencesByUser(User user) {
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<UserPreference> cq = cb.createQuery(UserPreference.class);
		Root<UserPreference> from = cq.from(UserPreference.class);
		Join<UserPreference, User> joinUser = from.join("user", JoinType.INNER);
		cq.where(cb.equal(joinUser.get("id"), user.getId()));
		return persistence.getResultList(cq);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public UserPreference getPreferenceByUserAndName(User user, String name) {
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<UserPreference> cq = cb.createQuery(UserPreference.class);
		Root<UserPreference> from = cq.from(UserPreference.class);
		Join<UserPreference, User> joinUser = from.join("user", JoinType.INNER);
		cq.where(cb.and(cb.equal(from.get("property"), name), cb.equal(joinUser.get("id"), user.getId())));
		return persistence.getSingleResult(cq);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void savePreference(User user, UserPreference userPreference) {
		User userSave = persistenceUser.find(User.class, user.getId());
		userPreference.setUser(userSave);
		persistence.saveOrUpdate(userPreference);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removePreference(UserPreference userPreference) {
		UserPreference userPrefDelete = persistence.find(UserPreference.class, userPreference.getId());
		persistence.remove(userPrefDelete);
	}

}
