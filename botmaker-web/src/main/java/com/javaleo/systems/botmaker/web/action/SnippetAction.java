package com.javaleo.systems.botmaker.web.action;

import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.libs.jee.core.web.actions.AbstractCrudAction;

import com.javaleo.systems.botmaker.ejb.entities.SnippetCode;
import com.javaleo.systems.botmaker.ejb.facades.IBotMakerFacade;
import com.javaleo.systems.botmaker.ejb.filters.SnippetCodeFilter;

@Named
@ConversationScoped
public class SnippetAction extends AbstractCrudAction<SnippetCode> {

	private static final long serialVersionUID = 1L;

	private Conversation conversation;
	private CRUD crudOp;

	private SnippetCodeFilter filter;
	private SnippetCode snippet;
	private List<SnippetCode> snippets;

	@Inject
	private IBotMakerFacade facade;

	@Override
	public Conversation getConversation() {
		return conversation;
	}

	public String startNew() {
		startNewConversation();
		filter = new SnippetCodeFilter();
		snippet = new SnippetCode();
		return "/pages/snippets/snippet.jsf?faces-redirect=true";
	}

	public String save() {
		facade.saveSnippetCode(snippet);
		return "/pages/snippets/snippet-search.jsf?faces-redirect=true";
	}

	public String list() {
		filter = new SnippetCodeFilter();
		snippets = facade.searchSnippetCodeByFilter(filter);
		return "/pages/snippets/snippet-search.jsf?faces-redirect=true";
	}

	@Override
	public CRUD getCrudOp() {
		return crudOp;
	}

	public void setCrudOp(CRUD crudOp) {
		this.crudOp = crudOp;
	}

	public SnippetCode getSnippet() {
		return snippet;
	}

	public void setSnippet(SnippetCode snippet) {
		this.snippet = snippet;
	}

	public List<SnippetCode> getSnippets() {
		return snippets;
	}

	public void setSnippets(List<SnippetCode> snippets) {
		this.snippets = snippets;
	}

}
