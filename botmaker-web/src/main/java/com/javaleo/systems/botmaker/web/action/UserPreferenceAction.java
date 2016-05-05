package com.javaleo.systems.botmaker.web.action;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.javaleo.systems.botmaker.ejb.entities.UserPreference;
import com.javaleo.systems.botmaker.ejb.entities.UserPreference.PrefsType;
import com.javaleo.systems.botmaker.ejb.facades.IBotMakerFacade;
import com.javaleo.systems.botmaker.ejb.security.BotMakerCredentials;

@Named
@ConversationScoped
public class UserPreferenceAction implements Serializable {

	private static final String PAGE_PREFS = "/pages/preferences/user-preferences.jsf?faces-redirect=true";
	private static final String SCRIPTING_THEME = "SCRIPTING_THEME";
	private static final String SCRIPTING_MAX_HEIGHT = "SCRIPTING_MAX_HEIGHT";
	private static final String SCRIPTING_MAX_WIDTH = "SCRIPTING_MAX_WIDTH";
	private static final String SCRIPTING_SHOW_LINE_NUMBERS = "SCRIPTING_SHOW_LINE_NUMBERS";

	private static final long serialVersionUID = 1L;

	@Inject
	private IBotMakerFacade facade;

	@Inject
	private BotMakerCredentials credentials;

	private UserPreference scriptingTheme;
	private UserPreference editorMaxWidthSize;
	private UserPreference editorMaxHeightSize;
	private UserPreference editorShowLineNumbers;

	private String codeExample;

	private List<String> themeOpt;

	public String init() {
		initOptions();
		return PAGE_PREFS;
	}

	public void loadPreferences() {
		initOptions();
	}

	private void initOptions() {
		scriptingTheme = facade.getPreferenceByUserAndName(credentials.getUser(), SCRIPTING_THEME);
		if (scriptingTheme == null) {
			scriptingTheme = new UserPreference();
			scriptingTheme.setProperty(SCRIPTING_THEME);
			scriptingTheme.setType(PrefsType.STRING);
			scriptingTheme.setValue("default");
		}

		editorMaxHeightSize = facade.getPreferenceByUserAndName(credentials.getUser(), SCRIPTING_MAX_HEIGHT);
		if (editorMaxHeightSize == null) {
			editorMaxHeightSize = new UserPreference();
			editorMaxHeightSize.setProperty(SCRIPTING_MAX_HEIGHT);
			editorMaxHeightSize.setType(PrefsType.STRING);
			editorMaxHeightSize.setValue("350");
		}

		editorMaxWidthSize = facade.getPreferenceByUserAndName(credentials.getUser(), SCRIPTING_MAX_WIDTH);
		if (editorMaxWidthSize == null) {
			editorMaxWidthSize = new UserPreference();
			editorMaxWidthSize.setProperty(SCRIPTING_MAX_WIDTH);
			editorMaxWidthSize.setType(PrefsType.STRING);
			editorMaxWidthSize.setValue("80%");
		}

		editorShowLineNumbers = facade.getPreferenceByUserAndName(credentials.getUser(), SCRIPTING_SHOW_LINE_NUMBERS);
		if (editorShowLineNumbers == null) {
			editorShowLineNumbers = new UserPreference();
			editorShowLineNumbers.setProperty(SCRIPTING_SHOW_LINE_NUMBERS);
			editorShowLineNumbers.setType(PrefsType.BOOLEAN);
			editorShowLineNumbers.setValue("true");
		}

		codeExample = "Onononono\nOnononono\nOnononono";

		themeOpt = Arrays
				.asList(new String[] { "ambiance", "cobalt", "eclipse", "elegant", "erlang-dark", "icecoder", "liquibyte", "monokai", "pastel-on-dark", "solarized", "rubyblue", "vibrant-ink" });
	}

	public String savePreferences() {
		facade.savePreference(credentials.getUser(), editorMaxHeightSize);
		facade.savePreference(credentials.getUser(), editorMaxWidthSize);
		facade.savePreference(credentials.getUser(), scriptingTheme);
		return PAGE_PREFS;
	}

	public UserPreference getScriptingTheme() {
		return scriptingTheme;
	}

	public void setScriptingTheme(UserPreference scriptingTheme) {
		this.scriptingTheme = scriptingTheme;
	}

	public UserPreference getEditorMaxWidthSize() {
		return editorMaxWidthSize;
	}

	public void setEditorMaxWidthSize(UserPreference editorMaxWidthSize) {
		this.editorMaxWidthSize = editorMaxWidthSize;
	}

	public UserPreference getEditorMaxHeightSize() {
		return editorMaxHeightSize;
	}

	public void setEditorMaxHeightSize(UserPreference editorMaxHeightSize) {
		this.editorMaxHeightSize = editorMaxHeightSize;
	}

	public UserPreference getEditorShowLineNumbers() {
		return editorShowLineNumbers;
	}

	public void setEditorShowLineNumbers(UserPreference editorShowLineNumbers) {
		this.editorShowLineNumbers = editorShowLineNumbers;
	}

	public String getCodeExample() {
		return codeExample;
	}

	public void setCodeExample(String codeExample) {
		this.codeExample = codeExample;
	}

	public List<String> getThemeOpt() {
		return themeOpt;
	}

	public void setThemeOpt(List<String> themeOpt) {
		this.themeOpt = themeOpt;
	}

}
