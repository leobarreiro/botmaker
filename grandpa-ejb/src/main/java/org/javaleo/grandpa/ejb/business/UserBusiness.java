package org.javaleo.grandpa.ejb.business;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.javaleo.grandpa.ejb.entities.Token;
import org.javaleo.grandpa.ejb.entities.User;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.utils.GrandPaUtils;
import org.javaleo.grandpa.ejb.utils.IMessageUtils;
import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;
import org.slf4j.Logger;

@Named
@Stateless
public class UserBusiness implements IUserBusiness {

	private static final long serialVersionUID = 1L;

	@Inject
	private IPersistenceBasic<User> persistence;

	@Inject
	private IMessageUtils messageUtils;

	@Inject
	private ITokenBusiness tokenBusiness;

	@Inject
	private Logger LOG;

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void validateUser(User user, String password, String passwordReview) throws BusinessException {
		// name
		Pattern namePattern = Pattern.compile("([A-Za-z]{3,}[\\W]+[A-Za-z]{2,})");
		Matcher nameMatcher = namePattern.matcher(user.getName());
		if (!nameMatcher.find()) {
			throw new BusinessException("Your name not contains first and last name. Please review and try again.");
		}
		// username
		Pattern usernamePattern = Pattern.compile("^[A-Za-z0-9]{4,12}$");
		Matcher usernameMatcher = usernamePattern.matcher(user.getUsername());
		if (!usernameMatcher.find()) {
			throw new BusinessException("The username must be between 4 and 12 digits long. He should contains letters and numbers only.");
		}
		// email
		if (!GrandPaUtils.validateEmail(user.getEmail())) {
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
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveUser(User user, String password, String passwordReview) throws BusinessException {
		validateUser(user, password, passwordReview);
		user.setPassword(DigestUtils.sha1Hex(password));
		persistence.saveOrUpdate(user);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void sendMessageRecoveryLoginToUser(String email, String emailReview) throws BusinessException {
		if (!GrandPaUtils.validateEmail(email)) {
			throw new BusinessException("The email can't be empty. You must enter a valid e-mail.");
		}
		if (!StringUtils.equals(email, emailReview)) {
			throw new BusinessException("Your email and email verification must be equals. Please review the information.");
		}
		User userOwnerEmail = findUserByEmail(email);
		if (userOwnerEmail == null) {
			throw new BusinessException("The email entered don't belong to any user. Please review the information.");
		}

		String conteudo = "";
		Map<String, String> keyValues = new HashMap<>();

		try {
			conteudo = IOUtils.toString(getClass().getResourceAsStream("/templates/mail-recover-password.html"));
		} catch (IOException e) {
			LOG.warn(e.getMessage());
			throw new BusinessException(e.getMessage());
		}

		String domainName = null;
		try {
			domainName = System.getProperty("botmaker.domain");
		} catch (Exception e) {
			LOG.warn(e.getMessage());
		}

		if (StringUtils.isBlank(domainName)) {
			LOG.warn("Property botmaker.domain is not defined in System properties. Please revise this configuration.");
			domainName = GrandPaUtils.DOMAIN_NAME;
		}

		Token token = tokenBusiness.generateTokenToUser(userOwnerEmail);
		keyValues.put("{domain-name}", domainName);
		keyValues.put("{token-uuid}", token.getUuid());

		String conteudoHtml = messageUtils.assemblyBodyMail(keyValues, conteudo);

		messageUtils.sendMailMessage("javaleo.org@gmail.com", userOwnerEmail.getEmail(), "Javaleo.org - Reset your password", conteudoHtml);
	}

	private User findUserByEmail(final String email) {
		CriteriaBuilder builder = persistence.getCriteriaBuilder();
		CriteriaQuery<User> query = builder.createQuery(User.class);
		Root<User> from = query.from(User.class);
		from.join("company", JoinType.INNER);
		query.where(builder.equal(from.get("email"), email));
		query.select(from);
		return persistence.getSingleResult(query);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public User findUserByUsernameAndPassphrase(String username, String passphrase) {
		CriteriaBuilder builder = persistence.getCriteriaBuilder();
		CriteriaQuery<User> query = builder.createQuery(User.class);
		Root<User> from = query.from(User.class);
		from.join("company", JoinType.INNER);
		query.where(builder.and(builder.equal(from.get("username"), username)), builder.and(builder.equal(from.get("password"), DigestUtils.sha1Hex(passphrase))));
		query.select(from);
		return persistence.getSingleResult(query);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public User findUserByUsername(String username) {
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> from = cq.from(User.class);
		from.join("company", JoinType.INNER);
		cq.where(cb.equal(from.get("username"), username));
		cq.select(from);
		return persistence.getSingleResult(cq);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<User> listAllUsers() {
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> from = cq.from(User.class);
		cq.select(from);
		return persistence.getResultList(cq);
	}

}
