package com.javaleo.systems.botmaker.ejb.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;

import com.javaleo.systems.botmaker.ejb.entities.Company;
import com.javaleo.systems.botmaker.ejb.entities.Validator;
import com.javaleo.systems.botmaker.ejb.enums.ValidatorType;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.filters.ValidatorFilter;
import com.javaleo.systems.botmaker.ejb.security.BotMakerCredentials;
import com.javaleo.systems.botmaker.ejb.utils.BotMakerUtils;

@Stateless
public class ValidatorBusiness implements IValidatorBusiness {

	private static final long serialVersionUID = 1L;

	@Inject
	private IPersistenceBasic<Validator> persistence;

	@Inject
	private BotMakerCredentials credentials;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveValidator(Validator snippet) throws BusinessException {
		// TODO Validar o snippet aqui antes de salvar
		persistence.saveOrUpdate(snippet);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Validator> searchValidatorByFilter(ValidatorFilter filter) {
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<Validator> cq = cb.createQuery(Validator.class);
		Root<Validator> fromSnippet = cq.from(Validator.class);
		Join<Validator, Company> joinCompany = fromSnippet.join("company", JoinType.LEFT);
		List<Predicate> predicates = new ArrayList<Predicate>();
		if (credentials.getCompany() != null) {
			predicates.add(cb.or(cb.equal(joinCompany.get("id"), credentials.getCompany().getId()), cb.isNull(joinCompany.get("id"))));
		} else {
			predicates.add(cb.isNull(joinCompany.get("id")));
		}
		if (filter.getValidatorType() != null) {
			predicates.add(cb.equal(fromSnippet.get("scriptType"), filter.getValidatorType()));
		}
		Predicate[] predicateArray = new Predicate[predicates.size()];
		predicates.toArray(predicateArray);
		cq.where(predicateArray);
		cq.orderBy(cb.asc(fromSnippet.get("name")));
		return persistence.getResultList(cq);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public boolean validateContent(Validator validator, String content) throws BusinessException {
		if (ValidatorType.SET.equals(validator.getValidatorType())) {
			String validatorSource = validator.getCode();
			List<String> optValidator = new ArrayList<String>(Arrays.asList(validatorSource.split(",")));
			for (String op : optValidator) {
				if (StringUtils.equalsIgnoreCase(StringUtils.trim(op), content)) {
					return true;
				}
			}
			return false;
		} else if (ValidatorType.BOOLEAN.equals(validator.getValidatorType())) {
			Pattern pattern = Pattern.compile(validator.getCode());
			Matcher m = pattern.matcher(StringUtils.lowerCase(content));
			return m.matches();
		}
		return false;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<List<String>> getOptionsByValidator(Validator validator) {
		return BotMakerUtils.convertStringToArrayOfArrays(validator.getCode(), 2, ',');
	}

}
