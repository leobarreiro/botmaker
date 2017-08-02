package org.javaleo.grandpa.web.action;

import javax.enterprise.context.Conversation;

import org.javaleo.libs.jee.core.model.IEntityBasic;

public abstract class AbstractCrudSinglePage<T extends IEntityBasic> {

	public static final long CONVERSATION_EXPIRATION = 760000l;

	public abstract Conversation getConversation();

	public abstract String loadPage();

	public abstract void startNew();

	public abstract void searchAll();

	public abstract void searchOnlyActives();

	public abstract void viewDetails(T pojo);

	public abstract void startEdit(T pojo);

	public abstract void deactivate(T pojo);

	public abstract void save();

	public void startConversation() {
		if (getConversation().isTransient()) {
			getConversation().begin();
			getConversation().setTimeout(CONVERSATION_EXPIRATION);
		}
	}

	public void endConversation() {
		if (!getConversation().isTransient()) {
			getConversation().end();
		}
	}

}
