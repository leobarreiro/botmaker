package com.javaleo.systems.botmaker.ejb.business;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;

import com.javaleo.systems.botmaker.ejb.entities.User;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;

@Named
@Stateless
public class UserBusiness implements IUserBusiness {

	private static final long serialVersionUID = 1L;

	@Inject
	private IPersistenceBasic<User> persistence;

	@Override
	public void validateUser(User user, String password, String passwordReview) throws BusinessException {
		// name
		Pattern namePattern = Pattern.compile("^[A-Za-z]{2,}[\\ ]{1}[A-Za-z]{2,}");
		Matcher nameMatcher = namePattern.matcher(user.getName());
		if (!nameMatcher.matches()) {
			throw new BusinessException("Your name not contains first and last name. Please review and try again.");
		}
		// username
		Pattern usernamePattern = Pattern.compile("^[A-Za-z0-9]{6,12}$");
		Matcher usernameMatcher = usernamePattern.matcher(user.getUsername());
		if (!usernameMatcher.matches()) {
			throw new BusinessException("The username must be between 6 and 12 digits long. He should contains letters and numbers only.");
		}
		// email
		Pattern mailPattern = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9_\\-.]*[a-zA-Z0-9]@[a-zA-Z0-9][a-zA-Z0-9.]*\\.[a-zA-Z0-9.]+$");
		Matcher mailMatcher = mailPattern.matcher(user.getEmail());
		if (!mailMatcher.matches()) {
			throw new BusinessException("The email entered is not valid. Please review your email.");
		}
		// password
		Pattern passwdPattern = Pattern.compile("^(?=.*\\d).{4,10}$");
		Matcher m = passwdPattern.matcher(password);
		if (!m.matches()) {
			throw new BusinessException("Your password must be between 4 and 10 digits long and include at least one numeric digit.");
		}
		if (!StringUtils.equals(password, passwordReview)) {
			throw new BusinessException("Please type your password confirmation equals to password field. Try again.");
		}
	}

	@Override
	public void saveUser(User user, String password, String passwordReview) throws BusinessException {
		validateUser(user, password, passwordReview);
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
