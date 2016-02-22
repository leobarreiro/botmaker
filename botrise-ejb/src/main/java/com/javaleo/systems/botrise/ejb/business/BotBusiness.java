package com.javaleo.systems.botrise.ejb.business;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.javaleo.libs.botgram.exceptions.BotGramException;
import org.javaleo.libs.botgram.model.User;
import org.javaleo.libs.botgram.response.GetMeResponse;
import org.javaleo.libs.botgram.service.BotGramConfig;
import org.javaleo.libs.botgram.service.IBotGramService;
import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;

import com.javaleo.systems.botrise.ejb.entities.Bot;
import com.javaleo.systems.botrise.ejb.enums.BotType;
import com.javaleo.systems.botrise.ejb.exceptions.BotRiseException;

@Stateless
public class BotBusiness implements IBotBusiness {

	private static final long serialVersionUID = 1L;

	@Inject
	private IPersistenceBasic<Bot> persistence;

	@Inject
	private IBotGramService botgramService;

	@Override
	public Bot validateBotTelegram(String token) throws BotRiseException {
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
			throw new BotRiseException(e.getMessage(), e);
		}
		return bot;
	}

	@Override
	public void saveBot(Bot bot) throws BotRiseException {
		persistence.saveOrUpdate(bot);
	}

}
