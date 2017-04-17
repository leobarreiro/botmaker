package org.javaleo.grandpa.blog.converter;

import java.util.TimeZone;

import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

@Named
@FacesConverter(forClass = java.util.Date.class)
public class BlogDateTimeConverter extends DateTimeConverter {

	private static final String TIME_PATTERN = "MMM, dd, yyyy";
	private static final TimeZone LOCAL_ZONE = TimeZone.getTimeZone("America/Sao_Paulo");

	public BlogDateTimeConverter() {
		super();
		setTimeZone(LOCAL_ZONE);
		setPattern(TIME_PATTERN);
	}

}
