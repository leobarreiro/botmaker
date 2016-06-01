package com.javaleo.systems.botmaker.ejb.facades;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

import com.javaleo.systems.botmaker.ejb.business.IBlackListExpressionBusiness;
import com.javaleo.systems.botmaker.ejb.business.IBotBusiness;
import com.javaleo.systems.botmaker.ejb.business.ICommandBusiness;
import com.javaleo.systems.botmaker.ejb.business.ICompanyBusiness;
import com.javaleo.systems.botmaker.ejb.business.IDialogContextVarBusiness;
import com.javaleo.systems.botmaker.ejb.business.IQuestionBusiness;
import com.javaleo.systems.botmaker.ejb.business.IScriptBusiness;
import com.javaleo.systems.botmaker.ejb.business.IUserBusiness;
import com.javaleo.systems.botmaker.ejb.business.IUserPreferenceBusiness;
import com.javaleo.systems.botmaker.ejb.business.IValidatorBusiness;
import com.javaleo.systems.botmaker.ejb.entities.BlackListExpression;
import com.javaleo.systems.botmaker.ejb.entities.Bot;
import com.javaleo.systems.botmaker.ejb.entities.Command;
import com.javaleo.systems.botmaker.ejb.entities.Company;
import com.javaleo.systems.botmaker.ejb.entities.Question;
import com.javaleo.systems.botmaker.ejb.entities.Script;
import com.javaleo.systems.botmaker.ejb.entities.User;
import com.javaleo.systems.botmaker.ejb.entities.UserPreference;
import com.javaleo.systems.botmaker.ejb.entities.Validator;
import com.javaleo.systems.botmaker.ejb.enums.ScriptType;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.filters.BotFilter;
import com.javaleo.systems.botmaker.ejb.filters.ValidatorFilter;
import com.javaleo.systems.botmaker.ejb.pojos.Dialog;
import com.javaleo.systems.botmaker.ejb.pojos.DialogContextVar;

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
	private IValidatorBusiness validatorBusiness;

	@Inject
	private IUserBusiness userBusiness;

	@Inject
	private IUserPreferenceBusiness userPreferenceBusiness;

	@Inject
	private IDialogContextVarBusiness dialogcontextVarBusiness;

	@Inject
	private IScriptBusiness scriptBusiness;

	@Inject
	private IBlackListExpressionBusiness blackListExpressionBusiness;

	/**
	 * @return
	 * @see com.javaleo.systems.botmaker.ejb.business.ICompanyBusiness#listAllCompanies()
	 */
	@Override
	public List<Company> listAllCompanies() {
		return companyBusiness.listAllCompanies();
	}

	/**
	 * @param company
	 * @throws BusinessException
	 * @see com.javaleo.systems.botmaker.ejb.business.ICompanyBusiness#saveCompany(com.javaleo.systems.botmaker.ejb.entities.Company)
	 */
	@Override
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
	@Override
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
	 * @see
	 * com.javaleo.systems.botmaker.ejb.facades.IBotMakerFacade#deactivateBot(com.javaleo.systems.botmaker.ejb.entities
	 * .Bot)
	 */
	@Override
	public void deactivateBot(Bot bot) throws BusinessException {
		botBusiness.deactivateBot(bot);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.javaleo.systems.botmaker.ejb.facades.IBotMakerFacade#reactivateBot(com.javaleo.systems.botmaker.ejb.entities
	 * .Bot)
	 */
	@Override
	public void reactivateBot(Bot bot) throws BusinessException {
		botBusiness.reactivateBot(bot);
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
	 * @see com.javaleo.systems.botmaker.ejb.facades.IBotMakerFacade#listLastBotsFromCompanyUser()
	 */
	@Override
	public List<Bot> listLastBotsFromCompanyUser() {
		return botBusiness.listLastBotsFromCompanyUser();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.javaleo.systems.botmaker.ejb.facades.IBotMakerFacade#getCommandById(java.lang.Long)
	 */
	@Override
	public Command getCommandById(Long id) {
		return commandBusiness.getCommandById(id);
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
	 * @see
	 * com.javaleo.systems.botmaker.ejb.business.ICommandBusiness#dropCommand(com.javaleo.systems.botmaker.ejb.entities
	 * .Command)
	 */
	@Override
	public void dropCommand(Command command) {
		commandBusiness.dropCommand(command);
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
	 * @see
	 * com.javaleo.systems.botmaker.ejb.facades.IBotMakerFacade#dropQuestion(com.javaleo.systems.botmaker.ejb.entities
	 * .Question)
	 */
	@Override
	public void dropQuestion(Question question) throws BusinessException {
		questionBusiness.dropQuestion(question);
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
	public void saveValidator(Validator validator) throws BusinessException {
		validatorBusiness.saveValidator(validator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.javaleo.systems.botmaker.ejb.facades.IBotMakerFacade#searchSnippetCodeByFilter(com.javaleo.systems.botmaker
	 * .ejb.filters.SnippetCodeFilter)
	 */
	@Override
	public List<Validator> searchValidatorByFilter(ValidatorFilter filter) {
		return validatorBusiness.searchValidatorByFilter(filter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.javaleo.systems.botmaker.ejb.facades.IBotMakerFacade#listPreferencesByUser(com.javaleo.systems.botmaker.ejb
	 * .entities.User)
	 */
	@Override
	public List<UserPreference> listPreferencesByUser(User user) {
		return userPreferenceBusiness.listPreferencesByUser(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.javaleo.systems.botmaker.ejb.facades.IBotMakerFacade#getPreferenceByUserAndName(com.javaleo.systems.botmaker
	 * .ejb.entities.User, java.lang.String)
	 */
	@Override
	public UserPreference getPreferenceByUserAndName(User user, String name) {
		return userPreferenceBusiness.getPreferenceByUserAndName(user, name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.javaleo.systems.botmaker.ejb.facades.IBotMakerFacade#savePreference(com.javaleo.systems.botmaker.ejb.entities
	 * .User, com.javaleo.systems.botmaker.ejb.entities.UserPreference)
	 */
	@Override
	public void savePreference(User user, UserPreference userPreference) {
		userPreferenceBusiness.savePreference(user, userPreference);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.javaleo.systems.botmaker.ejb.facades.IBotMakerFacade#removePreference(com.javaleo.systems.botmaker.ejb.entities
	 * .UserPreference)
	 */
	@Override
	public void removePreference(UserPreference userPreference) {
		userPreferenceBusiness.removePreference(userPreference);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.javaleo.systems.botmaker.ejb.facades.IBotMakerFacade#getListDialogContextVars()
	 */
	@Override
	public List<DialogContextVar> getListDialogContextVars() {
		return dialogcontextVarBusiness.getListDialogContextVars();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.javaleo.systems.botmaker.ejb.facades.IBotMakerFacade#getContextVarsFromDialog(com.javaleo.systems.botmaker
	 * .ejb.pojos.Dialog)
	 */
	@Override
	public List<DialogContextVar> getContextVarsFromDialog(Dialog dialog) {
		return dialogcontextVarBusiness.getContextVarsFromDialog(dialog);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.javaleo.systems.botmaker.ejb.facades.IBotMakerFacade#executeScript(com.javaleo.systems.botmaker.ejb.pojos
	 * .Dialog, com.javaleo.systems.botmaker.ejb.entities.Script)
	 */
	@Override
	public String executeScript(Dialog dialog, Script script) throws BusinessException {
		return scriptBusiness.executeScript(dialog, script);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.javaleo.systems.botmaker.ejb.facades.IBotMakerFacade#evaluateBooleanScript(com.javaleo.systems.botmaker.ejb
	 * .pojos.Dialog, com.javaleo.systems.botmaker.ejb.entities.Script)
	 */
	@Override
	public Boolean evaluateBooleanScript(Dialog dialog, Script script) throws BusinessException {
		return scriptBusiness.evaluateBooleanScript(dialog, script);
	}

	@Override
	public void saveBlackListExpression(BlackListExpression expression) throws BusinessException {
		blackListExpressionBusiness.saveBlackListExpression(expression);
	}

	@Override
	public List<BlackListExpression> listBlackListExpressionByScriptType(ScriptType scriptType) {
		return blackListExpressionBusiness.listBlackListExpressionByScriptType(scriptType);
	}

	@Override
	public void testScriptAgainstBlackListExpression(String scriptCode, ScriptType scriptType) throws BusinessException {
		blackListExpressionBusiness.testScriptAgainstBlackListExpression(scriptCode, scriptType);
	}

}
