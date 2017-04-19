package org.javaleo.grandpa.web.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.javaleo.grandpa.ejb.entities.Bot;
import org.javaleo.grandpa.ejb.entities.Command;
import org.javaleo.grandpa.ejb.entities.Question;
import org.javaleo.grandpa.ejb.entities.Script;
import org.javaleo.grandpa.ejb.enums.ScriptType;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.facades.IBotMakerFacade;
import org.javaleo.grandpa.ejb.pojos.Dialog;
import org.javaleo.grandpa.ejb.pojos.DialogContextVar;
import org.javaleo.grandpa.ejb.security.BotMakerCredentials;
import org.javaleo.grandpa.web.action.MsgAction.MessageType;
import org.javaleo.libs.botgram.enums.ParseMode;
import org.javaleo.libs.jee.core.web.actions.AbstractCrudAction;
import org.python.icu.util.Calendar;

@Named
@ConversationScoped
public class CommandAction extends AbstractCrudAction implements Serializable {

	private static final String PAGE_DETAIL = "/pages/command/command-detail.bot?faces-redirect=true";

	private static final String PAGE_EDIT = "/pages/command/command.bot?faces-redirect=true";

	private static final String PAGE_BOT_DETAIL = "/pages/bot/bot-detail.bot?faces-redirect=true";

	private static final long serialVersionUID = 1L;

	@Inject
	private Conversation conversation;

	@Inject
	private IBotMakerFacade facade;

	@Inject
	private UserPreferenceAction userPreferenceAction;

	@Inject
	private BotMakerCredentials credentials;

	@Inject
	private MsgAction msgAction;

	private Command command;
	private List<Command> commands;
	private List<Question> questions;
	private Bot bot;
	private List<DialogContextVar> contextVars;
	private String debugContent;
	private boolean debugging;

	@Override
	public Conversation getConversation() {
		return conversation;
	}

	public String startNew() {
		startOrResumeConversation();
		command = new Command();
		command.setBot(bot);
		fillPostScriptToCommand();
		questions = new ArrayList<Question>();
		loadQuestionsAndContextVars();
		userPreferenceAction.loadPreferences();
		return PAGE_EDIT;
	}

	public String list() {
		startOrResumeConversation();
		search();
		return PAGE_BOT_DETAIL;
	}

	public void search() {
		commands = facade.listCommandsByBot(bot);
	}

	public String edit(Command pojo) {
		this.command = facade.getCommandById(pojo.getId());
		fillPostScriptToCommand();
		loadQuestionsAndContextVars();
		userPreferenceAction.loadPreferences();
		debugMode(false);
		return PAGE_EDIT;
	}

	private void fillPostScriptToCommand() {
		if (command.getPostScript() == null) {
			Script script = new Script();
			script.setAuthor(credentials.getUser());
			script.setCreated(Calendar.getInstance().getTime());
			script.setEnabled(true);
			script.setCommand(command);
			script.setScriptType(ScriptType.GROOVY);
			script.setParseMode(ParseMode.MARKDOWN);
			command.setPostScript(script);
		}
	}

	public String detail(Command pojo) {
		startOrResumeConversation();
		// TODO: recuperar command e questions da base novamente, para carregar dados atualizados.
		this.command = facade.getCommandById(pojo.getId());
		fillPostScriptToCommand();
		loadQuestionsAndContextVars();
		userPreferenceAction.loadPreferences();
		debugMode(false);
		return PAGE_DETAIL;
	}

	public String save() {
		try {
			facade.saveCommand(command);
			msgAction.addMessage(MessageType.INFO, "Command saved");
			debugMode(false);
			return detail(command);
		} catch (BusinessException e) {
			msgAction.addMessage(MessageType.ERROR, e.getMessage());
			return PAGE_EDIT;
		}
	}

	public void debugMode(boolean mode) {
		this.debugging = mode;
	}

	public void testScript() {
		try {
			Map<String, Object> mapVars = new LinkedHashMap<String, Object>();
			for (DialogContextVar ctx : contextVars) {
				if (StringUtils.isNotEmpty(ctx.getValue())) {
					mapVars.put(ctx.getName(), ctx.getValue());
				}
			}
			debugMode(true);
			Dialog dialog = new Dialog();
			dialog.setBotId(command.getBot().getId());
			dialog.setId(0);
			dialog.setContextVars(mapVars);
			debugContent = facade.debugScript(dialog, command.getPostScript());
			boolean valid = facade.isValidScript(command.getPostScript());
			command.getPostScript().setValid(valid);
		} catch (Exception e) {
			debugContent = e.getMessage();
			command.getPostScript().setValid(false);
		}
	}

	public String dropCommand() {
		facade.dropCommand(command);
		search();
		msgAction.addInfoMessage("Command droped.");
		return PAGE_BOT_DETAIL;
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

	public void enablePostScript() {
		fillPostScriptToCommand();
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

	public boolean isDebugging() {
		return debugging;
	}

	public void setDebugging(boolean debugging) {
		this.debugging = debugging;
	}

}
