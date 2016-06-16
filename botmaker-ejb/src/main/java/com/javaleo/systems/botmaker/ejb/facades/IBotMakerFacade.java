package com.javaleo.systems.botmaker.ejb.facades;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

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

@Local
public interface IBotMakerFacade extends Serializable {

	Bot validateBotTelegram(String token) throws BusinessException;

	void saveBot(Bot bot) throws BusinessException;

	void deactivateBot(Bot bot) throws BusinessException;

	void reactivateBot(Bot bot) throws BusinessException;

	List<Bot> searchBot(BotFilter filter);

	void validateUser(User user, String password, String passwordReview) throws BusinessException;

	void saveUser(User user, String password, String passwordReview) throws BusinessException;

	User findUserByUsernameAndPassword(String username, String passphrase);

	Command getCommandById(Long id);

	List<Command> listCommandsByBot(Bot bot);

	void saveCommand(Command command) throws BusinessException;

	void saveQuestion(Question question) throws BusinessException;

	void dropQuestion(Question question) throws BusinessException;

	void dropCommand(Command command);

	List<Question> listQuestionsFromCommand(Command command);

	Question getLastQuestionFromCommand(Command command);

	void upQuestionOrder(Question question) throws BusinessException;

	List<User> listAllUsers();

	List<Bot> listLastBotsFromCompanyUser();

	void saveValidator(Validator validator) throws BusinessException;

	List<Validator> searchValidatorByFilter(ValidatorFilter filter);

	List<UserPreference> listPreferencesByUser(User user);

	UserPreference getPreferenceByUserAndName(User user, String name);

	void savePreference(User user, UserPreference userPreference);

	void removePreference(UserPreference userPreference);

	List<Company> listAllCompanies();

	void saveCompany(Company company) throws BusinessException;

	void deactivateCompany(Company company) throws BusinessException;

	List<DialogContextVar> getListDialogContextVars();

	List<DialogContextVar> getContextVarsFromDialog(Dialog dialog);

	boolean isValidScript(Script script) throws BusinessException;

	String debugScript(Dialog dialog, Script script);

	String executeScript(Dialog dialog, Script script) throws BusinessException;

	Boolean evaluateBooleanScript(Dialog dialog, Script script) throws BusinessException;
	
	void saveScript(Script script) throws BusinessException;

	void saveBlackListExpression(BlackListExpression expression) throws BusinessException;

	List<BlackListExpression> listBlackListExpressionByScriptType(ScriptType scriptType);

	void testScriptAgainstBlackListExpression(String scriptCode, ScriptType scriptType) throws BusinessException;

	void dropBlackListExpression(BlackListExpression expression) throws BusinessException;

}
