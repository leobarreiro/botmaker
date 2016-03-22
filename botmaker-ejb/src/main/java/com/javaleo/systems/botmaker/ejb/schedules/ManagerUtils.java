package com.javaleo.systems.botmaker.ejb.schedules;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;

import org.javaleo.libs.botgram.model.Update;

import com.javaleo.systems.botmaker.ejb.entities.Bot;
import com.javaleo.systems.botmaker.ejb.pojos.Dialog;

@Local
@Singleton
@ApplicationScoped
public class ManagerUtils implements Serializable {

	private static final long serialVersionUID = 1L;

	private ConcurrentMap<Long, Set<Update>> botUpdatesMap;
	private ConcurrentMap<Long, Integer> lastUpdateIdMap;
	private ConcurrentMap<Long, Set<Dialog>> dialogsPerBotMap;

	@PostConstruct
	public void startInit() {
		this.botUpdatesMap = new ConcurrentHashMap<Long, Set<Update>>();
		this.lastUpdateIdMap = new ConcurrentHashMap<Long, Integer>();
		this.dialogsPerBotMap = new ConcurrentHashMap<Long, Set<Dialog>>();
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
		lastUpdateIdMap.put(bot.getId(), getMaxIdFromUpdatesList(botUpdatesMap.get(bot.getId())));
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
		return (lastUpdateIdMap.containsKey(bot.getId())) ? lastUpdateIdMap.get(bot.getId()) : 0;
	}

	public int getNextUpdateOffsetFromBot(Bot bot) {
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

}
