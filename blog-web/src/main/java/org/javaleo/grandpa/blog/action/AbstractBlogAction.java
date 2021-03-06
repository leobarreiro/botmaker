package org.javaleo.grandpa.blog.action;

import javax.enterprise.context.Conversation;

public abstract class AbstractBlogAction {

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
