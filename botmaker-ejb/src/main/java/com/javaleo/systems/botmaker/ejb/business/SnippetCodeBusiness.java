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

import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;

import com.javaleo.systems.botmaker.ejb.entities.Company;
import com.javaleo.systems.botmaker.ejb.entities.SnippetCode;
import com.javaleo.systems.botmaker.ejb.filters.SnippetCodeFilter;
import com.javaleo.systems.botmaker.ejb.security.BotMakerCredentials;

@Named
@Stateless
public class SnippetCodeBusiness implements ISnippetCodeBusiness {

	private static final long serialVersionUID = 1L;

	@Inject
	private IPersistenceBasic<SnippetCode> persistence;

	@Inject
	private BotMakerCredentials credentials;

	@Override
	public void saveSnippetCode(SnippetCode snippetCode) {
		persistence.saveOrUpdate(snippetCode);
	}

	@Override
	public List<SnippetCode> searchSnippetCodeByFilter(SnippetCodeFilter filter) {
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<SnippetCode> cq = cb.createQuery(SnippetCode.class);
		Root<SnippetCode> fromSnippet = cq.from(SnippetCode.class);
		Join<SnippetCode, Company> joinCompany = fromSnippet.join("company", JoinType.LEFT);
		if (credentials.getCompany() != null) {
			cq.where(cb.or(cb.equal(joinCompany.get("id"), credentials.getCompany().getId()), cb.isNull(joinCompany.get("id"))));
		} else {
			cq.where(cb.isNull(joinCompany.get("id")));
		}
		return persistence.getResultList(cq);
	}

}
