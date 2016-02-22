package com.javaleo.systems.botrise.ejb.facades;

import java.io.Serializable;

import javax.ejb.Local;

import com.javaleo.systems.botrise.ejb.entities.Bot;
import com.javaleo.systems.botrise.ejb.exceptions.BotRiseException;

@Local
public interface IBotRiseFacade extends Serializable {

	/**
	 * @param token
	 * @return
	 * @throws BotRiseException
	 * @see com.javaleo.systems.botrise.ejb.business.IBotBusiness#validateBotTelegram(java.lang.String)
	 */
	Bot validateBotTelegram(String token) throws BotRiseException;

	/**
	 * @param bot
	 * @throws BotRiseException
	 * @see com.javaleo.systems.botrise.ejb.business.IBotBusiness#saveBot(com.javaleo.systems.botrise.ejb.entities.Bot)
	 */
	void saveBot(Bot bot) throws BotRiseException;

}
