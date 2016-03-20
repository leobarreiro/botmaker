package com.javaleo.systems.botmaker.ejb.business;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.javaleo.libs.botgram.exceptions.BotGramException;
import org.javaleo.libs.botgram.model.User;
import org.javaleo.libs.botgram.response.GetMeResponse;
import org.javaleo.libs.botgram.service.BotGramConfig;
import org.javaleo.libs.botgram.service.IBotGramService;
import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;
import org.slf4j.Logger;

import com.javaleo.systems.botmaker.ejb.entities.Bot;
import com.javaleo.systems.botmaker.ejb.entities.Command;
import com.javaleo.systems.botmaker.ejb.entities.Question;
import com.javaleo.systems.botmaker.ejb.enums.BotType;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.filters.BotFilter;

@Stateless
public class BotBusiness implements IBotBusiness {

	private static final long serialVersionUID = 1L;

	@Inject
	private Logger LOG;

	@Inject
	private IPersistenceBasic<Bot> persistence;

	@Inject
	private IBotGramService botgramService;

	@Override
	public Bot validateBotTelegram(String token) throws BusinessException {
		if (StringUtils.isBlank(token)) {
			throw new BusinessException("Token não pode ser nulo ou estar em branco.");
		}
		Bot bot = new Bot();
		BotGramConfig config = new BotGramConfig();
		config.setToken(token);
		botgramService.setConfiguration(config);
		try {
			GetMeResponse response = botgramService.getMe();
			User user = response.getUser();
			bot.setBotType(BotType.TELEGRAM);
			bot.setName(user.getFirstName());
			bot.setToken(token);
		} catch (BotGramException e) {
			throw new BusinessException("O Token informado não foi validado no Telegram.", e);
		}
		return bot;
	}

	@Override
	public void saveBot(Bot bot) throws BusinessException {
		persistence.saveOrUpdate(bot);
	}

	@Override
	public List<Bot> searchBot(BotFilter filter) {
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<Bot> query = cb.createQuery(Bot.class);
		Root<Bot> from = query.from(Bot.class);
		List<Predicate> predicates = new ArrayList<Predicate>();
		if (StringUtils.isNotBlank(filter.getName())) {
			Expression<String> path = from.get("name");
			predicates.add(cb.like(path, "%" + filter.getName() + "%"));
		}
		if (filter.getBotType() != null) {
			predicates.add(cb.equal(from.get("botType"), filter.getBotType()));
		}
		if (filter.getActive() != null) {
			predicates.add(cb.equal(from.get("active"), filter.getActive()));
		}
		query.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		persistence.logQuery(query);
		return persistence.getResultList(query);
	}

	@Override
	public List<Bot> listActiveBots() {
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<Bot> cq = cb.createQuery(Bot.class);
		Root<Bot> from = cq.from(Bot.class);
		Join<Bot, Command> joinCommand = from.join("commands", JoinType.INNER);
		Join<Command, Question> joinQuestion = joinCommand.join("questions", JoinType.INNER);
		cq.where(cb.equal(from.get("active"), true));
		return persistence.getResultList(cq);
	}

}
