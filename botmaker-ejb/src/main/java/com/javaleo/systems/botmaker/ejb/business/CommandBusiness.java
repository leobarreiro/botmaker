package com.javaleo.systems.botmaker.ejb.business;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;

import com.javaleo.systems.botmaker.ejb.entities.Bot;
import com.javaleo.systems.botmaker.ejb.entities.Command;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;

@Stateless
public class CommandBusiness implements ICommandBusiness {

	private static final long serialVersionUID = 1L;

	@Inject
	private IPersistenceBasic<Command> persistence;

	@Override
	public List<Command> listCommandsByBot(Bot bot) {
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<Command> cq = cb.createQuery(Command.class);
		Root<Command> fromCommand = cq.from(Command.class);
		Join<Command, Bot> joinBot = fromCommand.join("bot", JoinType.INNER);
		cq.where(cb.equal(joinBot.get("id"), bot.getId()));
		cq.select(fromCommand);
		cq.orderBy(cb.asc(fromCommand.get("key")));
		// persistence.logQuery(cq);
		return persistence.getResultList(cq);
	}

	@Override
	public void saveCommand(Command command) throws BusinessException {
		persistence.saveOrUpdate(command);
	}

	@Override
	public Command getCommandByBotAndKey(Bot bot, String text) {
		String clearText = text.replaceAll("/", "");
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<Command> cq = cb.createQuery(Command.class);
		Root<Command> fromCommand = cq.from(Command.class);
		Join<Command, Bot> joinBot = fromCommand.join("bot", JoinType.INNER);
		cq.where(cb.and(cb.equal(joinBot.get("id"), bot.getId()), cb.equal(fromCommand.get("key"), clearText)));
		cq.select(fromCommand);
		return persistence.getSingleResult(cq);
	}

}
