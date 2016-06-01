package com.javaleo.systems.botmaker.ejb.business;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;

import com.javaleo.systems.botmaker.ejb.entities.BlackListExpression;
import com.javaleo.systems.botmaker.ejb.enums.ScriptType;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;

@Stateless
public class BlackListExpressionBusiness implements IBlackListExpressionBusiness {

	private static final long serialVersionUID = 1L;

	@Inject
	private IPersistenceBasic<BlackListExpression> persistence;

	@Override
	public void saveBlackListExpression(BlackListExpression expression) throws BusinessException {
		testBlackListExpressionIntegrity(expression);
		persistence.saveOrUpdate(expression);
	}

	@Override
	public List<BlackListExpression> listBlackListExpressionByScriptType(ScriptType scriptType) {

		// GROOVY
		// System.exit
		// System.exec
		// Runtime.getRuntime().exec
		// System.getenv()
		// System.getenv
		// println

		// PYTHON
		// import os
		// import sys
		// open(
		// file(
		// import threading
		// __import__('threading')
		// os.environ.get
		// eval(
		// exec(

		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<BlackListExpression> query = cb.createQuery(BlackListExpression.class);
		Root<BlackListExpression> from = query.from(BlackListExpression.class);
		// List<Predicate> predicates = new ArrayList<Predicate>();
		// Expression<String> path = from.get("name");
		// predicates.add(cb.like(path, "%" + filter.getName() + "%"));
		// query.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		query.where(cb.equal(from.get("scriptType"), scriptType));
		return persistence.getResultList(query);
	}

	@Override
	public void testScriptAgainstBlackListExpression(String scriptCode, ScriptType scriptType) throws BusinessException {
		if (StringUtils.isBlank(scriptCode)) {
			throw new BusinessException("Script Code is empty or null.");
		}
		if (scriptType == null) {
			throw new BusinessException("Script type is not defined.");
		}
		String scriptCodeOneLine = scriptCode.replaceAll("\n", "").replaceAll("\r", "");
		List<BlackListExpression> blackList = listBlackListExpressionByScriptType(scriptType);
		List<String> problemsList = new ArrayList<String>();
		for (BlackListExpression expr : blackList) {
			if (expr.getRegexp().equals(true)) {
				Pattern pattern = Pattern.compile(expr.getContent());
				Matcher m = pattern.matcher(StringUtils.lowerCase(scriptCodeOneLine));
				if (m.matches()) {
					problemsList.add(expr.getContent());
				}
			} else {
				if (StringUtils.containsIgnoreCase(scriptCodeOneLine, expr.getContent())) {
					problemsList.add(expr.getContent());
				}
			}
		}
		if (!problemsList.isEmpty()) {
			StringBuilder str = new StringBuilder("Ooops. There are problems in your script code. For security reasons, the expressions above are not allowed.\n\n");
			for (String problem : problemsList) {
				str.append(problem);
				str.append("\n");
			}
			str.append("\nPlease, remove this expressions before save this script.");
			throw new BusinessException(str.toString());
		}
	}

	private void testBlackListExpressionIntegrity(BlackListExpression expression) throws BusinessException {
		if (expression == null) {
			throw new BusinessException("The Expression is null. It's not possible to test.");
		}
		if (StringUtils.isEmpty(expression.getContent())) {
			throw new BusinessException("The Expression content is null or blank.");
		}
		if (expression.getScriptType() == null) {
			throw new BusinessException("The script type of expression doesn't defined.");
		}
	}

}
