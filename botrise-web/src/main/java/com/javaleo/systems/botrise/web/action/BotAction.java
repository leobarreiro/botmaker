package com.javaleo.systems.botrise.web.action;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.javaleo.libs.jee.core.model.AbstractCrudAction;

import com.javaleo.systems.botrise.ejb.entities.Bot;
import com.javaleo.systems.botrise.ejb.enums.BotType;
import com.javaleo.systems.botrise.ejb.exceptions.BotRiseException;
import com.javaleo.systems.botrise.ejb.facades.IBotRiseFacade;
import com.javaleo.systems.botrise.web.action.MsgAction.MessageType;

@Named
@ConversationScoped
public class BotAction extends AbstractCrudAction implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IBotRiseFacade facade;

	@Inject
	private MsgAction msgAction;

	@Inject
	private Conversation conversation;

	private String token;
	private BotType botType;

	private List<BotType> botTypes;
	private Bot bot;
	private List<Bot> bots;

	public String validateToken() {
		if (StringUtils.isNotBlank(token)) {
			try {
				bot = facade.validateBotTelegram(token);
				return "/pages/bot/bot2.jsf?faces-redirect=true";
			} catch (BotRiseException e) {
				msgAction.addMessage(MessageType.ERROR, e.getMessage());
				return "/pages/bot/bot1.jsf?faces-redirect=true";
			}
		}
		return "/pages/bot/bot2.jsf?faces-redirect=true";
	}

	@Override
	public String start() {
		startNewConversation();
		botTypes = Arrays.asList(BotType.values());
		bot = new Bot();
		return "/pages/bot/bot1.jsf?faces-redirect=true";
	}

	@Override
	public String search() {
		return "/pages/bot/bot1.jsf?faces-redirect=true";
	}

	@Override
	public String save() {
		try {
			facade.saveBot(bot);
			msgAction.addMessage(MessageType.INFO, "Bot salvo corretamente");
		} catch (BotRiseException e) {
			msgAction.addMessage(MessageType.ERROR, e.getMessage());
		}
		return "/pages/bot/bot1.jsf?faces-redirect=true";
	}

	@Override
	public String finish() {
		endConversation();
		return "/pages/bot/bot1.jsf?faces-redirect=true";
	}

	@Override
	public Conversation getConversation() {
		return conversation;
	}

	public List<BotType> getBotTypes() {
		return botTypes;
	}

	public void setBotTypes(List<BotType> botTypes) {
		this.botTypes = botTypes;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public BotType getBotType() {
		return botType;
	}

	public void setBotType(BotType botType) {
		this.botType = botType;
	}

	public Bot getBot() {
		return bot;
	}

	public void setBot(Bot bot) {
		this.bot = bot;
	}

	public List<Bot> getBots() {
		return bots;
	}

	public void setBots(List<Bot> bots) {
		this.bots = bots;
	}

}
