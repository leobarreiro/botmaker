package org.javaleo.grandpa.ejb.utils;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.javaleo.grandpa.ejb.entities.bot.Bot;
import org.javaleo.grandpa.ejb.pojos.Dialog;
import org.javaleo.grandpa.ejb.pojos.UrlFile;
import org.javaleo.libs.botgram.enums.ParseMode;
import org.javaleo.libs.botgram.exceptions.BotGramException;
import org.javaleo.libs.botgram.model.Document;
import org.javaleo.libs.botgram.model.PhotoSize;
import org.javaleo.libs.botgram.model.ReplyKeyboardHide;
import org.javaleo.libs.botgram.model.ReplyKeyboardMarkup;
import org.javaleo.libs.botgram.model.TelegramFile;
import org.javaleo.libs.botgram.request.SendMessageRequest;
import org.javaleo.libs.botgram.response.GetFileResponse;
import org.javaleo.libs.botgram.response.SendMessageResponse;
import org.javaleo.libs.botgram.service.BotGramConfig;
import org.javaleo.libs.botgram.service.BotGramService;
import org.slf4j.Logger;

@Named
@Stateless
public class TelegramSendMessageUtils implements Serializable {

	private static final long serialVersionUID = 8555391736869701045L;

	@Inject
	private Logger LOG;

	@Asynchronous
	public void sendSimpleMessage(Bot bot, Dialog dialog, String plainText, ParseMode parseMode) {
		SendMessageRequest request = new SendMessageRequest();
		request.setChatId(dialog.getId());
		if (StringUtils.isBlank(plainText)) {
			LOG.error("Tried to send an empty Message. Bot {}.", bot.getName());
			return;
		}
		byte[] textBytes = plainText.getBytes(StandardCharsets.ISO_8859_1);
		String text = new String(textBytes, StandardCharsets.UTF_8);
		request.setParseMode(parseMode);
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
	public void sendMessageWithOptions(Bot bot, Dialog dialog, String plainText, ParseMode parseMode, List<List<String>> options) {
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
		request.setParseMode(parseMode);
		request.setText(text);
		try {
			sendMessage(bot.getToken(), request);
		} catch (BotGramException e) {
			LOG.error(e.getMessage());
		}
	}

	public UrlFile getUrlFile(String token, Document document) {
		UrlFile urlFile = new UrlFile();
		GetFileResponse rsp = getFileMessage(token, document.getId());
		TelegramFile tgf = rsp.getFile();
		urlFile.setMimeType(document.getMimeType());
		urlFile.setOriginalId(tgf.getId());
		urlFile.setName(FilenameUtils.getBaseName(tgf.getPath()));
		urlFile.setSize(tgf.getSize());
		urlFile.setUrl(getUrlFile(token, tgf.getPath()));
		return urlFile;
	}

	public List<UrlFile> getUrlFiles(String token, List<PhotoSize> photoSizes) {
		List<UrlFile> urlFiles = new ArrayList<UrlFile>();
		for (PhotoSize photo : photoSizes) {
			GetFileResponse rsp = getFileMessage(token, photo.getId());
			TelegramFile tgf = rsp.getFile();
			UrlFile uf = new UrlFile();
			uf.setHeight(photo.getHeight());
			uf.setWidth(photo.getWidth());
			uf.setName(FilenameUtils.getBaseName(tgf.getPath()));
			uf.setSize(tgf.getSize());
			uf.setOriginalId(tgf.getId());
			uf.setUrl(getUrlFile(token, tgf.getPath()));
			// uf.setMimeType(mimeType);
			urlFiles.add(uf);
		}
		return urlFiles;
	}

	private GetFileResponse getFileMessage(String token, String fileId) {
		BotGramConfig config = new BotGramConfig();
		config.setToken(token);
		BotGramService service = new BotGramService(config);
		GetFileResponse response = null;
		try {
			response = service.getFile(fileId);
		} catch (BotGramException e) {
			LOG.error(e.getMessage());
		}
		return response;
	}

	public String getUrlFile(String token, String pathFile) {
		BotGramConfig config = new BotGramConfig();
		config.setToken(token);
		BotGramService service = new BotGramService(config);
		String response = null;
		try {
			response = service.getFileUrlDownload(pathFile);
		} catch (BotGramException e) {
			LOG.error(e.getMessage());
		}
		return response;
	}

	// public void saveFileFromUserBot(Bot bot, Dialog dialog, String url) {
	// StringBuilder str = new StringBuilder(System.getProperty("botmaker.companies.dir"));
	// str.append(File.separator);
	// str.append(bot.getCompany().getId());
	// str.append(File.separator);
	// str.append(bot.getId());
	// str.append(File.separator);
	// str.append(dialog.getId());
	// try {
	// URL file = new URL(url);
	// File fileDir = new File(str.toString());
	// FileUtils.forceMkdir(fileDir);
	// str.append(File.separator);
	// List<Answer> answers = dialog.getAnswers();
	// Answer answer = answers.get(answers.size() - 1);
	// str.append(answer.getFileId());
	// File fileName = new File(str.toString());
	// FileUtils.copyURLToFile(file, fileName);
	// } catch (IOException e) {
	// LOG.error(e.getMessage());
	// }
	// }

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
