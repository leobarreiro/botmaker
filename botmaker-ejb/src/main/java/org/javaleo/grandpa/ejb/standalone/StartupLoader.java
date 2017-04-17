package org.javaleo.grandpa.ejb.standalone;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.io.FileUtils;
import org.javaleo.grandpa.ejb.business.ICompanyBusiness;
import org.javaleo.grandpa.ejb.business.IUserBusiness;
import org.javaleo.grandpa.ejb.entities.Company;
import org.javaleo.grandpa.ejb.entities.User;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.slf4j.Logger;

@Startup
@Singleton
public class StartupLoader implements IStartupLoader {

	private static final long serialVersionUID = -903960922086627052L;

	@Inject
	private EntityManager entityManager;

	@Inject
	private ICompanyBusiness companyBusiness;

	@Inject
	private IUserBusiness userBusiness;

	@Inject
	private Logger LOG;

	@Override
	@PostConstruct
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void startVerification() {
		try {
			List<Company> companies = companyBusiness.listAllCompanies();
			if (companies == null || companies.isEmpty()) {
				Company company = new Company();
				company.setName("Javaleo.org");
				company.setWebsite("http://javaleo.org");
				company.setActive(true);
				companyBusiness.saveCompany(company);
				mountCompanyRootDirectory(company.getId());
			} else {
				for (Company c : companies) {
					mountCompanyRootDirectory(c.getId());
				}
			}
			Company firstCompany = companyBusiness.getCompanyById(1L);
			List<User> users = userBusiness.listAllUsers();
			if (users == null || users.isEmpty()) {
				User user = new User();
				user.setCompany(firstCompany);
				user.setName("Admin User BotRise");
				user.setEmail("javaleo@javaleo.org");
				user.setUsername("botmaster");
				user.setAdmin(true);
				user.setActive(true);
				String passwd = "admin@123";
				userBusiness.saveUser(user, passwd, passwd);
			}

			String jpqlAnswerType = "UPDATE botmaker.question SET answer_type = \'STRING\' WHERE answer_type IS NULL";
			Query qrAnswerType = entityManager.createNativeQuery(jpqlAnswerType);
			qrAnswerType.executeUpdate();

			// String jpqlParseMode = "UPDATE botmaker.question SET parse_mode =
			// \'HTML\' WHERE parse_mode IS NULL";
			// Query qrParseMode =
			// entityManager.createNativeQuery(jpqlParseMode);
			// qrParseMode.executeUpdate();

			String jpqlValidatorType = "UPDATE botmaker.validator SET validator_type = \'BOOLEAN\' WHERE (validator_type = \'GROOVY\' OR validator_type = \'REGEXP\')";
			Query qrValidatorType = entityManager.createNativeQuery(jpqlValidatorType);
			qrValidatorType.executeUpdate();
		} catch (BusinessException e) {
			LOG.warn(e.getMessage());
		}
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	private void mountCompanyRootDirectory(Long idCompany) {
		StringBuilder str = new StringBuilder(System.getProperty("botmaker.companies.dir"));
		str.append(File.separator);
		str.append(idCompany);
		File file = new File(str.toString());
		if (!file.exists()) {
			try {
				FileUtils.forceMkdir(file);
			} catch (IOException e) {
				LOG.error(e.getMessage());
			}
		}
	}

}
