package com.javaleo.systems.botrise.web.action;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.libs.jee.core.web.actions.AbstractCrudAction;

import com.javaleo.systems.botrise.ejb.entities.Bot;
import com.javaleo.systems.botrise.ejb.entities.Command;
import com.javaleo.systems.botrise.ejb.exceptions.BotRiseException;
import com.javaleo.systems.botrise.ejb.facades.IBotRiseFacade;
import com.javaleo.systems.botrise.web.action.MsgAction.MessageType;

@Named
@ConversationScoped
public class CommandAction extends AbstractCrudAction<Command> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Conversation conversation;

	@Inject
	private IBotRiseFacade facade;

	@Inject
	private MsgAction msgAction;

	private CRUD crudOp;
	private Command command;
	private List<Command> commands;
	private Bot bot;

	@Override
	public Conversation getConversation() {
		return conversation;
	}

	@Override
	public String loadNewScreen() {
		startOrResumeConversation();
		command = new Command();
		command.setBot(bot);
		return "/pages/command/command.jsf?faces-redirect=true";
	}

	@Override
	public String loadSearchScreen() {
		startOrResumeConversation();
		search();
		return "/pages/bot/bot-details.jsf?faces-redirect=true";
	}

	@Override
	public void search() {
		commands = facade.listCommandsByBot(bot);
	}

	@Override
	public String loadEditScreen() {
		return "/pages/command/command.jsf?faces-redirect=true";
	}

	public String edit(Command command) {
		this.command = command;
		return "/pages/command/command.jsf?faces-redirect=true";
	}

	@Override
	public String loadDetailScreen(Command pojo) {
		return "/pages/command/command-details.jsf?faces-redirect=true";
	}

	@Override
	public String save() {
		try {
			facade.saveCommand(command);
			msgAction.addMessage(MessageType.INFO, "Comando salvo corretamente");
		} catch (BotRiseException e) {
			msgAction.addMessage(MessageType.ERROR, e.getMessage());
		}
		return "/pages/bot/bot-detail.jsf?faces-redirect=true";
	}

	@Override
	public CRUD getCrudOp() {
		return crudOp;
	}

	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}

	public List<Command> getCommands() {
		return commands;
	}

	public void setCommands(List<Command> commands) {
		this.commands = commands;
	}

	public Bot getBot() {
		return bot;
	}

	public void setBot(Bot bot) {
		this.bot = bot;
	}

}
