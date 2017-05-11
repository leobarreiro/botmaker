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
import org.javaleo.grandpa.ejb.facades.IGrandPaFacade;
import org.javaleo.grandpa.web.action.MsgAction.MessageType;
import org.javaleo.libs.jee.core.exceptions.JavaleoException;
import org.javaleo.libs.jee.core.security.IJavaleoAuthenticator;
import org.slf4j.Logger;

@Named
@ConversationScoped
public class UserAction extends AbstractConversationAction implements Serializable {

	private static final String PAGE_RESET_PASSWD = "/reset-password.bot?faces-redirect=true";

	private static final String PAGE_PASSWD_RECOVERY = "/password-recovery.bot?faces-redirect=true";

	private static final String PAGE_NEW_ACCOUNT = "/new-account.bot?faces-redirect=true";

	private static final String PAGE_INIT = "/index.bot?faces-redirect=true";

	private static final long serialVersionUID = 8467442454444489390L;

	@Inject
	private IJavaleoAuthenticator authenticator;

	@Inject
	private IGrandPaFacade facade;

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
			return PAGE_INIT;
		}
	}

	public String logoff() {
		try {
			authenticator.logoff();
			msgAction.addMessage(MessageType.INFO, "Log out performed.");
		} catch (JavaleoException e) {
			msgAction.addMessage(MessageType.ERROR, e.getMessage());
		}
		return PAGE_INIT;
	}

	public String createAccount() {
		startNewConversation();
		company = new Company();
		user = new User();
		return PAGE_NEW_ACCOUNT;
	}

	public String confirmMailOwnership() {
		startOrResumeConversation();
		try {
			facade.validateUser(user, plainPassword, passwordReview);
			if (StringUtils.isBlank(company.getName())) {
				company.setName(user.getName());
			}
			company.setActive(false);
			facade.saveCompany(company);
			user.setActive(false);
			user.setCompany(company);
			facade.saveUser(user, plainPassword, passwordReview);
			facade.sendMessageToConfirmMailOwnership(user);
			msgAction.addInfoMessage("Congratulations! Just one more step. Please access your e-mail account and confirm his ownership.");
			username = user.getUsername();
			return PAGE_INIT;
		} catch (BusinessException e) {
			msgAction.addErrorMessage(e.getMessage());
			return PAGE_NEW_ACCOUNT;
		}
	}

	public void loadUserAfterMailConfirmation() {
		startOrResumeConversation();
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String uniqueToken = params.get("uuidToken");
		validToken = false;

		try {
			token = facade.getTokenByUUID(uniqueToken);
			validToken = (token != null);
		} catch (BusinessException e) {
			LOG.error(e.getMessage());
			return;
		}

		if (!validToken) {
			msgAction.addErrorMessage("The token used is not valid. Please try a new registration again.");
			return;
		}

		uuidToken = uniqueToken;
		user = token.getUser();
		user.setActive(true);
		company = user.getCompany();
		company.setActive(true);
	}

	public String saveNewUser() {
		startOrResumeConversation();
		try {
			user.setActive(true);
			company.setActive(true);
			facade.saveCompany(company);
			user.setCompany(company);
			facade.confirmUserRegistration(user);
			msgAction.addInfoMessage("Congratulations! You are registered in GrandPa. Please log in to start.");
			username = user.getUsername();
			return goToLogin();
		} catch (BusinessException e) {
			msgAction.addErrorMessage(e.getMessage());
			return PAGE_NEW_ACCOUNT;
		}
	}

	public String forgotMyPassword() {
		startNewConversation();
		emailRecovery = null;
		emailRecoveryReview = null;
		return PAGE_PASSWD_RECOVERY;
	}

	public String recoverPasswordFromUser() {
		startOrResumeConversation();
		try {
			facade.sendMessageRecoveryLoginToUser(emailRecovery, emailRecoveryReview);
			msgAction.addInfoMessage("A message was sent to your e-mail. Please read it and follow the instructions.");
			return PAGE_INIT;
		} catch (BusinessException e) {
			msgAction.addErrorMessage(e.getMessage());
			return PAGE_PASSWD_RECOVERY;
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
			msgAction.addInfoMessage("Your new password has been saved!");
			return PAGE_INIT;
		} catch (BusinessException e) {
			msgAction.addErrorMessage(e.getMessage());
			return PAGE_RESET_PASSWD;
		}
	}

	public String goToLogin() {
		startOrResumeConversation();
		return PAGE_INIT;
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
