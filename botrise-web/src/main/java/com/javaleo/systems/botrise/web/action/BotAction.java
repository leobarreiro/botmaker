package com.javaleo.systems.botrise.web.action;

import java.io.Serializable;
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
	private CommandAction commandAction;

	@Inject
	private Conversation conversation;

	private String token;
	private BotType botType;
	private BotFilter filter;
	private CRUD crudOp;
	private Bot bot;
	private List<Bot> bots;

	@Override
	public String loadNewScreen() {
		startNewConversation();
		bot = new Bot();
		filter = new BotFilter();
		return "/pages/bot/bot1.jsf?faces-redirect=true";
	}

	@Override
	public String loadSearchScreen() {
		startNewConversation();
		filter = new BotFilter();
		search();
		return "/pages/bot/bot-search.jsf?faces-redirect=true";
	}

	@Override
	public void search() {
		bots = facade.searchBot(filter);
	}

	@Override
	public String save() {
		try {
			facade.saveBot(bot);
			msgAction.addMessage(MessageType.INFO, "Bot salvo corretamente");
		} catch (BotRiseException e) {
			msgAction.addMessage(MessageType.ERROR, e.getMessage());
		}
		return loadSearchScreen();
	}

	@Override
	public String loadEditScreen() {
		this.filter = new BotFilter();
		return "/pages/bot/bot2.jsf?faces-redirect=true";
	}

	@Override
	public String loadDetailScreen(Bot bot) {
		this.bot = bot;
		commandAction.setBot(bot);
		commandAction.search();
		return "/pages/bot/bot-detail.jsf?faces-redirect=true";
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
