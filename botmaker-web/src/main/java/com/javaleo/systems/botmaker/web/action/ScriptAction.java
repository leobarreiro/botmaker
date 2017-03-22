package com.javaleo.systems.botmaker.web.action;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.javaleo.libs.botgram.enums.ParseMode;
import org.python.icu.util.Calendar;

import com.javaleo.systems.botmaker.ejb.entities.Bot;
import com.javaleo.systems.botmaker.ejb.entities.Command;
import com.javaleo.systems.botmaker.ejb.entities.Question;
import com.javaleo.systems.botmaker.ejb.entities.Script;
import com.javaleo.systems.botmaker.ejb.entities.Validator;
import com.javaleo.systems.botmaker.ejb.enums.ScriptType;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.facades.IBotMakerFacade;
import com.javaleo.systems.botmaker.ejb.pojos.Dialog;
import com.javaleo.systems.botmaker.ejb.pojos.DialogContextVar;
import com.javaleo.systems.botmaker.ejb.security.BotMakerCredentials;

@Named
@ConversationScoped
public class ScriptAction extends AbstractConversationAction implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IBotMakerFacade facade;

	@Inject
	private Conversation conversation;

	@Inject
	private BotMakerCredentials credentials;

	@Inject
	private CommandAction commandAction;

	@Inject
	private ValidatorAction validatorAction;

	@Inject
	private AuxAction auxAction;

	@Inject
	private MsgAction msgAction;

	private Bot bot;
	private Script script;
	private Script viewScript;
	private List<Script> scripts;
	private List<DialogContextVar> contextVars;
	private String debugContent;
	private Boolean editing = Boolean.FALSE;

	public String startEditScript(Bot botEdition, Command command, Script scriptEdition) {
		startOrResumeConversation();
		editing = Boolean.TRUE;
		bot = botEdition;
		if (scriptEdition != null && scriptEdition.getId() != null) {
			script = facade.getScriptToEdition(scriptEdition.getId());
		} else {
			script = new Script();
			script.setAuthor(credentials.getUser());
			script.setCreated(Calendar.getInstance().getTime());
			script.setEnabled(true);
			script.setCommand(command);
			script.setScriptType(ScriptType.GROOVY);
			script.setParseMode(ParseMode.MARKDOWN);
			command.setPostScript(script);
		}
		script.setGeneric(false);
		loadContextVars();
		listGenericScripts();
		return "/pages/scripts/editor-full.jsf?faces-redirect=true";
	}

	public String startNewGeneric() {
		startNewConversation();
		editing = Boolean.TRUE;
		bot = null;
		script = new Script();
		script.setGeneric(true);
		loadContextVars();
		return "/pages/scripts/editor-full.jsf?faces-redirect=true";
	}

	public String startEditGeneric(Script script) {
		startOrResumeConversation();
		editing = Boolean.TRUE;
		this.bot = null;
		this.script = facade.getScriptToEdition(script.getId());
		loadContextVars();
		return "/pages/scripts/editor-full.jsf?faces-redirect=true";
	}

	public String startEditScriptValidator(Validator validator) {
		startOrResumeConversation();
		editing = Boolean.TRUE;
		this.bot = null;
		if (validator.getScript() != null) {
			script = facade.getScriptToEdition(validator.getScript().getId());
		} else {
			script = new Script();
			script.setGeneric(false);
			script.setValidator(validator);
		}
		loadContextVars();
		listGenericScripts();
		return "/pages/scripts/editor-full.jsf?faces-redirect=true";
	}

	public void startViewScript(Script scriptView) {
		startOrResumeConversation();
		viewScript = (scriptView != null && scriptView.getId() != null) ? facade.getScriptToEdition(scriptView.getId()) : scriptView;
	}

	public void stopEdit() {
		this.editing = Boolean.FALSE;
	}

	private void loadContextVars() {
		this.contextVars = facade.getListDialogContextVars();
		if (script.getCommand() != null && script.getCommand().getId() != null) {
			List<Question> questions = facade.listQuestionsFromCommand(script
					.getCommand());
			for (Question q : questions) {
				this.contextVars.add(new DialogContextVar(q.getVarName(), "", q
						.getInstruction()));
			}
		}
		// TODO fazer logica para script de uma question (sem command)
	}

	public void testScript() {
		try {
			Map<String, Object> mapVars = new LinkedHashMap<String, Object>();
			for (DialogContextVar ctx : contextVars) {
				if (StringUtils.isNotEmpty(ctx.getValue())) {
					mapVars.put(ctx.getName(), ctx.getValue());
				}
			}
			Dialog dialog = new Dialog();
			dialog.setBotId((bot != null) ? bot.getId() : 0L);
			dialog.setId(0);
			dialog.setContextVars(mapVars);
			debugContent = facade.debugScript(dialog, script);
			boolean valid = facade.isValidScript(script);
			script.setValid(valid);
		} catch (Exception e) {
			debugContent = e.getMessage();
			script.setValid(false);
		}
	}

	public void clearOutput() {
		this.debugContent = "";
	}

	public String listGenericScripts() {
		if (this.script.getScriptType() == null) {
			this.script.setScriptType(ScriptType.GROOVY);
		}
		handleGenericScripts();
		return "/pages/scripts/list-generic.jsf?faces-redirect=true";
	}

	public String saveScript() {
		try {
			facade.saveScript(script);
			msgAction.addInfoMessage("Script saved!");
			auxAction.updateLastGenericScripts();
			auxAction.updateCompanyGenericScripts();
		} catch (BusinessException e) {
			msgAction.addErrorMessage(e.getMessage());
		}
		return "/pages/scripts/editor-full.jsf?faces-redirect=true";
	}

	public void handleGenericScripts() {
		scripts = facade.listGenericScriptsFromScriptType(this.script.getScriptType());
	}

	public void handleRefreshConversation() {
		startOrResumeConversation();
	}

	public String goBackCommand() {
		return commandAction.detail(script.getCommand());
	}

	public String goBackValidator() {
		return validatorAction.detail(script.getValidator());
	}

	@Override
	public Conversation getConversation() {
		return conversation;
	}

	public Bot getBot() {
		return bot;
	}

	public void setBot(Bot bot) {
		this.bot = bot;
	}

	public Script getScript() {
		return script;
	}

	public void setScript(Script script) {
		this.script = script;
	}

	public Script getViewScript() {
		return viewScript;
	}

	public void setViewScript(Script script) {
		this.viewScript = script;
	}

	public List<Script> getScripts() {
		return scripts;
	}

	public void setScripts(List<Script> scripts) {
		this.scripts = scripts;
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

	public Boolean getEditing() {
		return editing;
	}

	public void setEditing(Boolean editing) {
		this.editing = editing;
	}

}
