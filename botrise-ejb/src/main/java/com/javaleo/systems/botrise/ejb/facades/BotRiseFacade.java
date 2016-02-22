package com.javaleo.systems.botrise.ejb.facades;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

import com.javaleo.systems.botrise.ejb.business.IBotBusiness;
import com.javaleo.systems.botrise.ejb.entities.Bot;
import com.javaleo.systems.botrise.ejb.exceptions.BotRiseException;

@Named
@Stateless
public class BotRiseFacade implements IBotRiseFacade {

	private static final long serialVersionUID = 1L;

	@Inject
	private IBotBusiness botBusiness;

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

}
