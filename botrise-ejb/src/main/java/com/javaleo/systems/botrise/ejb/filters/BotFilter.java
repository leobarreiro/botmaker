package com.javaleo.systems.botrise.ejb.filters;

import java.util.Arrays;
import java.util.List;

import com.javaleo.systems.botrise.ejb.enums.BotType;

public class BotFilter {

	private String name;
	private BotType botType;
	private String token;

	private List<BotType> optBotTypes;

	public BotFilter() {
		super();
		optBotTypes = Arrays.asList(BotType.values());
	}

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

	public List<BotType> getOptBotTypes() {
		return optBotTypes;
	}

	public void setOptBotTypes(List<BotType> optBotTypes) {
		this.optBotTypes = optBotTypes;
	}

}
