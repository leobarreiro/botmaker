package org.javaleo.grandpa.web.action;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.javaleo.grandpa.ejb.entities.Bot;
import org.javaleo.grandpa.ejb.entities.Command;
import org.javaleo.grandpa.ejb.enums.BotType;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.facades.IBotMakerFacade;
import org.javaleo.grandpa.ejb.filters.BotFilter;
import org.javaleo.grandpa.web.action.MsgAction.MessageType;
import org.javaleo.libs.jee.core.web.actions.AbstractCrudAction;

@Named
@ConversationScoped
public class BotAction extends AbstractCrudAction implements Serializable {

	private static final String PAGE_BOT_1 = "/pages/bot/bot1.bot?faces-redirect=true";

	private static final String PAGE_BOT_2 = "/pages/bot/bot2.bot?faces-redirect=true";

	private static final String PAGE_DETAIL = "/pages/bot/bot-detail.bot?faces-redirect=true";

	private static final String CANCEL_KEY = "/cancel";

	private static final long serialVersionUID = 1L;

	@Inject
	private IBotMakerFacade facade;

	@Inject
	private AuxAction auxAction;

	@Inject
	private MsgAction msgAction;

	@Inject
	private CommandAction commandAction;

	@Inject
	private Conversation conversation;

	private String token;
	private BotType botType;
	private BotFilter filter;
	private Bot bot;
	private List<Bot> bots;
	private String rawCommands;

	public String startNew() {
		msgAction.clear();
		startNewConversation();
		bot = new Bot();
		bot.setCancelKey(CANCEL_KEY);
		filter = new BotFilter();
		return PAGE_BOT_1;
	}

	public String list() {
		startNewConversation();
		filter = new BotFilter();
		search();
		return "/pages/bot/bot-search.bot?faces-redirect=true";
	}

	public void search() {
		bots = facade.searchBot(filter);
	}

	public String save() {
		try {
			Bot botOld = facade.validateBotTelegram(bot.getToken());
			bot.setName(botOld.getName());
			facade.saveBot(bot);
			msgAction.addMessage(MessageType.INFO, "Bot saved");
			auxAction.updateLastBotsFromCompanyUser();
		} catch (BusinessException e) {
			msgAction.addMessage(MessageType.ERROR, e.getMessage());
		}
		return PAGE_DETAIL;
	}

	public String edit() {
		this.filter = new BotFilter();
		return PAGE_BOT_2;
	}

	public String deactivateBot() {
		try {
			facade.deactivateBot(bot);
			msgAction.addMessage(MessageType.INFO, "Bot deactivated");
		} catch (BusinessException e) {
			msgAction.addMessage(MessageType.ERROR, e.getMessage());
		}
		return PAGE_DETAIL;
	}

	public String reactivateBot() {
		try {
			facade.reactivateBot(bot);
			msgAction.addMessage(MessageType.INFO, "Bot is active now");
		} catch (BusinessException e) {
			msgAction.addMessage(MessageType.ERROR, e.getMessage());
		}
		return PAGE_DETAIL;
	}

	public String detail(Bot bot) {
		this.bot = bot;
		commandAction.setBot(bot);
		commandAction.search();
		mountRawCommandsFromBot();
		return PAGE_DETAIL;
	}

	private void mountRawCommandsFromBot() {
		List<Command> commands = commandAction.getCommands();
		StringBuilder str = new StringBuilder();
		for (Command comm : commands) {
			str.append(comm.getKey());
			if (StringUtils.isNotBlank(comm.getShortDescription())) {
				str.append(" - ");
				str.append(comm.getShortDescription());
			} else {
				str.append(" - No description");
			}
			str.append("\r\n");
		}
		if (StringUtils.isNotBlank(bot.getCancelKey())) {
			str.append(bot.getCancelKey().replaceAll("/", ""));
			str.append(" - Cancel a command");
		}

		this.rawCommands = str.toString();
	}

	public String validateToken() {
		try {
			bot = facade.validateBotTelegram(token);
			return PAGE_BOT_2;
		} catch (BusinessException e) {
			bot.setName("Bot token is not valid");
			bot.setValid(false);
			msgAction.addMessage(MessageType.ERROR, e.getMessage());
			return PAGE_BOT_1;
		}
	}

	public void validateNewToken() {
		try {
			Bot validBot = facade.validateBotTelegram(bot.getToken());
			bot.setName(validBot.getName());
			bot.setToken(validBot.getToken());
			bot.setBotType(validBot.getBotType());
			bot.setUsername(validBot.getUsername());
			bot.setValid(validBot.getValid());
		} catch (BusinessException e) {
			bot.setName("Bot token is not valid");
			bot.setValid(false);
			msgAction.addMessage(MessageType.ERROR, e.getMessage());
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

	public String getRawCommands() {
		return rawCommands;
	}

	public void setRawCommands(String rawCommands) {
		this.rawCommands = rawCommands;
	}

}
