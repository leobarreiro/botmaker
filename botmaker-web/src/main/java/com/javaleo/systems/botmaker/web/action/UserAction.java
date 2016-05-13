package com.javaleo.systems.botmaker.web.action;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.libs.jee.core.exceptions.JavaleoException;
import org.javaleo.libs.jee.core.security.IJavaleoAuthenticator;

import com.javaleo.systems.botmaker.web.action.MsgAction.MessageType;

@Named
@RequestScoped
public class UserAction implements Serializable {

	private static final long serialVersionUID = 8467442454444489390L;

	@Inject
	private IJavaleoAuthenticator authenticator;

	@Inject
	private BotAction botAction;

	@Inject
	private AuxAction auxAction;

	@Inject
	private MsgAction msgAction;

	private String username;
	private String plainPassword;

	public String login() {
		try {
			authenticator.authenticate(username, plainPassword);
			auxAction.init();
			return botAction.list();
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
