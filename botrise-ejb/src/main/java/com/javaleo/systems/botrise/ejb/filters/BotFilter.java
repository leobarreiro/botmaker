package com.javaleo.systems.botrise.ejb.filters;

import com.javaleo.systems.botrise.ejb.enums.BotType;

public class BotFilter {

	private String name;
	private BotType botType;
	private String token;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BotType getBotType() {
		return botType;
	}

	public void setBotType(BotType botType) {
		this.botType = botType;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
