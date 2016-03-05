package com.javaleo.systems.botrise.ejb.facades;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

import com.javaleo.systems.botrise.ejb.business.IBotBusiness;
import com.javaleo.systems.botrise.ejb.business.ICommandBusiness;
import com.javaleo.systems.botrise.ejb.business.IQuestionBusiness;
import com.javaleo.systems.botrise.ejb.business.IUserBusiness;
import com.javaleo.systems.botrise.ejb.entities.Bot;
import com.javaleo.systems.botrise.ejb.entities.Command;
import com.javaleo.systems.botrise.ejb.entities.Question;
import com.javaleo.systems.botrise.ejb.entities.User;
import com.javaleo.systems.botrise.ejb.exceptions.BotRiseException;
import com.javaleo.systems.botrise.ejb.filters.BotFilter;

@Named
@Stateless
public class BotRiseFacade implements IBotRiseFacade {

	private static final long serialVersionUID = 1L;

	@Inject
	private IBotBusiness botBusiness;

	@Inject
	private ICommandBusiness commandBusiness;

	@Inject
	private IQuestionBusiness questionBusiness;

	@Inject
	private IUserBusiness userBusiness;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.javaleo.systems.botrise.ejb.facades.IBotRiseFacade#validateBotTelegram(java.lang.String)
	 */
	@Override
	public Bot validateBotTelegram(String token) throws BotRiseException {
		return botBusiness.validateBotTelegram(token);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.javaleo.systems.botrise.ejb.facades.IBotRiseFacade#saveBot(com.javaleo.systems.botrise.ejb.entities.Bot)
	 */
	@Override
	public void saveBot(Bot bot) throws BotRiseException {
		botBusiness.saveBot(bot);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.javaleo.systems.botrise.ejb.facades.IBotRiseFacade#searchBot(com.javaleo.systems.botrise.ejb.filters.BotFilter
	 * )
	 */
	@Override
	public List<Bot> searchBot(BotFilter filter) {
		return botBusiness.searchBot(filter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.javaleo.systems.botrise.ejb.facades.IBotRiseFacade#saveUser(com.javaleo.systems.botrise.ejb.entities.User,
	 * java.lang.String)
	 */
	@Override
	public void saveUser(User user, String password) throws BotRiseException {
		userBusiness.saveUser(user, password);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.javaleo.systems.botrise.ejb.facades.IBotRiseFacade#findUserByUsernameAndPassphrase(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public User findUserByUsernameAndPassphrase(String username, String passphrase) {
		return userBusiness.findUserByUsernameAndPassphrase(username, passphrase);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.javaleo.systems.botrise.ejb.facades.IBotRiseFacade#listCommandsByBot(com.javaleo.systems.botrise.ejb.entities
	 * .Bot)
	 */
	@Override
	public List<Command> listCommandsByBot(Bot bot) {
		return commandBusiness.listCommandsByBot(bot);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.javaleo.systems.botrise.ejb.facades.IBotRiseFacade#saveCommand(com.javaleo.systems.botrise.ejb.entities.Command
	 * )
	 */
	@Override
	public void saveCommand(Command command) throws BotRiseException {
		commandBusiness.saveCommand(command);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.javaleo.systems.botrise.ejb.facades.IBotRiseFacade#listQuestionsByCommand(com.javaleo.systems.botrise.ejb
	 * .entities.Command)
	 */
	@Override
	public List<Question> listQuestionsByCommand(Command command) {
		return questionBusiness.listQuestionsByCommand(command);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.javaleo.systems.botrise.ejb.facades.IBotRiseFacade#saveQuestion(com.javaleo.systems.botrise.ejb.entities.
	 * Question)
	 */
	@Override
	public void saveQuestion(Question question) throws BotRiseException {
		questionBusiness.saveQuestion(question);
	}

}
