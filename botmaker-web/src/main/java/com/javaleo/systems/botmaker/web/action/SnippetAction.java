package com.javaleo.systems.botmaker.web.action;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.libs.jee.core.web.actions.AbstractCrudAction;

import com.javaleo.systems.botmaker.ejb.entities.Snippet;
import com.javaleo.systems.botmaker.ejb.enums.ScriptType;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.facades.IBotMakerFacade;
import com.javaleo.systems.botmaker.ejb.filters.SnippetFilter;
import com.javaleo.systems.botmaker.web.action.MsgAction.MessageType;

@Named
@ConversationScoped
public class SnippetAction extends AbstractCrudAction<Snippet> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Conversation conversation;

	@Inject
	private MsgAction msgAction;

	private CRUD crudOp;

	private SnippetFilter filter;
	private Snippet snippet;
	private List<Snippet> snippets;
	private List<ScriptType> scriptTypeOptions;

	@Inject
	private IBotMakerFacade facade;

	public String startNew() {
		startNewConversation();
		loadOptions();
		snippet = new Snippet();
		snippet.setScriptType(ScriptType.REGEXP);
		return "/pages/snippets/snippet.jsf?faces-redirect=true";
	}

	public String save() {
		try {
			facade.saveSnippet(snippet);
			msgAction.addMessage(MessageType.INFO, "Snippet saved.");
		} catch (BusinessException e) {
			msgAction.addMessage(MessageType.ERROR, e.getMessage());
		}
		return list();
	}

	public String edit() {
		startOrResumeConversation();
		loadOptions();
		return "/pages/snippets/snippet.jsf?faces-redirect=true";
	}

	public String detail(Snippet snippet) {
		startOrResumeConversation();
		this.snippet = snippet;
		return "/pages/snippets/snippet-detail.jsf?faces-redirect=true";
	}

	public String cancel() {
		return list();
	}

	public String list() {
		startOrResumeConversation();
		loadOptions();
		snippets = facade.searchSnippetByFilter(filter);
		return "/pages/snippets/snippet-search.jsf?faces-redirect=true";
	}

	private void loadOptions() {
		filter = new SnippetFilter();
		this.scriptTypeOptions = Arrays.asList(ScriptType.values());
	}

	@Override
	public Conversation getConversation() {
		return conversation;
	}

	@Override
	public CRUD getCrudOp() {
		return crudOp;
	}

	public void setCrudOp(CRUD crudOp) {
		this.crudOp = crudOp;
	}

	public Snippet getSnippet() {
		return snippet;
	}

	public void setSnippet(Snippet snippet) {
		this.snippet = snippet;
	}

	public List<Snippet> getSnippets() {
		return snippets;
	}

	public void setSnippets(List<Snippet> snippets) {
		this.snippets = snippets;
	}

	public List<ScriptType> getScriptTypeOptions() {
		return scriptTypeOptions;
	}

	public void setScriptTypeOptions(List<ScriptType> scriptTypeOptions) {
		this.scriptTypeOptions = scriptTypeOptions;
	}

	public SnippetFilter getFilter() {
		return filter;
	}

	public void setFilter(SnippetFilter filter) {
		this.filter = filter;
	}

}
