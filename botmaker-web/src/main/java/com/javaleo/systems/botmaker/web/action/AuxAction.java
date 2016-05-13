package com.javaleo.systems.botmaker.web.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.libs.botgram.enums.ParseMode;

import com.javaleo.systems.botmaker.ejb.entities.Bot;
import com.javaleo.systems.botmaker.ejb.enums.AnswerType;
import com.javaleo.systems.botmaker.ejb.enums.ScriptType;
import com.javaleo.systems.botmaker.ejb.facades.IBotMakerFacade;
import com.javaleo.systems.botmaker.ejb.security.BotMakerCredentials;

@Named
@ConversationScoped
public class AuxAction implements Serializable {

	@Inject
	private BotMakerCredentials credentials;

	@Inject
	private IBotMakerFacade facade;

	private static final long serialVersionUID = 1L;

	private List<Bot> bots;

	public void init() {
		bots = facade.listLastBotsFromCompanyUser();
	}

	public List<ScriptType> getScriptTypeOpt() {
		return new ArrayList<ScriptType>(Arrays.asList(ScriptType.values()));
	}

	public List<ParseMode> getParseModeOpt() {
		return new ArrayList<ParseMode>(Arrays.asList(ParseMode.values()));
	}

	public List<AnswerType> getAnswerTypeOpt() {
		return new ArrayList<AnswerType>(Arrays.asList(AnswerType.values()));
	}

	public List<Bot> getBots() {
		return bots;
	}

	public void setBots(List<Bot> bots) {
		this.bots = bots;
	}

}
