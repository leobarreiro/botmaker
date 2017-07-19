package org.javaleo.grandpa.ejb.business.bot;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import org.javaleo.grandpa.ejb.entities.bot.Bot;
import org.javaleo.grandpa.ejb.enums.BotType;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.filters.BotFilter;

@Local
public interface IBotBusiness extends Serializable {

	Bot validateBotTelegram(final String token) throws BusinessException;

	void saveBot(Bot bot) throws BusinessException;

	void deactivateBot(Bot bot) throws BusinessException;

	void reactivateBot(Bot bot) throws BusinessException;

	List<Bot> searchBot(BotFilter filter);

	List<Bot> listLastBotsFromCompanyUser();

	List<Bot> listValidAndActiveBots(BotType botType);

}
