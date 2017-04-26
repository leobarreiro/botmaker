package org.javaleo.grandpa.web.action;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.apache.commons.lang3.StringUtils;
import org.javaleo.grandpa.ejb.annotations.EditingNow;
import org.javaleo.grandpa.ejb.entities.Bot;
import org.javaleo.grandpa.ejb.entities.Command;
import org.javaleo.grandpa.ejb.entities.Question;
import org.javaleo.grandpa.ejb.entities.Script;
import org.javaleo.grandpa.ejb.entities.Validator;
import org.javaleo.grandpa.ejb.enums.ScriptType;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.facades.IGrandPaFacade;
import org.javaleo.grandpa.ejb.interceptors.EditingInterceptor;
import org.javaleo.grandpa.ejb.pojos.Dialog;
import org.javaleo.grandpa.ejb.pojos.DialogContextVar;
import org.javaleo.grandpa.ejb.security.BotMakerCredentials;
import org.javaleo.libs.botgram.enums.ParseMode;
import org.python.icu.util.Calendar;

@Named
@ConversationScoped
@Interceptors(EditingInterceptor.class)
public class ScriptAction extends AbstractConversationAction implements Serializable {

	private static final String PAGE_LIST_GENERIC = "/pages/scripts/list-generic.bot?faces-redirect=true";

	private static final String PAGE_EDIT_FULL = "/pages/scripts/editor-full.bot?faces-redirect=true";

	private static final long serialVersionUID = 1L;

	@Inject
	private IGrandPaFacade facade;

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
	private ScriptType scriptType;
	private Script viewScript;
	private List<Script> scripts;
	private List<DialogContextVar> contextVars;
	private String debugContent;

	@EditingNow(edit = true)
	public String startEditScript(Bot botEdition, Command command, Script scriptEdition) {
		startOrResumeConversation();
		bot = botEdition;
		if (scriptEdition != null && scriptEdition.getId() != null) {
			script = facade.getScriptToEdition(scriptEdition.getId());
			if (script.getGenericScript() != null) {
				viewScript = script.getGenericScript();
			}
		} else {
			script = new Script();
			script.setAuthor(credentials.getUser());
			script.setCreated(Calendar.getInstance().getTime());
			script.setEnabled(true);
			script.setCommand(command);
			script.setScriptType(ScriptType.GROOVY);
			script.setParseMode(ParseMode.MARKDOWN);
			command.setPostScript(script);
			viewScript = null;
		}
		script.setGeneric(false);
		scriptType = script.getScriptType();
		loadContextVars();
		handleGenericScripts();
		return PAGE_EDIT_FULL;
	}

	@EditingNow(edit = true)
	public String startNewGeneric() {
		startNewConversation();
		bot = null;
		script = new Script();
		script.setGeneric(true);
		scriptType = null;
		loadContextVars();
		return PAGE_EDIT_FULL;
	}

	@EditingNow(edit = true)
	public String startEditGeneric(Script script) {
		startOrResumeConversation();
		this.bot = null;
		this.script = facade.getScriptToEdition(script.getId());
		loadContextVars();
		return PAGE_EDIT_FULL;
	}

	@EditingNow(edit = true)
	public String startEditScriptValidator(Validator validator) {
		startOrResumeConversation();
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
		return PAGE_EDIT_FULL;
	}

	public void startViewScript(Script scriptView) {
		startOrResumeConversation();
		viewScript = (scriptView != null && scriptView.getId() != null) ? facade.getScriptToEdition(scriptView.getId()) : scriptView;
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
		this.scriptType = null;
		this.script = null;
		this.viewScript = null;
		handleGenericScripts();
		return PAGE_LIST_GENERIC;
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
		return PAGE_EDIT_FULL;
	}

	public void handleGenericScripts() {
		if (this.script != null) {
			this.scriptType = this.script.getScriptType();
		}
		scripts = facade.listGenericScriptsFromScriptType(this.scriptType);
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

	public ScriptType getScriptType() {
		return scriptType;
	}

	public void setScriptType(ScriptType scriptType) {
		this.scriptType = scriptType;
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

}
