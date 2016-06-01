package com.javaleo.systems.botmaker.ejb.business;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;
import org.slf4j.Logger;

import com.javaleo.systems.botmaker.ejb.entities.Bot;
import com.javaleo.systems.botmaker.ejb.entities.Command;
import com.javaleo.systems.botmaker.ejb.entities.Script;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.pojos.Dialog;
import com.javaleo.systems.botmaker.ejb.security.BotMakerCredentials;
import com.javaleo.systems.botmaker.ejb.utils.BotMakerUtils;

@Stateless
public class CommandBusiness implements ICommandBusiness {

	private static final long serialVersionUID = 1L;

	@Inject
	private IPersistenceBasic<Command> persistence;

	@Inject
	private IPersistenceBasic<Script> scriptPersistence;

	@Inject
	private IScriptBusiness scriptBiz;

	@Inject
	private BotMakerCredentials credentials;

	@Inject
	private Logger LOG;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Command> listCommandsByBot(Bot bot) {
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<Command> cq = cb.createQuery(Command.class);
		Root<Command> fromCommand = cq.from(Command.class);
		Join<Command, Bot> joinBot = fromCommand.join("bot", JoinType.INNER);
		cq.where(cb.equal(joinBot.get("id"), bot.getId()));
		cq.select(fromCommand);
		cq.orderBy(cb.asc(fromCommand.get("key")));
		return persistence.getResultList(cq);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveCommand(Command command) throws BusinessException {
		if (StringUtils.isBlank(command.getKey())) {
			throw new BusinessException("Command key must be filled.");
		}
		Command otherCommand = getCommandByBotAndKey(command.getBot(), command.getKey());
		if (otherCommand != null && (command.getId() == null || !otherCommand.getId().equals(command.getId()))) {
			throw new BusinessException("There is another command for this bot with the same Key. Please choose other Key name.");
		}

		Calendar cal = Calendar.getInstance();
		if (command.getPostProcess() && command.getPostScript() != null && StringUtils.isNotEmpty(command.getPostScript().getCode())) {
			Script script = command.getPostScript();
			if (script.getId() == null) {
				script.setAuthor(credentials.getUser());
				script.setCreated(cal.getTime());
			}
			script.setModified(cal.getTime());

			script.setValid(scriptBiz.isValidScript(script));
			script.setEnabled(scriptBiz.isValidScript(script));

			script.setCommand(command);
			scriptPersistence.saveOrUpdate(script);
			command.setPostScript(script);
		} else {
			command.setPostScript(null);
		}
		command.setKey(StringUtils.lowerCase(command.getKey()));
		persistence.saveOrUpdate(command);
		persistence.getEntityManager().flush();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Command getCommandById(Long id) {
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<Command> cq = cb.createQuery(Command.class);
		Root<Command> fromCommand = cq.from(Command.class);
		fromCommand.join("questions", JoinType.LEFT);
		fromCommand.join("postScript", JoinType.LEFT);
		cq.where(cb.equal(fromCommand.get("id"), id));
		cq.select(fromCommand);
		return persistence.getSingleResult(cq);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Command getCommandByBotAndKey(Bot bot, String text) {
		String clearText = StringUtils.lowerCase(StringUtils.replace(text, "/", ""));
		if (clearText == null) {
			return null;
		}
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<Command> cq = cb.createQuery(Command.class);
		Root<Command> fromCommand = cq.from(Command.class);
		Join<Command, Bot> joinBot = fromCommand.join("bot", JoinType.INNER);
		// Join<Question, Command> joinQuestions = fromCommand.join("questions", JoinType.LEFT);
		fromCommand.join("questions", JoinType.LEFT);
		fromCommand.join("postScript", JoinType.LEFT);
		cq.where(cb.and(cb.equal(joinBot.get("id"), bot.getId()), cb.equal(fromCommand.get("key"), clearText)));
		cq.select(fromCommand);
		return persistence.getSingleResult(cq);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<List<String>> convertCommandsToOptions(Bot bot) {
		List<Command> commands = listCommandsByBot(bot);
		List<String> keyCommands = new ArrayList<String>();
		for (Command c : commands) {
			keyCommands.add("/".concat(c.getKey()));
		}
		return BotMakerUtils.convertArrayOfArrays(keyCommands, 3);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void dropCommand(Command command) {
		Command deleteCommand = persistence.find(Command.class, command.getId());
		persistence.remove(deleteCommand);
		persistence.getEntityManager().flush();
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void postProcessCommand(Dialog dialog, Command command) throws BusinessException {
		if (!scriptBiz.isValidScript(command.getPostScript())) {
			throw new BusinessException(
					MessageFormat.format("Trying to execute a Post Script not valid [Bot:{0}|Command:{1}]", command.getBot().getId().toString(), command.getId().toString()));
		}
		String postProcessed = scriptBiz.executeScript(dialog, command.getPostScript());
		dialog.setPostProcessedResult(postProcessed);
	}

}
