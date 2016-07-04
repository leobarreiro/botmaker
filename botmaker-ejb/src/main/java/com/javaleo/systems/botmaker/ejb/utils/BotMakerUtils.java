package com.javaleo.systems.botmaker.ejb.utils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class BotMakerUtils {

	public static List<List<String>> convertStringToArrayOfArrays(String source, int maxSize, char separator) {
		String[] optArray = StringUtils.split(source, separator);
		List<String> options = Arrays.asList(optArray);
		return convertArrayOfArrays(options, maxSize);
	}

	public static List<List<String>> convertArrayOfArrays(List<String> originalOptions, int maxSize) {
		List<List<String>> listOfLists = new ArrayList<List<String>>();
		List<String> subList = new ArrayList<String>();
		for (int i = 0; i < originalOptions.size(); i++) {
			byte[] textBytes = StringUtils.trim(originalOptions.get(i)).getBytes(StandardCharsets.ISO_8859_1);
			subList.add(new String(textBytes, StandardCharsets.UTF_8));
			if (i % maxSize == 0) {
				listOfLists.add(subList);
				subList = new ArrayList<String>();
			}
		}
		listOfLists.add(subList);
		return listOfLists;
	}

	public static boolean validateEmail(final String email) {
		if (StringUtils.isBlank(email)) {
			return false;
		}
		Pattern mailPattern = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9_\\-.]*[a-zA-Z0-9]@[a-zA-Z0-9][a-zA-Z0-9.]*\\.[a-zA-Z0-9.]+$");
		Matcher mailMatcher = mailPattern.matcher(email);
		return mailMatcher.matches();
	}

}
