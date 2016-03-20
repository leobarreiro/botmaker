package com.javaleo.systems.botmaker.ejb.producers;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

public class ResourceBundleProducer {

	private static final String MESSAGES = "messages_";
	public static final String PT = "pt";
	public static final String EN = "en";
	public static final String ES = "es";
	public static final String ES_ES = "esES";
	public static final String EN_US = "enUS";
	public static final String PT_BR = "ptBR";
	private Locale locale = Locale.getDefault();

	@Produces
	@Named("msgs")
	public ResourceBundle getResourceBundle() {
		ResourceBundle bundle = ResourceBundle.getBundle(getLocaleLanguage(), locale);
		// ResourceBundle bundle = ResourceBundle.getBundle("messages_ptBR");
		return bundle;
	}

	public String getLocaleLanguage() {
		StringBuilder lang = new StringBuilder();
		lang.append(MESSAGES);
		if (locale.getLanguage().toLowerCase().equals(PT)) {
			lang.append(PT_BR);
		} else if (locale.getLanguage().toLowerCase().equals(ES)) {
			lang.append(ES_ES);
		} else {
			lang.append(EN_US);
		}
		return lang.toString();
	}
}