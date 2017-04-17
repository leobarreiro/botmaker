package org.javaleo.grandpa.web.action;

import javax.enterprise.context.Conversation;

public abstract class AbstractConversationAction {

	public static long CONVERSATION_EXPIRES = 760000l;

	public void startNewConversation() {
		if (!getConversation().isTransient()) {
			getConversation().end();
		}
		getConversation().begin();
		getConversation().setTimeout(CONVERSATION_EXPIRES);
	}

	public void startOrResumeConversation() {
		if (getConversation().isTransient()) {
			getConversation().begin();
			getConversation().setTimeout(CONVERSATION_EXPIRES);
		}
	}

	public void endConversation() {
		if (!getConversation().isTransient()) {
			getConversation().end();
		}
	}

	public abstract Conversation getConversation();

}
