package com.javaleo.systems.botmaker.ejb.schedules;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.libs.botgram.exceptions.BotGramException;
import org.javaleo.libs.botgram.model.Update;
import org.javaleo.libs.botgram.response.GetUpdatesResponse;
import org.javaleo.libs.botgram.service.BotGramConfig;
import org.javaleo.libs.botgram.service.BotGramService;
import org.slf4j.Logger;

import com.javaleo.systems.botmaker.ejb.business.IBotBusiness;
import com.javaleo.systems.botmaker.ejb.entities.Bot;

@Named
@Local
@Stateless
public class BotListenerSchedule implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IBotBusiness botBusiness;

	@Inject
	private Logger LOG;

	@Inject
	private ManagerUtils managerUtils;

	@Schedule(dayOfWeek = "*", hour = "*", minute = "*", second = "*/30", persistent = false)
	public void listenBotUpdates() {
		LOG.info("Verificando Updates dos Bots ativos.");
		List<Bot> bots = botBusiness.listActiveBots();
		for (Bot bot : bots) {
			BotGramConfig config = new BotGramConfig();
			config.setToken(bot.getToken());
			BotGramService service = new BotGramService(config);
			try {
				GetUpdatesResponse updatesResponse = service.getUpdates(managerUtils.getNextUpdateOffsetFromBot(bot), 20);
				List<Update> updates = updatesResponse.getUpdates();
				managerUtils.addUpdatesToBot(bot, updates);
			} catch (BotGramException e) {
				LOG.error(e.getMessage());
			}
		}

	}
}
