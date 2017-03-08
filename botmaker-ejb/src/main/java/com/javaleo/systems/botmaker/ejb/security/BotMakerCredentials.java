package com.javaleo.systems.botmaker.ejb.security;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.javaleo.libs.jee.core.security.IJavaLeoCredentials;
import org.javaleo.libs.jee.core.security.JavaLeoCredentials;

import com.javaleo.systems.botmaker.ejb.entities.Company;
import com.javaleo.systems.botmaker.ejb.entities.User;

@Named
@SessionScoped
public class BotMakerCredentials extends JavaLeoCredentials implements IJavaLeoCredentials {

	private static final long serialVersionUID = 1L;

	private User user;
	private Company company;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Override
	public void clear() {
		super.clear();
		this.company = null;
		this.user = null;
	}

}
