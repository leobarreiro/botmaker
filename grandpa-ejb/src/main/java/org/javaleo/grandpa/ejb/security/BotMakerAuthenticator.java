package org.javaleo.grandpa.ejb.security;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.grandpa.ejb.business.IUserBusiness;
import org.javaleo.grandpa.ejb.entities.User;
import org.javaleo.libs.jee.core.exceptions.JavaleoException;
import org.javaleo.libs.jee.core.security.IJavaleoAuthenticator;

@Named
@Stateless
public class BotMakerAuthenticator implements IJavaleoAuthenticator {

	private static final long serialVersionUID = 1L;

	@Inject
	private BotMakerCredentials credentials;

	@Inject
	private IUserBusiness userBusiness;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.javaleo.libs.jee.core.security.IJavaleoAuthenticator#authenticate(java.lang.String, java.lang.String)
	 */
	@Override
	public void authenticate(String username, String password) throws JavaleoException {
		User user = userBusiness.findUserByUsernameAndPassphrase(username, password);
		if (user == null) {
			throw new JavaleoException("user.not.found");
		}
		credentials.setUser(user);
		if (user.getCompany() != null) {
			credentials.setCompany(user.getCompany());
		}
		credentials.setUuid(username);
		credentials.setName(user.getName());
	}

	@Override
	public void logoff() throws JavaleoException {
		credentials.clear();
	}

	@Override
	public boolean isLoggedIn() {
		return (credentials.getUser() != null);
	}

}
