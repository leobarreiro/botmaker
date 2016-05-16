package com.javaleo.systems.botmaker.web.action;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.libs.jee.core.web.actions.AbstractCrudAction;

import com.javaleo.systems.botmaker.ejb.entities.Bot;
import com.javaleo.systems.botmaker.ejb.entities.Command;
import com.javaleo.systems.botmaker.ejb.entities.Question;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.facades.IBotMakerFacade;
import com.javaleo.systems.botmaker.web.action.MsgAction.MessageType;

@Named
@ConversationScoped
public class CommandAction extends AbstractCrudAction<Command> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Conversation conversation;

	@Inject
	private IBotMakerFacade facade;

	@Inject
	private UserPreferenceAction userPreferenceAction;

	@Inject
	private MsgAction msgAction;

	private CRUD crudOp;
	private Command command;
	private List<Command> commands;
	private List<Question> questions;
	private Bot bot;
	private Boolean readOnly;

	@Override
	public Conversation getConversation() {
		return conversation;
	}

	public String startNew() {
		startOrResumeConversation();
		this.readOnly = false;
		command = new Command();
		command.setBot(bot);
		userPreferenceAction.loadPreferences();
		return "/pages/command/command.jsf?faces-redirect=true";
	}

	public String list() {
		startOrResumeConversation();
		search();
		return "/pages/bot/bot-detail.jsf?faces-redirect=true";
	}

	public void search() {
		commands = facade.listCommandsByBot(bot);
	}

	public String edit(Command pojo) {
		this.command = pojo;
		this.readOnly = false;
		userPreferenceAction.loadPreferences();
		return "/pages/command/command.jsf?faces-redirect=true";
	}

	public String detail(Command command) {
		startOrResumeConversation();
		this.readOnly = true;
		this.command = command;
		questions = facade.listQuestionsFromCommand(command);
		userPreferenceAction.loadPreferences();
		return "/pages/command/command-detail.jsf?faces-redirect=true";
	}

	public String save() {
		try {
			facade.saveCommand(command);
			this.readOnly = true;
			msgAction.addMessage(MessageType.INFO, "Command saved");
		} catch (BusinessException e) {
			msgAction.addMessage(MessageType.ERROR, e.getMessage());
		}
		return "/pages/command/command-detail.jsf?faces-redirect=true";
	}

	public String dropCommand() {
		facade.dropCommand(command);
		search();
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

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public Boolean getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}

}
