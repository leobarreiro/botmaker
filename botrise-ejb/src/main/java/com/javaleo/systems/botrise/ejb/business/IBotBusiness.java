package com.javaleo.systems.botrise.ejb.business;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import com.javaleo.systems.botrise.ejb.entities.Bot;
import com.javaleo.systems.botrise.ejb.exceptions.BusinessException;
import com.javaleo.systems.botrise.ejb.filters.BotFilter;

@Local
public interface IBotBusiness extends Serializable {

	Bot validateBotTelegram(final String token) throws BusinessException;

	void saveBot(Bot bot) throws BusinessException;

	List<Bot> searchBot(BotFilter filter);

}
