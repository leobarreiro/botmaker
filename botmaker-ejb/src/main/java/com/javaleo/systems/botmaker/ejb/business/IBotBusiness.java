package com.javaleo.systems.botmaker.ejb.business;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import com.javaleo.systems.botmaker.ejb.entities.Bot;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.filters.BotFilter;

@Local
public interface IBotBusiness extends Serializable {

	Bot validateBotTelegram(final String token) throws BusinessException;

	void saveBot(Bot bot) throws BusinessException;

	List<Bot> searchBot(BotFilter filter);

}