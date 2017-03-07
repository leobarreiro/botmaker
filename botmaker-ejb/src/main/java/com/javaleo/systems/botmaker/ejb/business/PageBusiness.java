package com.javaleo.systems.botmaker.ejb.business;

import java.util.Calendar;
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

import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;

import com.javaleo.systems.botmaker.ejb.entities.Company;
import com.javaleo.systems.botmaker.ejb.entities.Page;
import com.javaleo.systems.botmaker.ejb.entities.User;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.security.BotMakerCredentials;

@Stateless
public class PageBusiness implements IPageBusiness {

	private static final long serialVersionUID = 1L;

	@Inject
	private IPersistenceBasic<Page> persistence;

	@Inject
	private BotMakerCredentials credentials;

	@Inject
	private ICompanyBusiness companyBusiness;

	@Inject
	private IUserBusiness userBusiness;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void savePage(Page page) throws BusinessException {
		Company company = companyBusiness.getCompanyById(credentials.getCompany().getId());
		page.setCompany(company);
		User user = userBusiness.findUserByUsername(credentials.getUser().getUsername());
		page.setUser(user);
		if (page.getCreated() == null) {
			page.setCreated(Calendar.getInstance().getTime());
		}
		persistence.saveOrUpdate(page);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Page> listPages() {
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<Page> cq = cb.createQuery(Page.class);
		Root<Page> fromPage = cq.from(Page.class);
		Join<Page, Company> joinCompany = fromPage.join("company", JoinType.INNER);
		cq.where(cb.equal(joinCompany.get("id"), credentials.getCompany().getId()));
		cq.orderBy(cb.desc(fromPage.get("created")));
		return persistence.getResultList(cq);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void disablePage(Page page) throws BusinessException {
		Page managedPage = persistence.find(Page.class, page.getId());
		managedPage.setActive(false);
		persistence.saveOrUpdate(managedPage);
	}

}
