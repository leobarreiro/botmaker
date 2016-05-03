package com.javaleo.systems.botmaker.web.action;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.javaleo.systems.botmaker.ejb.entities.UserPreference;
import com.javaleo.systems.botmaker.ejb.facades.IBotMakerFacade;
import com.javaleo.systems.botmaker.ejb.security.BotMakerCredentials;

@Named
@ConversationScoped
public class UserPreferenceAction implements Serializable {

	private static final String SCRIPTING_THEME = "SCRIPTING_THEME";

	private static final long serialVersionUID = 1L;

	@Inject
	private IBotMakerFacade facade;

	@Inject
	private BotMakerCredentials credentials;

	private UserPreference scriptingTheme;
	private String nameScriptingTheme;
	private List<String> themeOpt;

	public void loadPreferences() {
		scriptingTheme = facade.getPreferenceByUserAndName(credentials.getUser(), SCRIPTING_THEME);
		nameScriptingTheme = (scriptingTheme != null) ? scriptingTheme.getValue() : "default";
		themeOpt = Arrays
				.asList(new String[] { "ambiance", "cobalt", "eclipse", "elegant", "erlang-dark", "icecoder", "liquibyte", "monokai", "pastel-on-dark", "solarized", "rubyblue", "vibrant-ink" });
	}

	public void saveScriptingTheme() {
		UserPreference pref = facade.getPreferenceByUserAndName(credentials.getUser(), SCRIPTING_THEME);
		if (pref == null) {
			pref = new UserPreference();
			pref.setProperty(SCRIPTING_THEME);
		}
		pref.setValue(this.nameScriptingTheme);
		facade.savePreference(credentials.getUser(), pref);
	}

	public UserPreference getScriptingTheme() {
		return scriptingTheme;
	}

	public void setScriptingTheme(UserPreference scriptingTheme) {
		this.scriptingTheme = scriptingTheme;
	}

	public String getNameScriptingTheme() {
		return nameScriptingTheme;
	}

	public void setNameScriptingTheme(String nameScriptingTheme) {
		this.nameScriptingTheme = nameScriptingTheme;
	}

	public List<String> getThemeOpt() {
		return themeOpt;
	}

	public void setThemeOpt(List<String> themeOpt) {
		this.themeOpt = themeOpt;
	}

}
