package org.javaleo.grandpa.ejb.utils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class GrandPaUtils {

	public static final String DOMAIN_NAME = "javaleo.org";

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

	public static boolean validateContentAgainstPattern(final String pattern, final String content) {
		if (StringUtils.isBlank(content)) {
			return false;
		}
		Pattern compiledPattern = Pattern.compile(pattern);
		Matcher matcher = compiledPattern.matcher(content);
		return matcher.matches();
	}

	public static boolean validateEmail(final String email) {
		return validateContentAgainstPattern("^[a-zA-Z0-9][a-zA-Z0-9_\\-.]*[a-zA-Z0-9]@[a-zA-Z0-9][a-zA-Z0-9.]*\\.[a-zA-Z0-9.]+$", email);
	}

}
