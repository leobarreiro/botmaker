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
	private Boolean editing;

	public void startEditing() {
		this.editing = Boolean.TRUE;
	}

	public void stopEditing() {
		this.editing = Boolean.FALSE;
	}

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

	public Boolean getEditing() {
		return editing;
	}

	public void setEditing(Boolean editing) {
		this.editing = editing;
	}

	@Override
	public void clear() {
		super.clear();
		this.company = null;
		this.user = null;
		this.editing = null;
	}

}
