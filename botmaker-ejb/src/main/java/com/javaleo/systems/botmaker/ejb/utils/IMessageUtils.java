package com.javaleo.systems.botmaker.ejb.utils;

import javax.ejb.Local;

@Local
public interface IMessageUtils {

	void sendMailMessage(String from, String to, String subject, String content);

}
