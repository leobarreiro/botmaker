package com.javaleo.systems.botmaker.web.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import org.javaleo.libs.botgram.enums.ParseMode;

import com.javaleo.systems.botmaker.ejb.enums.ScriptType;

@Named
@ConversationScoped
public class AuxAction implements Serializable {

	private static final long serialVersionUID = 1L;

	public List<ScriptType> getScriptTypeOpt() {
		return new ArrayList<ScriptType>(Arrays.asList(ScriptType.values()));
	}

	public List<ParseMode> getParseModeOpt() {
		return new ArrayList<ParseMode>(Arrays.asList(ParseMode.values()));
	}

}
