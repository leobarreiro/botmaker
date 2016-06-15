package com.javaleo.systems.botmaker.web.action;

import java.io.Serializable;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.javaleo.libs.jee.core.exceptions.JavaleoException;
import org.javaleo.libs.jee.core.security.IJavaleoAuthenticator;

import com.javaleo.systems.botmaker.ejb.entities.Company;
import com.javaleo.systems.botmaker.ejb.entities.User;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.facades.IBotMakerFacade;
import com.javaleo.systems.botmaker.web.action.MsgAction.MessageType;

@Named
@ConversationScoped
public class UserAction implements Serializable {

	private static final long serialVersionUID = 8467442454444489390L;

	@Inject
	private IJavaleoAuthenticator authenticator;

	@Inject
	private IBotMakerFacade facade;

	@Inject
	private Conversation conversation;

	@Inject
	private BotAction botAction;

	@Inject
	private AuxAction auxAction;

	@Inject
	private MsgAction msgAction;

	private String username;
	private String plainPassword;
	private String passwordReview;

	private Company company;
	private User user;

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
			msgAction.addMessage(MessageType.INFO, "Log out performed.");
		} catch (JavaleoException e) {
			msgAction.addMessage(MessageType.ERROR, e.getMessage());
		}
		return "/index.jsf?faces-redirect=true";
	}

	public String createAccount() {
		conversation.begin();
		company = new Company();
		user = new User();
		return "/new-account.jsf?faces-redirect=true";
	}

	public String saveNewUser() {
		try {
			facade.validateUser(user, plainPassword, passwordReview);
			if (StringUtils.isBlank(company.getName())) {
				company.setName(user.getName());
			}
			company.setActive(true);
			facade.saveCompany(company);
			user.setCompany(company);
			facade.saveUser(user, plainPassword, passwordReview);
			msgAction.addInfoMessage("Congratulations! Now you are registered in BotMaker. Please log in to start.");
			username = user.getUsername();
			return goToLogin();
		} catch (BusinessException e) {
			msgAction.addErrorMessage(e.getMessage());
			return "/new-account.jsf?faces-redirect=true";
		}
	}

	public String forgotMyPassword() {
		return "/index.jsf?faces-redirect=true";
	}

	public String goToLogin() {
		if (!conversation.isTransient()) {
			conversation.end();
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

	public String getPasswordReview() {
		return passwordReview;
	}

	public void setPasswordReview(String passwordReview) {
		this.passwordReview = passwordReview;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
