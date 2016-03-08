package com.javaleo.systems.botrise.web.action;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.libs.jee.core.exceptions.JavaleoException;
import org.javaleo.libs.jee.core.security.IJavaleoAuthenticator;

import com.javaleo.systems.botrise.web.action.MsgAction.MessageType;

@Named
@ConversationScoped
public class UserAction implements Serializable {

	private static final long serialVersionUID = 8467442454444489390L;

	@Inject
	private IJavaleoAuthenticator authenticator;

	@Inject
	private BotAction botAction;

	@Inject
	private MsgAction msgAction;

	private String username;
	private String plainPassword;

	public String login() {
		try {
			authenticator.authenticate(username, plainPassword);
			return botAction.loadSearchScreen();
		} catch (JavaleoException e) {
			msgAction.addMessage(MessageType.ERROR, e.getMessage());
			return "/index.jsf?faces-redirect=true";
		}
	}

	public String logoff() {
		try {
			authenticator.logoff();
		} catch (JavaleoException e) {
			msgAction.addMessage(MessageType.ERROR, e.getMessage());
		}
		return "/index.jsf?faces-redirect=true";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPlainPassword() {
		return plainPassword;
	}

	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
	}

}
