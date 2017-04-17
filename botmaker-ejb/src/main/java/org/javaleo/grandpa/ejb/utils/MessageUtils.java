package org.javaleo.grandpa.ejb.utils;

import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

@Named
@Stateless
public class MessageUtils implements IMessageUtils {

	private static final String X_MAILER = "X-Mailer";

	private static final String MAIL_CLIENT = "MailClient";

	private static final String MULTIPART_MIME = "text/html; charset=utf-8";

	@Resource(mappedName = "java:jboss/mail/Javaleo")
	private Session mailSession;

	@Inject
	private Logger LOG;

	@Override
	@Asynchronous
	public void sendMailMessage(String from, String to, String subject, String content) {
		LOG.info("Sending Email from " + from + " to " + to + " : " + subject);
		try {
			MimeBodyPart bodyPart = new MimeBodyPart();
			bodyPart.setContent(content, MULTIPART_MIME);
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(bodyPart);
			Message message = new MimeMessage(mailSession);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(subject);
			// message.setText(content);
			message.setContent(multipart);
			message.setHeader(X_MAILER, MAIL_CLIENT);
			Transport.send(message);
			LOG.info("Email was sent");
		} catch (MessagingException e) {
			LOG.error("Error sending email : " + e.getMessage());
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public String assemblyBodyMail(Map<String, String> keyValues, String rawContent) {
		return StringUtils.replaceEachRepeatedly(rawContent, keyValues.keySet().toArray(new String[keyValues.size()]), keyValues.values().toArray(new String[keyValues.size()]));
	}
}
