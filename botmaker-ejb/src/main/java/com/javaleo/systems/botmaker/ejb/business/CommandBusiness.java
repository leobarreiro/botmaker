package com.javaleo.systems.botmaker.ejb.business;

import java.util.ArrayList;
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
import com.javaleo.systems.botmaker.ejb.enums.ScriptType;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.pojos.Dialog;
import com.javaleo.systems.botmaker.ejb.utils.BotMakerUtils;
import com.javaleo.systems.botmaker.ejb.utils.GroovyScriptRunnerUtils;

@Stateless
public class CommandBusiness implements ICommandBusiness {

	private static final long serialVersionUID = 1L;

	@Inject
	private IPersistenceBasic<Command> persistence;

	@Inject
	private GroovyScriptRunnerUtils scriptRunner;

	@Inject
	private Logger LOG;

	@Override
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
	public void saveCommand(Command command) throws BusinessException {
		command.setKey(StringUtils.lowerCase(command.getKey()));
		persistence.saveOrUpdate(command);
	}

	@Override
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
		cq.where(cb.and(cb.equal(joinBot.get("id"), bot.getId()), cb.equal(fromCommand.get("key"), clearText)));
		cq.select(fromCommand);
		return persistence.getSingleResult(cq);
	}

	@Override
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
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void postProcessCommand(Dialog dialog, Command command) {
		if (command.getPostProcess() && command.getPostProcessScriptType().equals(ScriptType.GROOVY)) {
			try {
				String postProcessed = (String) scriptRunner.evaluateScript(command.getPostProcessScript(), dialog.getContextVars());
				dialog.setPostProcessedResult(postProcessed);
			} catch (Exception e) {
				LOG.error(e.getMessage());
			}
		}
	}

}
