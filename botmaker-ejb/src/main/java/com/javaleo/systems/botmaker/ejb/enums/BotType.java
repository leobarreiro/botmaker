package com.javaleo.systems.botmaker.ejb.enums;

public enum BotType {

	TELEGRAM("Telegram"), HYPERCHAT("Hyperchat"), IRC("IRC");

	private BotType(String name) {
		this.name = name;
	}

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
