package com.javaleo.systems.botmaker.ejb.business;

import java.util.Calendar;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;

import com.javaleo.systems.botmaker.ejb.entities.Token;
import com.javaleo.systems.botmaker.ejb.entities.User;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;

@Named
@Stateless
public class TokenBusiness implements ITokenBusiness {

	private static final long serialVersionUID = 1L;

	@Inject
	private IPersistenceBasic<Token> persistence;

	@Override
	public Token generateTokenToUser(User user) throws BusinessException {
		if (user == null) {
			throw new BusinessException("In order to create a token the user can't be null.");
		}
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, 2);
		Token token = new Token();
		token.setUser(user);
		token.setValidUntil(cal.getTimeInMillis());
		token.setUuid(StringUtils.substring(UUID.randomUUID().toString(), 1, 30));
		token.setUsed(false);
		persistence.saveOrUpdate(token);
		return token;
	}

	@Override
	public Token validateTokenByUUID(String uuid) throws BusinessException {
		Calendar cal = Calendar.getInstance();
		CriteriaBuilder builder = persistence.getCriteriaBuilder();
		CriteriaQuery<Token> query = builder.createQuery(Token.class);
		Root<Token> from = query.from(Token.class);
		from.join("user", JoinType.INNER);
		query.where(builder.equal(from.get("uuid"), uuid));
		// TODO review validUntil too.
		query.select(from);
		return persistence.getSingleResult(query);
	}
}
