package org.javaleo.grandpa.web.action;

import java.io.Serializable;
import java.util.Map;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.javaleo.grandpa.ejb.entities.Company;
import org.javaleo.grandpa.ejb.entities.Token;
import org.javaleo.grandpa.ejb.entities.User;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.facades.IBotMakerFacade;
import org.javaleo.grandpa.web.action.MsgAction.MessageType;
import org.javaleo.libs.jee.core.exceptions.JavaleoException;
import org.javaleo.libs.jee.core.security.IJavaleoAuthenticator;
import org.slf4j.Logger;

@Named
@ConversationScoped
public class UserAction extends AbstractConversationAction implements Serializable {

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

	@Inject
	private Logger LOG;

	private String username;
	private String plainPassword;
	private String passwordReview;
	private String firstName;
	private String emailRecovery;
	private String emailRecoveryReview;

	private String uuidToken;
	private Token token;
	private Boolean validToken;

	private Company company;
	private User user;

	public String login() {
		startNewConversation();
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
		startNewConversation();
		company = new Company();
		user = new User();
		return "/new-account.jsf?faces-redirect=true";
	}

	public String saveNewUser() {
		startOrResumeConversation();
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
		startNewConversation();
		emailRecovery = null;
		emailRecoveryReview = null;
		return "/password-recovery.jsf?faces-redirect=true";
	}

	public String recoverPasswordFromUser() {
		startOrResumeConversation();
		try {
			facade.sendMessageRecoveryLoginToUser(emailRecovery, emailRecoveryReview);
			msgAction.addInfoMessage("A message was sent to your e-mail. Please read it and follow the instructions.");
			return "/index.jsf?faces-redirect=true";
		} catch (BusinessException e) {
			msgAction.addErrorMessage(e.getMessage());
			return "/password-recovery.jsf?faces-redirect=true";
		}
	}

	public void startResetPassword() {
		startNewConversation();
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String uniqueToken = params.get("uuidToken");

		try {
			token = facade.getTokenByUUID(uniqueToken);
			validToken = (token != null);
		} catch (BusinessException e) {
			LOG.error(e.getMessage());
		}
		uuidToken = uniqueToken;
		plainPassword = null;
		passwordReview = null;
	}

	public String confirmResetPassword() {
		startOrResumeConversation();
		try {
			facade.saveUser(token.getUser(), plainPassword, passwordReview);
			msgAction.addInfoMessage("Your new password was been saved!");
			return "/index.jsf?faces-redirect=true";
		} catch (BusinessException e) {
			msgAction.addErrorMessage(e.getMessage());
			return "/reset-password.jsf?faces-redirect=true";
		}
	}

	public String goToLogin() {
		startOrResumeConversation();
		return "/index.jsf?faces-redirect=true";
	}

	@Override
	public Conversation getConversation() {
		return conversation;
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEmailRecovery() {
		return emailRecovery;
	}

	public void setEmailRecovery(String emailRecovery) {
		this.emailRecovery = emailRecovery;
	}

	public String getEmailRecoveryReview() {
		return emailRecoveryReview;
	}

	public void setEmailRecoveryReview(String emailRecoveryReview) {
		this.emailRecoveryReview = emailRecoveryReview;
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

	public String getUuidToken() {
		return uuidToken;
	}

	public void setUuidToken(String uuidToken) {
		this.uuidToken = uuidToken;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public Boolean getValidToken() {
		return validToken;
	}

	public void setValidToken(Boolean validToken) {
		this.validToken = validToken;
	}

}