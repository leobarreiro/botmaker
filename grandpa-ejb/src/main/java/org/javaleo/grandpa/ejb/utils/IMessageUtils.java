package org.javaleo.grandpa.ejb.utils;

import java.util.Map;

import javax.ejb.Local;

@Local
public interface IMessageUtils {

	void sendMailMessage(String from, String to, String subject, String content);

	String assemblyBodyMail(Map<String, String> keyValues, String rawContent);

}
