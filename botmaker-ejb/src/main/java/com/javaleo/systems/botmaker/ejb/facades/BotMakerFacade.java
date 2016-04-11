package com.javaleo.systems.botmaker.ejb.facades;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

import com.javaleo.systems.botmaker.ejb.business.IBotBusiness;
import com.javaleo.systems.botmaker.ejb.business.ICommandBusiness;
import com.javaleo.systems.botmaker.ejb.business.ICompanyBusiness;
import com.javaleo.systems.botmaker.ejb.business.IQuestionBusiness;
import com.javaleo.systems.botmaker.ejb.business.ISnippetCodeBusiness;
import com.javaleo.systems.botmaker.ejb.business.IUserBusiness;
import com.javaleo.systems.botmaker.ejb.entities.Bot;
import com.javaleo.systems.botmaker.ejb.entities.Command;
import com.javaleo.systems.botmaker.ejb.entities.Company;
import com.javaleo.systems.botmaker.ejb.entities.Question;
import com.javaleo.systems.botmaker.ejb.entities.SnippetCode;
import com.javaleo.systems.botmaker.ejb.entities.User;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.filters.BotFilter;
import com.javaleo.systems.botmaker.ejb.filters.SnippetCodeFilter;

@Named
@Stateless
public class BotMakerFacade implements IBotMakerFacade {

	private static final long serialVersionUID = 1L;

	@Inject
	private ICompanyBusiness companyBusiness;

	@Inject
	private IBotBusiness botBusiness;

	@Inject
	private ICommandBusiness commandBusiness;

	@Inject
	private IQuestionBusiness questionBusiness;

	@Inject
	private ISnippetCodeBusiness snippetCodeBusiness;

	@Inject
	private IUserBusiness userBusiness;

	/**
	 * @return
	 * @see com.javaleo.systems.botmaker.ejb.business.ICompanyBusiness#listAllCompanies()
	 */
	public List<Company> listAllCompanies() {
		return companyBusiness.listAllCompanies();
	}

	/**
	 * @param company
	 * @throws BusinessException
	 * @see com.javaleo.systems.botmaker.ejb.business.ICompanyBusiness#saveCompany(com.javaleo.systems.botmaker.ejb.entities.Company)
	 */
	public void saveCompany(Company company) throws BusinessException {
		companyBusiness.saveCompany(company);
	}

	/**
	 * @param company
	 * @throws BusinessException
	 * @see com.javaleo.systems.botmaker.ejb.business.ICompanyBusiness#activateCompany(com.javaleo.systems.botmaker.ejb.entities.Company)
	 */
	public void activateCompany(Company company) throws BusinessException {
		companyBusiness.activateCompany(company);
	}

	/**
	 * @param company
	 * @throws BusinessException
	 * @see com.javaleo.systems.botmaker.ejb.business.ICompanyBusiness#deactivateCompany(com.javaleo.systems.botmaker.ejb.entities.Company)
	 */
	public void deactivateCompany(Company company) throws BusinessException {
		companyBusiness.deactivateCompany(company);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.javaleo.systems.botrise.ejb.facades.IBotRiseFacade#validateBotTelegram(java.lang.String)
	 */
	@Override
	public Bot validateBotTelegram(String token) throws BusinessException {
		return botBusiness.validateBotTelegram(token);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.javaleo.systems.botrise.ejb.facades.IBotRiseFacade#saveBot(com.javaleo.systems.botrise
	 * .ejb.entities.Bot)
	 */
	@Override
	public void saveBot(Bot bot) throws BusinessException {
		botBusiness.saveBot(bot);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.javaleo.systems.botrise.ejb.facades.IBotRiseFacade#searchBot(com.javaleo.systems.botrise.ejb
	 * .filters.BotFilter )
	 */
	@Override
	public List<Bot> searchBot(BotFilter filter) {
		return botBusiness.searchBot(filter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.javaleo.systems.botrise.ejb.facades.IBotRiseFacade#saveUser(com.javaleo.systems.botrise.ejb
	 * .entities.User, java.lang.String)
	 */
	@Override
	public void saveUser(User user, String password) throws BusinessException {
		userBusiness.saveUser(user, password);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.javaleo.systems.botrise.ejb.facades.IBotRiseFacade#findUserByUsernameAndPassphrase(java .lang.String,
	 * java.lang.String)
	 */
	@Override
	public User findUserByUsernameAndPassword(String username, String passphrase) {
		return userBusiness.findUserByUsernameAndPassphrase(username, passphrase);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.javaleo.systems.botrise.ejb.facades.IBotRiseFacade#listCommandsByBot(com.javaleo.systems.
	 * botrise.ejb.entities .Bot)
	 */
	@Override
	public List<Command> listCommandsByBot(Bot bot) {
		return commandBusiness.listCommandsByBot(bot);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.javaleo.systems.botrise.ejb.facades.IBotRiseFacade#saveCommand(com.javaleo.systems.botrise
	 * .ejb.entities.Command )
	 */
	@Override
	public void saveCommand(Command command) throws BusinessException {
		commandBusiness.saveCommand(command);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.javaleo.systems.botrise.ejb.facades.IBotRiseFacade#saveQuestion(com.javaleo.systems.botrise
	 * .ejb.entities. Question)
	 */
	@Override
	public void saveQuestion(Question question) throws BusinessException {
		questionBusiness.saveQuestion(question);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.javaleo.systems.botrise.ejb.facades.IBotRiseFacade#listQuestionsFromCommand(com.javaleo
	 * .systems.botrise.ejb.entities.Command)
	 */
	@Override
	public List<Question> listQuestionsFromCommand(Command command) {
		return questionBusiness.listQuestionsFromCommand(command);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.javaleo.systems.botrise.ejb.facades.IBotRiseFacade#getLastQuestionFromCommand(com.javaleo
	 * .systems.botrise.ejb.entities.Command)
	 */
	@Override
	public Question getLastQuestionFromCommand(Command command) {
		return questionBusiness.getLastQuestionFromCommand(command);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.javaleo.systems.botrise.ejb.facades.IBotRiseFacade#upQuestionOrder(com.javaleo.systems
	 * .botrise.ejb.entities.Question)
	 */
	@Override
	public void upQuestionOrder(Question question) throws BusinessException {
		questionBusiness.upQuestionOrder(question);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.javaleo.systems.botrise.ejb.facades.IBotRiseFacade#listAllUsers()
	 */
	@Override
	public List<User> listAllUsers() {
		return userBusiness.listAllUsers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.javaleo.systems.botmaker.ejb.facades.IBotMakerFacade#saveSnippetCode(com.javaleo.systems.botmaker.ejb.entities
	 * .SnippetCode)
	 */
	@Override
	public void saveSnippetCode(SnippetCode snippetCode) {
		snippetCodeBusiness.saveSnippetCode(snippetCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.javaleo.systems.botmaker.ejb.facades.IBotMakerFacade#searchSnippetCodeByFilter(com.javaleo.systems.botmaker
	 * .ejb.filters.SnippetCodeFilter)
	 */
	@Override
	public List<SnippetCode> searchSnippetCodeByFilter(SnippetCodeFilter filter) {
		return snippetCodeBusiness.searchSnippetCodeByFilter(filter);
	}

}
