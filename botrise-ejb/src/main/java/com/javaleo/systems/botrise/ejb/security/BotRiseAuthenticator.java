package com.javaleo.systems.botrise.ejb.security;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.libs.jee.core.exceptions.JavaleoException;
import org.javaleo.libs.jee.core.security.Credentials;
import org.javaleo.libs.jee.core.security.IJavaleoAuthenticator;

@Named
@Stateless
public class BotRiseAuthenticator implements IJavaleoAuthenticator {

	@Inject
	private Credentials credentials;

	@Override
	public void authenticate(String username, String password) throws JavaleoException {
		// TODO Auto-generated method stub
		credentials.setUsername(username);
	}

	@Override
	public void logoff() throws JavaleoException {
		// TODO Auto-generated method stub
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
