package com.javaleo.systems.botmaker.ejb.security;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.libs.jee.core.exceptions.JavaleoException;
import org.javaleo.libs.jee.core.security.Credentials;
import org.javaleo.libs.jee.core.security.IJavaleoAuthenticator;

import com.javaleo.systems.botmaker.ejb.business.IUserBusiness;
import com.javaleo.systems.botmaker.ejb.entities.User;

@Named
@Stateless
public class BotRiseAuthenticator implements IJavaleoAuthenticator {

	@Inject
	private Credentials credentials;

	@Inject
	private IUserBusiness userBusiness;

	@Override
	public void authenticate(String username, String password) throws JavaleoException {
		User user = userBusiness.findUserByUsernameAndPassphrase(username, password);
		if (user == null) {
			throw new JavaleoException("Usuario não encontrado.");
		}
		credentials.setUsername(username);
		credentials.setName(user.getName());
		credentials.setEmail(user.getEmail());
	}

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
