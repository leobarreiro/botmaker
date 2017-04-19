package org.javaleo.grandpa.ejb.schedules;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.javaleo.grandpa.ejb.entities.Bot;
import org.javaleo.grandpa.ejb.pojos.Dialog;
import org.javaleo.libs.botgram.model.Update;
import org.slf4j.Logger;

@Local
@Singleton
@ApplicationScoped
public class ManagerUtils implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Logger LOG;

	private ConcurrentMap<Long, Set<Update>> botUpdatesMap;
	private ConcurrentMap<Long, AtomicInteger> lastUpdateIdMap;
	private ConcurrentMap<Long, Boolean> processingBotMap;
	private ConcurrentMap<Long, Set<Dialog>> dialogsPerBotMap;

	@PostConstruct
	public void startInit() {
		this.botUpdatesMap = new ConcurrentHashMap<Long, Set<Update>>();
		this.lastUpdateIdMap = new ConcurrentHashMap<Long, AtomicInteger>();
		this.processingBotMap = new ConcurrentHashMap<Long, Boolean>();
		this.dialogsPerBotMap = new ConcurrentHashMap<Long, Set<Dialog>>();
	}

	public void startProcessBot(Bot bot) {
		processingBotMap.put(bot.getId(), true);
	}

	public void finishProcessingBot(Bot bot) {
		processingBotMap.put(bot.getId(), false);
	}

	public boolean isProcessingBot(Bot bot) {
		return (processingBotMap.containsKey(bot.getId())) ? processingBotMap.get(bot.getId()) : false;
	}

	public void addUpdatesToBot(Bot bot, List<Update> updates) {
		if (updates.isEmpty()) {
			return;
		}
		if (botUpdatesMap.containsKey(bot.getId())) {
			botUpdatesMap.get(bot.getId()).addAll(updates);
		} else {
			botUpdatesMap.put(bot.getId(), new HashSet<Update>(updates));
		}
		lastUpdateIdMap.put(bot.getId(), new AtomicInteger(getMaxIdFromUpdatesList(botUpdatesMap.get(bot.getId()))));
	}

	private Integer getMaxIdFromUpdatesList(Set<Update> updates) {
		Integer max = 0;
		for (Update update : updates) {
			max = (update.getId() > max) ? update.getId() : max;
		}
		return max;
	}

	public Set<Update> getUpdatesFromBot(Bot bot) {
		return (botUpdatesMap.containsKey(bot.getId())) ? botUpdatesMap.get(bot.getId()) : new HashSet<Update>();
	}

	public void cleanUpdatesFromDeactivatedBots(List<Bot> deactivatedBots) {
		for (Bot bot : deactivatedBots) {
			if (botUpdatesMap.containsKey(bot.getId())) {
				botUpdatesMap.remove(bot.getId());
			}
		}
	}

	public int getLastUpdateIdFromBot(Bot bot) {
		return (lastUpdateIdMap.containsKey(bot.getId())) ? lastUpdateIdMap.get(bot.getId()).get() : 0;
	}

	public int getNextUpdateOfsetFromBot(Bot bot) {
		return (getLastUpdateIdFromBot(bot) + 1);
	}

	public void addDialogToBot(Bot bot, Dialog dialog) {
		if (!dialogsPerBotMap.containsKey(bot.getId())) {
			dialogsPerBotMap.put(bot.getId(), new HashSet<Dialog>());
		}
		dialogsPerBotMap.get(bot.getId()).add(dialog);
	}

	public Set<Dialog> getDialogsFromBot(Bot bot) {
		return (dialogsPerBotMap.containsKey(bot.getId())) ? dialogsPerBotMap.get(bot.getId()) : new HashSet<Dialog>();
	}

	public void removeOldDialogWithoutInteraction() {
		for (Long idBot : dialogsPerBotMap.keySet()) {
			Set<Dialog> dialogs = new CopyOnWriteArraySet<Dialog>(dialogsPerBotMap.get(idBot));
			for (Dialog d : dialogs) {
				// 2 days ago
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_MONTH, -2);
				if (d.getLastInteraction() < cal.getTimeInMillis()) {
					dialogs.remove(d);
				}
			}
			dialogsPerBotMap.put(idBot, dialogs);
		}
	}

	public void updateDialogToBot(Bot bot, Dialog dialog) {
		Set<Dialog> dialogs = new CopyOnWriteArraySet<Dialog>(dialogsPerBotMap.get(bot.getId()));
		for (Dialog d : dialogs) {
			if (d.getId() == dialog.getId()) {
				dialogs.remove(d);
				dialogs.add(dialog);
				break;
			}
		}
		dialogsPerBotMap.put(bot.getId(), dialogs);
	}

	public Map<String, Object> getAllContextVarsFromUserIdAndBot(Long idBot, Integer userId) {
		Map<String, Object> mapContextVars = new LinkedHashMap<String, Object>();
		if (dialogsPerBotMap.containsKey(idBot)) {
			Set<Dialog> dialogs = dialogsPerBotMap.get(idBot);
			for (Dialog d : dialogs) {
				if (d.getUserId().equals(userId)) {
					mapContextVars.putAll(d.getContextVars());
				}
			}
		}
		return mapContextVars;
	}

}
