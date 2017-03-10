package com.javaleo.systems.botmaker.web.action;

import javax.enterprise.context.Conversation;

public abstract class AbstractConversationAction {

	private Conversation conversation;

	public void startNewConversation() {
		if (!conversation.isTransient()) {
			conversation.begin();
		}
		conversation.begin();
	}

	public void startOrResumeConversation() {
		if (conversation.isTransient()) {
			conversation.begin();
		}
	}

}
