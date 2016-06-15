package com.javaleo.systems.botmaker.ejb.utils;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;

@Named
@Stateless
public class MessageUtils implements IMessageUtils {

	@Resource(mappedName = "java:jboss/mail/Javaleo")
	private Session mailSession;

	@Inject
	private Logger LOG;

	@Override
	@Asynchronous
	public void sendMailMessage(String from, String to, String subject, String content) {
		LOG.info("Sending Email from " + from + " to " + to + " : " + subject);
		try {
			Message message = new MimeMessage(mailSession);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to));
			message.setSubject(subject);
			message.setText(content);
			Transport.send(message);
			LOG.debug("Email was sent");
		} catch (MessagingException e) {
			LOG.error("Error while sending email : " + e.getMessage());
		}
	}

}
