package com.javaleo.systems.botrise.ejb.business;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import com.javaleo.systems.botrise.ejb.entities.Bot;
import com.javaleo.systems.botrise.ejb.exceptions.BotRiseException;
import com.javaleo.systems.botrise.ejb.filters.BotFilter;

@Local
public interface IBotBusiness extends Serializable {

	Bot validateBotTelegram(final String token) throws BotRiseException;

	void saveBot(Bot bot) throws BotRiseException;

	List<Bot> searchBot(BotFilter filter);

}
