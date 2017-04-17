package org.javaleo.grandpa.ejb.facades;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import org.javaleo.grandpa.ejb.entities.BlackListExpression;
import org.javaleo.grandpa.ejb.entities.Bot;
import org.javaleo.grandpa.ejb.entities.Command;
import org.javaleo.grandpa.ejb.entities.Company;
import org.javaleo.grandpa.ejb.entities.Page;
import org.javaleo.grandpa.ejb.entities.Question;
import org.javaleo.grandpa.ejb.entities.Script;
import org.javaleo.grandpa.ejb.entities.Token;
import org.javaleo.grandpa.ejb.entities.User;
import org.javaleo.grandpa.ejb.entities.UserPreference;
import org.javaleo.grandpa.ejb.entities.Validator;
import org.javaleo.grandpa.ejb.enums.ScriptType;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.filters.BotFilter;
import org.javaleo.grandpa.ejb.filters.PageFilter;
import org.javaleo.grandpa.ejb.filters.ValidatorFilter;
import org.javaleo.grandpa.ejb.pojos.Dialog;
import org.javaleo.grandpa.ejb.pojos.DialogContextVar;

@Local
public interface IBotMakerFacade extends Serializable {

	Bot validateBotTelegram(String token) throws BusinessException;

	void saveBot(Bot bot) throws BusinessException;

	void deactivateBot(Bot bot) throws BusinessException;

	void reactivateBot(Bot bot) throws BusinessException;

	List<Bot> searchBot(BotFilter filter);

	void validateUser(User user, String password, String passwordReview) throws BusinessException;

	void saveUser(User user, String password, String passwordReview) throws BusinessException;

	void sendMessageRecoveryLoginToUser(String email, String emailReview) throws BusinessException;

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

	List<Script> listGenericScriptsFromScriptType(ScriptType scriptType);

	Script getScriptToEdition(Long id);

	void saveScript(Script script) throws BusinessException;

	void saveBlackListExpression(BlackListExpression expression) throws BusinessException;

	List<BlackListExpression> listBlackListExpressionByScriptType(ScriptType scriptType);

	void testScriptAgainstBlackListExpression(String scriptCode, ScriptType scriptType) throws BusinessException;

	void dropBlackListExpression(BlackListExpression expression) throws BusinessException;

	Token getTokenByUUID(String uuid) throws BusinessException;

	List<Script> listLastGenericScriptsFromUser();

	List<Script> listLastCommandScriptsEditedByUser();

	List<Script> listAllGenericScriptsFromCompany();

	void savePage(Page page) throws BusinessException;

	List<Page> listPages(PageFilter pageFilter);

	void disablePage(Page page) throws BusinessException;

	List<Page> listLastPagesEdited();

}
