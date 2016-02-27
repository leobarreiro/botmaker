package com.javaleo.systems.botrise.web.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.libs.jee.core.web.actions.AbstractCrudAction;

import com.javaleo.systems.botrise.ejb.entities.Bot;
import com.javaleo.systems.botrise.ejb.enums.BotType;
import com.javaleo.systems.botrise.ejb.exceptions.BotRiseException;
import com.javaleo.systems.botrise.ejb.facades.IBotRiseFacade;
import com.javaleo.systems.botrise.ejb.filters.BotFilter;
import com.javaleo.systems.botrise.web.action.MsgAction.MessageType;

@Named
@ConversationScoped
public class BotAction extends AbstractCrudAction<Bot> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IBotRiseFacade facade;

	@Inject
	private MsgAction msgAction;

	@Inject
	private Conversation conversation;

	private String token;
	private BotType botType;
	private BotFilter filter;
	private CRUD crudOp;
	private List<BotType> botTypes;
	private Bot bot;
	private List<Bot> bots;

	@Override
	public String loadNewScreen() {
		startNewConversation();
		botTypes = Arrays.asList(BotType.values());
		bot = new Bot();
		filter = new BotFilter();
		return "/pages/bot/bot1.jsf?faces-redirect=true";
	}

	@Override
	public String loadSearchScreen() {
		startNewConversation();
		filter = new BotFilter();
		bots = new ArrayList<Bot>();
		return "/pages/bot/bot-search.jsf?faces-redirect=true";
	}

	@Override
	public String search() {
		bots = facade.searchBot(filter);
		return "/pages/bot/bot-search.jsf?faces-redirect=true";
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
	public String loadEditScreen(Bot bot) {
		this.bot = bot;
		return "/pages/bot/bot1.jsf?faces-redirect=true";
	}

	@Override
	public String loadViewScreen(Bot bot) {
		this.bot = bot;
		return "/pages/bot/bot-details.jsf?faces-redirect=true";
	}

	@Override
	public String finish() {
		endConversation();
		return "/pages/bot/bot1.jsf?faces-redirect=true";
	}

	@Override
	public CRUD getCrudOp() {
		return crudOp;
	}

	public String validateToken() {
		try {
			bot = facade.validateBotTelegram(token);
			return "/pages/bot/bot2.jsf?faces-redirect=true";
		} catch (BotRiseException e) {
			msgAction.addMessage(MessageType.ERROR, e.getMessage());
			return "/pages/bot/bot1.jsf?faces-redirect=true";
		}
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

	public BotFilter getFilter() {
		return filter;
	}

	public void setFilter(BotFilter filter) {
		this.filter = filter;
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
