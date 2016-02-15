package com.javaleo.systems.botrise.ejb.session;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

@Named
@SessionScoped
public class Credentials implements Serializable {

	private static final long serialVersionUID = -4266891230589895335L;

	private String username;
	private String ip;
	private String host;

	public Boolean isLoggedIn() {
		return (StringUtils.isNotBlank(username));
	}

	public void login(String username) {
		this.username = username;
	}

	public void logout() {
		this.username = null;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

}