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
import org.python.icu.util.Calendar;

import com.javaleo.systems.botmaker.ejb.entities.Company;
import com.javaleo.systems.botmaker.ejb.entities.User;
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
	private ICompanyBusiness companyBusiness;

	@Inject
	private IUserBusiness userBusiness;

	@Inject
	private BotMakerCredentials credentials;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveValidator(Validator validator) throws BusinessException {
		if (validator.getCompany() == null) {
			Company company = companyBusiness.getCompanyById(credentials.getCompany().getId());
			validator.setCompany(company);
		}
		if (validator.getAuthor() == null) {
			User user = userBusiness.findUserByUsername(credentials.getUser().getUsername());
			validator.setAuthor(user);
		}
		if (validator.getCreated() == null) {
			validator.setCreated(Calendar.getInstance().getTime());
		}
		validator.setModified(Calendar.getInstance().getTime());
		persistence.saveOrUpdate(validator);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Validator> searchValidatorByFilter(ValidatorFilter filter) {
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<Validator> cq = cb.createQuery(Validator.class);
		Root<Validator> fromSnippet = cq.from(Validator.class);
		Join<Validator, Company> joinCompany = fromSnippet.join("company", JoinType.LEFT);
		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(cb.or(cb.equal(joinCompany.get("id"), credentials.getCompany().getId()), cb.equal(fromSnippet.get("publicUse"), Boolean.TRUE)));
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
