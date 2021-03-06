package org.javaleo.grandpa.web.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.grandpa.ejb.entities.blog.Page;
import org.javaleo.grandpa.ejb.entities.bot.Bot;
import org.javaleo.grandpa.ejb.entities.bot.Script;
import org.javaleo.grandpa.ejb.enums.AnswerType;
import org.javaleo.grandpa.ejb.enums.ScriptType;
import org.javaleo.grandpa.ejb.facades.IGrandPaFacade;
import org.javaleo.libs.botgram.enums.ParseMode;
import org.javaleo.libs.jee.core.security.IJavaleoAuthenticator;

@Named
@ConversationScoped
public class AuxAction extends AbstractConversationAction implements Serializable {

	@Inject
	private Conversation conversation;

	@Inject
	private IJavaleoAuthenticator authenticator;

	@Inject
	private IGrandPaFacade facade;

	private static final long serialVersionUID = 1L;

	private List<Bot> bots;
	private List<Script> lastGenericScripts;
	private List<Script> companyGenericScripts;
	private List<Script> lastCommandScriptsEdited;
	private List<Page> lastPagesEdited;

	@PostConstruct
	public void init() {
		if (authenticator.isLoggedIn()) {
			if (conversation.isTransient()) {
				conversation.begin();
			}
			updateLastBotsFromCompanyUser();
			updateLastGenericScripts();
			updateCompanyGenericScripts();
			updateLastContentPages();
			updateLastCommandScriptEditedByUser();
		}
	}

	@Override
	public Conversation getConversation() {
		return conversation;
	}

	public void updateLastBotsFromCompanyUser() {
		bots = facade.listLastBotsFromCompanyUser();
	}

	public void updateLastGenericScripts() {
		lastGenericScripts = facade.listLastGenericScriptsFromUser();
	}

	public void updateCompanyGenericScripts() {
		companyGenericScripts = facade.listAllGenericScriptsFromCompany();
	}

	public void updateLastCommandScriptEditedByUser() {
		lastCommandScriptsEdited = facade.listLastCommandScriptsEditedByUser();
	}

	public void updateLastContentPages() {
		lastPagesEdited = facade.listLastPagesEdited();
	}

	public List<ScriptType> getScriptTypeOpt() {
		return new ArrayList<ScriptType>(Arrays.asList(ScriptType.values()));
	}

	public List<ParseMode> getParseModeOpt() {
		return new ArrayList<ParseMode>(Arrays.asList(ParseMode.values()));
	}

	public List<AnswerType> getAnswerTypeOpt() {
		return new ArrayList<AnswerType>(Arrays.asList(AnswerType.values()));
	}

	public List<Bot> getBots() {
		return bots;
	}

	public void setBots(List<Bot> bots) {
		this.bots = bots;
	}

	public List<Script> getLastGenericScripts() {
		return lastGenericScripts;
	}

	public void setLastGenericScripts(List<Script> lastGenericScripts) {
		this.lastGenericScripts = lastGenericScripts;
	}

	public List<Script> getCompanyGenericScripts() {
		return companyGenericScripts;
	}

	public void setCompanyGenericScripts(List<Script> companyGenericScripts) {
		this.companyGenericScripts = companyGenericScripts;
	}

	public List<Script> getLastCommandScriptsEdited() {
		return lastCommandScriptsEdited;
	}

	public void setLastCommandScriptsEdited(List<Script> lastCommandScriptsEdited) {
		this.lastCommandScriptsEdited = lastCommandScriptsEdited;
	}

	public List<Page> getLastPagesEdited() {
		return lastPagesEdited;
	}

	public void setLastPagesEdited(List<Page> lastPagesEdited) {
		this.lastPagesEdited = lastPagesEdited;
	}

}
