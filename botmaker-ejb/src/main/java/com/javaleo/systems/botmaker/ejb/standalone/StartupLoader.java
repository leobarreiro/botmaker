package com.javaleo.systems.botmaker.ejb.standalone;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.javaleo.systems.botmaker.ejb.business.ICompanyBusiness;
import com.javaleo.systems.botmaker.ejb.business.IUserBusiness;
import com.javaleo.systems.botmaker.ejb.entities.Company;
import com.javaleo.systems.botmaker.ejb.entities.User;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;

@Startup
@Singleton
public class StartupLoader implements IStartupLoader {

	private static final long serialVersionUID = -903960922086627052L;

	@Inject
	private ICompanyBusiness companyBusiness;

	@Inject
	private IUserBusiness userBusiness;

	@Inject
	private Logger LOG;

	@Override
	@PostConstruct
	public void startVerification() {
		try {
			List<Company> companies = companyBusiness.listAllCompanies();
			if (companies == null || companies.isEmpty()) {
				Company company = new Company();
				company.setName("Javaleo.org");
				company.setWebsite("http://javaleo.org");
				company.setActive(true);
				companyBusiness.saveCompany(company);

				List<User> users = userBusiness.listAllUsers();
				if (users == null || users.isEmpty()) {
					User user = new User();
					user.setCompany(company);
					user.setName("Admin User BotRise");
					user.setEmail("javaleo@javaleo.org");
					user.setUsername("admin");
					user.setAdmin(true);
					user.setActive(true);
					String passwd = "admin@123";
					userBusiness.saveUser(user, passwd);
				}

			}
		} catch (BusinessException e) {
			LOG.warn(e.getMessage());
		}
	}
}
