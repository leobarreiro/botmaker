package com.javaleo.systems.botmaker.ejb.business;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.apache.commons.codec.digest.DigestUtils;
import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;

import com.javaleo.systems.botmaker.ejb.entities.Company;
import com.javaleo.systems.botmaker.ejb.entities.User;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;

@Named
@Stateless
public class UserBusiness implements IUserBusiness {

	private static final long serialVersionUID = 1L;

	@Inject
	private IPersistenceBasic<User> persistence;

	@Override
	public void saveUser(User user, String password) throws BusinessException {
		user.setPassword(DigestUtils.sha1Hex(password));
		persistence.saveOrUpdate(user);
	}

	@Override
	public User findUserByUsernameAndPassphrase(String username, String passphrase) {
		CriteriaBuilder builder = persistence.getCriteriaBuilder();
		CriteriaQuery<User> query = builder.createQuery(User.class);
		Root<User> from = query.from(User.class);
		from.join("company", JoinType.LEFT);
		query.where(builder.and(builder.equal(from.get("username"), username)), builder.and(builder.equal(from.get("password"), DigestUtils.sha1Hex(passphrase))));
		query.select(from);
		return persistence.getSingleResult(query);
	}

	@Override
	public List<User> listAllUsers() {
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> from = cq.from(User.class);
		cq.select(from);
		return persistence.getResultList(cq);
	}

}
