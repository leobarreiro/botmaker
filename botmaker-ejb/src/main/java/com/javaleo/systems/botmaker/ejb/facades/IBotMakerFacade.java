package com.javaleo.systems.botmaker.ejb.facades;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import org.javaleo.libs.botgram.enums.ParseMode;

import com.javaleo.systems.botmaker.ejb.entities.Bot;
import com.javaleo.systems.botmaker.ejb.entities.Command;
import com.javaleo.systems.botmaker.ejb.entities.Company;
import com.javaleo.systems.botmaker.ejb.entities.Question;
import com.javaleo.systems.botmaker.ejb.entities.User;
import com.javaleo.systems.botmaker.ejb.entities.UserPreference;
import com.javaleo.systems.botmaker.ejb.entities.Validator;
import com.javaleo.systems.botmaker.ejb.enums.ScriptType;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.filters.BotFilter;
import com.javaleo.systems.botmaker.ejb.filters.ValidatorFilter;

@Local
public interface IBotMakerFacade extends Serializable {

	/**
	 * @param token
	 * @return
	 * @throws BusinessException
	 * @see com.javaleo.systems.botmaker.ejb.business.IBotBusiness#validateBotTelegram(java.lang.String)
	 */
	Bot validateBotTelegram(String token) throws BusinessException;

	/**
	 * @param bot
	 * @throws BusinessException
	 * @see com.javaleo.systems.botmaker.ejb.business.IBotBusiness#saveBot(com.javaleo.systems.botmaker.ejb.entities.Bot)
	 */
	void saveBot(Bot bot) throws BusinessException;

	/**
	 * @param bot
	 * @throws BusinessException
	 * @see com.javaleo.systems.botmaker.ejb.business.IBotBusiness#deactivateBot(com.javaleo.systems.botmaker.ejb.entities.Bot)
	 */
	void deactivateBot(Bot bot) throws BusinessException;

	/**
	 * @param filter
	 * @return
	 * @see com.javaleo.systems.botmaker.ejb.business.IBotBusiness#searchBot(com.javaleo.systems.botmaker.ejb.filters.BotFilter)
	 */
	List<Bot> searchBot(BotFilter filter);

	/**
	 * @param user
	 * @param password
	 * @throws BusinessException
	 * @see com.javaleo.systems.botmaker.ejb.business.IUserBusiness#saveUser(com.javaleo.systems.botmaker.ejb.entities.User,
	 *      java.lang.String)
	 */
	void saveUser(User user, String password) throws BusinessException;

	/**
	 * @param username
	 * @param passphrase
	 * @return
	 * @see com.javaleo.systems.botmaker.ejb.business.IUserBusiness#findUserByUsernameAndPassphrase(java.lang.String,
	 *      java.lang.String)
	 */
	User findUserByUsernameAndPassword(String username, String passphrase);

	/**
	 * @param bot
	 * @return
	 * @see com.javaleo.systems.botmaker.ejb.business.ICommandBusiness#listCommandsByBot(com.javaleo.systems.botmaker.ejb.entities.Bot)
	 */
	List<Command> listCommandsByBot(Bot bot);

	/**
	 * @param command
	 * @throws BusinessException
	 * @see com.javaleo.systems.botmaker.ejb.business.ICommandBusiness#saveCommand(com.javaleo.systems.botmaker.ejb.entities.Command)
	 */
	void saveCommand(Command command) throws BusinessException;

	/**
	 * @param idCommand
	 * @param scriptCode
	 * @param parseMode
	 * @param scriptType
	 * @throws BusinessException
	 * @see com.javaleo.systems.botmaker.ejb.business.ICommandBusiness#saveCommandPostScript(java.lang.Long,
	 *      java.lang.String, org.javaleo.libs.botgram.enums.ParseMode,
	 *      com.javaleo.systems.botmaker.ejb.enums.ScriptType)
	 */
	void saveCommandPostScript(Long idCommand, String scriptCode, ParseMode parseMode, ScriptType scriptType) throws BusinessException;

	/**
	 * @param question
	 * @throws BusinessException
	 * @see com.javaleo.systems.botmaker.ejb.business.IQuestionBusiness#saveQuestion(com.javaleo.systems.botmaker.ejb.entities.Question)
	 */
	void saveQuestion(Question question) throws BusinessException;

	/**
	 * @param idQuestion
	 * @param code
	 * @param parseMode
	 * @param scriptType
	 * @throws BusinessException
	 * @see com.javaleo.systems.botmaker.ejb.business.IQuestionBusiness#saveQuestionCode(java.lang.Long,
	 *      java.lang.String, org.javaleo.libs.botgram.enums.ParseMode,
	 *      com.javaleo.systems.botmaker.ejb.enums.ScriptType)
	 */
	void saveQuestionCode(Long idQuestion, String code, ParseMode parseMode, ScriptType scriptType) throws BusinessException;

	/**
	 * 
	 * @see com.javaleo.systems.botmaker.ejb.business.ICommandBusiness#dropCommand(com.javaleo.systems.botmaker.ejb.entities
	 *      .Command)
	 */
	void dropCommand(Command command);

	/**
	 * @param command
	 * @return
	 * @see com.javaleo.systems.botmaker.ejb.business.IQuestionBusiness#listQuestionsFromCommand(com.javaleo.systems.botmaker.ejb.entities.Command)
	 */
	List<Question> listQuestionsFromCommand(Command command);

	/**
	 * @param command
	 * @return
	 * @see com.javaleo.systems.botmaker.ejb.business.IQuestionBusiness#getLastQuestionFromCommand(com.javaleo.systems.botmaker.ejb.entities.Command)
	 */
	Question getLastQuestionFromCommand(Command command);

	/**
	 * @param question
	 * @throws BusinessException
	 * @see com.javaleo.systems.botmaker.ejb.business.IQuestionBusiness#upQuestionOrder(com.javaleo.systems.botmaker.ejb.entities.Question)
	 */
	void upQuestionOrder(Question question) throws BusinessException;

	/**
	 * @return
	 * @see com.javaleo.systems.botmaker.ejb.business.IUserBusiness#listAllUsers()
	 */
	List<User> listAllUsers();

	/**
	 * @return
	 * @see com.javaleo.systems.botmaker.ejb.business.IBotBusiness#listLastBotsFromCompanyUser()
	 */
	List<Bot> listLastBotsFromCompanyUser();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.javaleo.systems.botmaker.ejb.facades.IBotMakerFacade#saveSnippetCode(com.javaleo.systems.botmaker.ejb.entities
	 * .SnippetCode)
	 */
	void saveValidator(Validator validator) throws BusinessException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.javaleo.systems.botmaker.ejb.facades.IBotMakerFacade#searchSnippetCodeByFilter(com.javaleo.systems.botmaker
	 * .ejb.filters.SnippetCodeFilter)
	 */
	List<Validator> searchValidatorByFilter(ValidatorFilter filter);

	/**
	 * @param user
	 * @return
	 * @see com.javaleo.systems.botmaker.ejb.business.IUserPreferenceBusiness#listPreferencesByUser(com.javaleo.systems.botmaker.ejb.entities.User)
	 */
	List<UserPreference> listPreferencesByUser(User user);

	/**
	 * @param user
	 * @param name
	 * @return
	 * @see com.javaleo.systems.botmaker.ejb.business.IUserPreferenceBusiness#getPreferenceByUserAndName(com.javaleo.systems.botmaker.ejb.entities.User,
	 *      java.lang.String)
	 */
	UserPreference getPreferenceByUserAndName(User user, String name);

	/**
	 * @param user
	 * @param userPreference
	 * @see com.javaleo.systems.botmaker.ejb.business.IUserPreferenceBusiness#savePreference(com.javaleo.systems.botmaker.ejb.entities.User,
	 *      com.javaleo.systems.botmaker.ejb.entities.UserPreference)
	 */
	void savePreference(User user, UserPreference userPreference);

	/**
	 * @param userPreference
	 * @see com.javaleo.systems.botmaker.ejb.business.IUserPreferenceBusiness#removePreference(com.javaleo.systems.botmaker.ejb.entities.UserPreference)
	 */
	void removePreference(UserPreference userPreference);

	List<Company> listAllCompanies();

	void saveCompany(Company company) throws BusinessException;

	void deactivateCompany(Company company) throws BusinessException;

}
