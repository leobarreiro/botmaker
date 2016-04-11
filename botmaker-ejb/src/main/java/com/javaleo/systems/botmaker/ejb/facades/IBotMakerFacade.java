package com.javaleo.systems.botmaker.ejb.facades;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import com.javaleo.systems.botmaker.ejb.entities.Bot;
import com.javaleo.systems.botmaker.ejb.entities.Command;
import com.javaleo.systems.botmaker.ejb.entities.Question;
import com.javaleo.systems.botmaker.ejb.entities.Snippet;
import com.javaleo.systems.botmaker.ejb.entities.User;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.filters.BotFilter;
import com.javaleo.systems.botmaker.ejb.filters.SnippetFilter;

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
	 * @param question
	 * @throws BusinessException
	 * @see com.javaleo.systems.botmaker.ejb.business.IQuestionBusiness#saveQuestion(com.javaleo.systems.botmaker.ejb.entities.Question)
	 */
	void saveQuestion(Question question) throws BusinessException;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.javaleo.systems.botmaker.ejb.facades.IBotMakerFacade#saveSnippetCode(com.javaleo.systems.botmaker.ejb.entities
	 * .SnippetCode)
	 */
	void saveSnippet(Snippet snippetCode) throws BusinessException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.javaleo.systems.botmaker.ejb.facades.IBotMakerFacade#searchSnippetCodeByFilter(com.javaleo.systems.botmaker
	 * .ejb.filters.SnippetCodeFilter)
	 */
	List<Snippet> searchSnippetByFilter(SnippetFilter filter);

}
