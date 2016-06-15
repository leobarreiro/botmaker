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

	@Override
	public List<Company> listAllCompanies() {
		return companyBusiness.listAllCompanies();
	}

	@Override
	public void saveCompany(Company company) throws BusinessException {
		companyBusiness.saveCompany(company);
	}

	public void activateCompany(Company company) throws BusinessException {
		companyBusiness.activateCompany(company);
	}

	@Override
	public void deactivateCompany(Company company) throws BusinessException {
		companyBusiness.deactivateCompany(company);
	}

	@Override
	public Bot validateBotTelegram(String token) throws BusinessException {
		return botBusiness.validateBotTelegram(token);
	}

	@Override
	public void saveBot(Bot bot) throws BusinessException {
		botBusiness.saveBot(bot);
	}

	@Override
	public void deactivateBot(Bot bot) throws BusinessException {
		botBusiness.deactivateBot(bot);
	}

	@Override
	public void reactivateBot(Bot bot) throws BusinessException {
		botBusiness.reactivateBot(bot);
	}

	@Override
	public List<Bot> searchBot(BotFilter filter) {
		return botBusiness.searchBot(filter);
	}

	@Override
	public void validateUser(User user, String password, String passwordReview) throws BusinessException {
		userBusiness.validateUser(user, password, passwordReview);
	}

	@Override
	public void saveUser(User user, String password, String passwordReview) throws BusinessException {
		userBusiness.saveUser(user, password, passwordReview);
	}

	@Override
	public User findUserByUsernameAndPassword(String username, String passphrase) {
		return userBusiness.findUserByUsernameAndPassphrase(username, passphrase);
	}

	@Override
	public List<Bot> listLastBotsFromCompanyUser() {
		return botBusiness.listLastBotsFromCompanyUser();
	}

	@Override
	public Command getCommandById(Long id) {
		return commandBusiness.getCommandById(id);
	}

	@Override
	public List<Command> listCommandsByBot(Bot bot) {
		return commandBusiness.listCommandsByBot(bot);
	}

	@Override
	public void saveCommand(Command command) throws BusinessException {
		commandBusiness.saveCommand(command);
	}

	@Override
	public void dropCommand(Command command) {
		commandBusiness.dropCommand(command);
	}

	@Override
	public void saveQuestion(Question question) throws BusinessException {
		questionBusiness.saveQuestion(question);
	}

	@Override
	public void dropQuestion(Question question) throws BusinessException {
		questionBusiness.dropQuestion(question);
	}

	@Override
	public List<Question> listQuestionsFromCommand(Command command) {
		return questionBusiness.listQuestionsFromCommand(command);
	}

	@Override
	public Question getLastQuestionFromCommand(Command command) {
		return questionBusiness.getLastQuestionFromCommand(command);
	}

	@Override
	public void upQuestionOrder(Question question) throws BusinessException {
		questionBusiness.upQuestionOrder(question);
	}

	@Override
	public List<User> listAllUsers() {
		return userBusiness.listAllUsers();
	}

	@Override
	public void saveValidator(Validator validator) throws BusinessException {
		validatorBusiness.saveValidator(validator);
	}

	@Override
	public List<Validator> searchValidatorByFilter(ValidatorFilter filter) {
		return validatorBusiness.searchValidatorByFilter(filter);
	}

	@Override
	public List<UserPreference> listPreferencesByUser(User user) {
		return userPreferenceBusiness.listPreferencesByUser(user);
	}

	@Override
	public UserPreference getPreferenceByUserAndName(User user, String name) {
		return userPreferenceBusiness.getPreferenceByUserAndName(user, name);
	}

	@Override
	public void savePreference(User user, UserPreference userPreference) {
		userPreferenceBusiness.savePreference(user, userPreference);
	}

	@Override
	public void removePreference(UserPreference userPreference) {
		userPreferenceBusiness.removePreference(userPreference);
	}

	@Override
	public List<DialogContextVar> getListDialogContextVars() {
		return dialogcontextVarBusiness.getListDialogContextVars();
	}

	@Override
	public List<DialogContextVar> getContextVarsFromDialog(Dialog dialog) {
		return dialogcontextVarBusiness.getContextVarsFromDialog(dialog);
	}

	@Override
	public boolean isValidScript(Script script) throws BusinessException {
		return scriptBusiness.isValidScript(script);
	}

	@Override
	public String debugScript(Dialog dialog, Script script) {
		return scriptBusiness.debugScript(dialog, script);
	}

	@Override
	public String executeScript(Dialog dialog, Script script) throws BusinessException {
		return scriptBusiness.executeScript(dialog, script);
	}

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

	@Override
	public void dropBlackListExpression(BlackListExpression expression) throws BusinessException {
		blackListExpressionBusiness.dropBlackListExpression(expression);
	}

}
