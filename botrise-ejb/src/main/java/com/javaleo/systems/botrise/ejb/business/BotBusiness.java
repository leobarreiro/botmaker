package com.javaleo.systems.botrise.ejb.business;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.javaleo.libs.botgram.exceptions.BotGramException;
import org.javaleo.libs.botgram.model.User;
import org.javaleo.libs.botgram.response.GetMeResponse;
import org.javaleo.libs.botgram.service.BotGramConfig;
import org.javaleo.libs.botgram.service.IBotGramService;
import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;

import com.javaleo.systems.botrise.ejb.entities.Bot;
import com.javaleo.systems.botrise.ejb.enums.BotType;
import com.javaleo.systems.botrise.ejb.exceptions.BotRiseException;
import com.javaleo.systems.botrise.ejb.filters.BotFilter;

@Stateless
public class BotBusiness implements IBotBusiness {

	private static final long serialVersionUID = 1L;

	@Inject
	private IPersistenceBasic<Bot> persistence;

	@Inject
	private IBotGramService botgramService;

	@Override
	public Bot validateBotTelegram(String token) throws BotRiseException {
		if (StringUtils.isBlank(token)) {
			throw new BotRiseException("Token não pode ser nulo ou estar em branco.");
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
			throw new BotRiseException("O Token informado não foi validado no Telegram.", e);
		}
		return bot;
	}

	@Override
	public void saveBot(Bot bot) throws BotRiseException {
		persistence.saveOrUpdate(bot);
	}

	@Override
	public List<Bot> searchBot(BotFilter filter) {
		CriteriaBuilder builder = persistence.getCriteriaBuilder();
		CriteriaQuery<Bot> query = builder.createQuery(Bot.class);
		Root<Bot> from = query.from(Bot.class);
		if (StringUtils.isNotBlank(filter.getName())) {
			query.where(builder.equal(from.get("name"), filter.getName()));
		}
		return persistence.getResultList(query);
	}

}
