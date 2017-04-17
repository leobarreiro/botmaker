package org.javaleo.grandpa.web.converter;

import java.util.TimeZone;

import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

@Named
@FacesConverter(forClass = java.util.Date.class)
public class BotMakerDateTimeConverter extends DateTimeConverter {

	private static final String TIME_PATTERN = "dd/MM/yy HH'h'mm'm'";
	private static final TimeZone LOCAL_ZONE = TimeZone.getTimeZone("America/Sao_Paulo");

	public BotMakerDateTimeConverter() {
		super();
		setTimeZone(LOCAL_ZONE);
		setPattern(TIME_PATTERN);
	}

}
