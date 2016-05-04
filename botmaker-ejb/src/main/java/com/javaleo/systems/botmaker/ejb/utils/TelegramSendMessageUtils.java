package com.javaleo.systems.botmaker.ejb.utils;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.javaleo.libs.botgram.enums.ParseMode;
import org.javaleo.libs.botgram.exceptions.BotGramException;
import org.javaleo.libs.botgram.model.ReplyKeyboardHide;
import org.javaleo.libs.botgram.model.ReplyKeyboardMarkup;
import org.javaleo.libs.botgram.request.SendMessageRequest;
import org.javaleo.libs.botgram.response.SendMessageResponse;
import org.javaleo.libs.botgram.service.BotGramConfig;
import org.javaleo.libs.botgram.service.BotGramService;
import org.slf4j.Logger;

import com.javaleo.systems.botmaker.ejb.entities.Bot;
import com.javaleo.systems.botmaker.ejb.pojos.Dialog;

@Named
@Stateless
public class TelegramSendMessageUtils implements Serializable {

	private static final long serialVersionUID = 8555391736869701045L;

	@Inject
	private Logger LOG;

	@Asynchronous
	public void sendSimpleMessage(Bot bot, Dialog dialog, String plainText) {
		SendMessageRequest request = new SendMessageRequest();
		request.setChatId(dialog.getId());
		if (StringUtils.isBlank(plainText)) {
			LOG.error("Tried to send an empty Message. Bot {}.", bot.getName());
			return;
		}
		byte[] textBytes = plainText.getBytes(StandardCharsets.ISO_8859_1);
		String text = new String(textBytes, StandardCharsets.UTF_8);
		request.setParseMode(ParseMode.HTML);
		ReplyKeyboardHide keyboardHide = new ReplyKeyboardHide();
		keyboardHide.setHideKeyboard(true);
		keyboardHide.setSelective(false);
		request.setKeyboard(keyboardHide);
		request.setText(text);
		try {
			sendMessage(bot.getToken(), request);
		} catch (BotGramException e) {
			LOG.error(e.getMessage());
		}
	}

	@Asynchronous
	public void sendMessageWithOptions(Bot bot, Dialog dialog, String plainText, List<List<String>> options) {
		SendMessageRequest request = new SendMessageRequest();
		request.setChatId(dialog.getId());
		byte[] textBytes = plainText.getBytes(StandardCharsets.ISO_8859_1);
		String text = new String(textBytes, StandardCharsets.UTF_8);
		ReplyKeyboardMarkup replyKey = new ReplyKeyboardMarkup();
		replyKey.setKeyboard(options);
		replyKey.setOneTimeKeyboard(true);
		replyKey.setResizeKeyboard(true);
		replyKey.setSelective(false);
		request.setKeyboard(replyKey);
		request.setParseMode(ParseMode.HTML);
		request.setText(text);
		try {
			sendMessage(bot.getToken(), request);
		} catch (BotGramException e) {
			LOG.error(e.getMessage());
		}
	}

	private SendMessageResponse sendMessage(String token, SendMessageRequest message) throws BotGramException {
		BotGramConfig config = new BotGramConfig();
		config.setToken(token);
		BotGramService service = new BotGramService(config);
		SendMessageResponse response = service.sendMessage(message);
		if (!response.getOk()) {
			LOG.info("Msg Ok: {} / Description: {}", response.getOk(), response.getDescription());
		}
		return response;
	}

}
