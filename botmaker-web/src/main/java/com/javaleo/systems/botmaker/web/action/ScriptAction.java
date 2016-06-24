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

import com.javaleo.systems.botmaker.ejb.entities.Bot;
import com.javaleo.systems.botmaker.ejb.entities.Question;
import com.javaleo.systems.botmaker.ejb.entities.Script;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.facades.IBotMakerFacade;
import com.javaleo.systems.botmaker.ejb.pojos.Dialog;
import com.javaleo.systems.botmaker.ejb.pojos.DialogContextVar;

@Named
@ConversationScoped
public class ScriptAction implements Serializable {

	private static final long CONVERSATION_EXPIRES = 760000l;

	private static final long serialVersionUID = 1L;

	@Inject
	private IBotMakerFacade facade;

	@Inject
	private Conversation conversation;

	@Inject
	private CommandAction commandAction;

	@Inject
	private AuxAction auxAction;

	@Inject
	private MsgAction msgAction;

	private Bot bot;
	private Script script;
	private List<Script> scripts;
	private List<DialogContextVar> contextVars;
	private String debugContent;

	public String startEditScript(Bot bot, Script script) {
		if (conversation.isTransient()) {
			conversation.begin();
		}
		conversation.setTimeout(CONVERSATION_EXPIRES);
		this.bot = bot;
		this.script = script;
		loadQuestionsAndContextVars();
		return "/pages/scripts/editor-full.jsf?faces-redirect=true";
	}

	public String startNewGeneric() {
		if (conversation.isTransient()) {
			conversation.begin();
		}
		conversation.setTimeout(CONVERSATION_EXPIRES);
		bot = null;
		script = new Script();
		script.setGeneric(true);
		loadQuestionsAndContextVars();
		return "/pages/scripts/editor-full.jsf?faces-redirect=true";
	}

	public String startEditGeneric(Script script) {
		if (conversation.isTransient()) {
			conversation.begin();
		}
		conversation.setTimeout(CONVERSATION_EXPIRES);
		this.bot = null;
		this.script = script;
		loadQuestionsAndContextVars();
		return "/pages/scripts/editor-full.jsf?faces-redirect=true";
	}

	private void loadQuestionsAndContextVars() {
		this.contextVars = facade.getListDialogContextVars();
		if (script.getCommand() != null && script.getCommand().getId() != null) {
			List<Question> questions = facade.listQuestionsFromCommand(script.getCommand());
			for (Question q : questions) {
				this.contextVars.add(new DialogContextVar(q.getVarName(), "", q.getInstruction()));
			}
		}
		// TODO fazer logica para script de uma question (sem command)
	}

	public void testScript() {
		try {
			Map<String, String> mapVars = new HashMap<String, String>();
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

	public String listGenericScripts() {
		scripts = facade.listGenericScripts();
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

	public String goBackCommand() {
		return commandAction.detail(script.getCommand());
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
