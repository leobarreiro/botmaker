package com.javaleo.systems.botmaker.web.action;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.javaleo.libs.jee.core.web.actions.AbstractCrudAction;

import com.javaleo.systems.botmaker.ejb.entities.Bot;
import com.javaleo.systems.botmaker.ejb.entities.Command;
import com.javaleo.systems.botmaker.ejb.entities.Question;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.facades.IBotMakerFacade;
import com.javaleo.systems.botmaker.ejb.pojos.DialogContextVar;
import com.javaleo.systems.botmaker.ejb.utils.GroovyScriptRunnerUtils;
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
	private GroovyScriptRunnerUtils groovyScriptRunner;

	@Inject
	private MsgAction msgAction;

	private CRUD crudOp;
	private Command command;
	private List<Command> commands;
	private List<Question> questions;
	private Bot bot;
	private List<DialogContextVar> contextVars;
	private String debugContent;

	@Override
	public Conversation getConversation() {
		return conversation;
	}

	public String startNew() {
		startOrResumeConversation();
		command = new Command();
		command.setBot(bot);
		loadQuestionsAndContextVars();
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
		userPreferenceAction.loadPreferences();
		loadQuestionsAndContextVars();
		return "/pages/command/command.jsf?faces-redirect=true";
	}

	public String detail(Command command) {
		startOrResumeConversation();
		this.command = command;
		loadQuestionsAndContextVars();
		userPreferenceAction.loadPreferences();
		return "/pages/command/command-detail.jsf?faces-redirect=true";
	}

	public String save() {
		try {
			facade.saveCommand(command);
			msgAction.addMessage(MessageType.INFO, "Command saved");
		} catch (BusinessException e) {
			msgAction.addMessage(MessageType.ERROR, e.getMessage());
		}
		return "/pages/command/command-detail.jsf?faces-redirect=true";
	}

	public void testScript() {
		try {
			Map<String, String> mapVars = new HashMap<String, String>();
			for (DialogContextVar ctx : contextVars) {
				if (StringUtils.isNotEmpty(ctx.getValue())) {
					mapVars.put(ctx.getName(), ctx.getValue());
				}
			}
			debugContent = (String) groovyScriptRunner.testScript(command.getPostProcessScript(), mapVars);
		} catch (BusinessException e) {
			msgAction.addMessage(MessageType.ERROR, e.getMessage());
		}
	}

	public String dropCommand() {
		facade.dropCommand(command);
		search();
		return "/pages/bot/bot-detail.jsf?faces-redirect=true";
	}

	public void loadQuestionsAndContextVars() {
		this.contextVars = facade.getListDialogContextVars();
		if (this.command.getId() != null) {
			questions = facade.listQuestionsFromCommand(command);
			for (Question q : questions) {
				this.contextVars.add(new DialogContextVar(q.getVarName(), "", q.getInstruction()));
			}
		}
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

	public List<DialogContextVar> getContextVars() {
		return contextVars;
	}

	public void setContextVars(List<DialogContextVar> contextVars) {
		this.contextVars = contextVars;
	}

	public String getDebugContent() {
		return debugContent;
	}

	public void setDebugContent(String debugContent) {
		this.debugContent = debugContent;
	}

}
