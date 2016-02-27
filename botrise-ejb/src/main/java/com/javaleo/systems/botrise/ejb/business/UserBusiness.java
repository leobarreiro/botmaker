package com.javaleo.systems.botrise.ejb.business;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.codec.digest.DigestUtils;
import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;

import com.javaleo.systems.botrise.ejb.entities.User;
import com.javaleo.systems.botrise.ejb.exceptions.BotRiseException;

@Named
@Stateless
public class UserBusiness implements IUserBusiness {

	@Inject
	private IPersistenceBasic<User> persistence;

	@Override
	public void saveUser(User user, String password) throws BotRiseException {
		user.setPassword(DigestUtils.sha1Hex(password));
		persistence.saveOrUpdate(user);
	}

	@Override
	public User findUserByUsernameAndPassphrase(String username, String passphrase) {
		CriteriaBuilder builder = persistence.getCriteriaBuilder();
		CriteriaQuery<User> query = builder.createQuery(User.class);
		Root<User> from = query.from(User.class);
		query.where(
				builder.and(builder.equal(from.get("username"), username)),
				builder.and(builder.equal(from.get("password"), DigestUtils.sha1Hex(passphrase)))
				);
		query.select(from);
		return persistence.getSingleResult(query);
	}
}
