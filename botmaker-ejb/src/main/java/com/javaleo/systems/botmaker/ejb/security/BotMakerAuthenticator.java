package com.javaleo.systems.botmaker.ejb.security;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.libs.jee.core.exceptions.JavaleoException;
import org.javaleo.libs.jee.core.security.IJavaleoAuthenticator;

import com.javaleo.systems.botmaker.ejb.business.IUserBusiness;
import com.javaleo.systems.botmaker.ejb.entities.User;

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
			throw new JavaleoException("User not found.");
		}
		credentials.setUser(user);
		if (user.getCompany() != null) {
			credentials.setCompany(user.getCompany());
		}
		credentials.setUsername(username);
		credentials.setName(user.getName());
		credentials.setEmail(user.getEmail());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.javaleo.libs.jee.core.security.IJavaleoAuthenticator#logoff()
	 */
	@Override
	public void logoff() throws JavaleoException {
		credentials.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.javaleo.libs.jee.core.security.IJavaleoAuthenticator#isAuthenticated()
	 */
	@Override
	public boolean isAuthenticated() {
		return (credentials.getUsername() != null);
	}

}
