package com.javaleo.systems.botmaker.ejb.schedules;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Local;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.javaleo.libs.botgram.enums.ParseMode;
import org.javaleo.libs.botgram.exceptions.BotGramException;
import org.javaleo.libs.botgram.model.Message;
import org.javaleo.libs.botgram.model.ReplyKeyboardHide;
import org.javaleo.libs.botgram.model.ReplyKeyboardMarkup;
import org.javaleo.libs.botgram.model.Update;
import org.javaleo.libs.botgram.request.SendMessageRequest;
import org.javaleo.libs.botgram.response.GetUpdatesResponse;
import org.javaleo.libs.botgram.response.SendMessageResponse;
import org.javaleo.libs.botgram.service.BotGramConfig;
import org.javaleo.libs.botgram.service.BotGramService;
import org.slf4j.Logger;

import com.javaleo.systems.botmaker.ejb.business.IBotBusiness;
import com.javaleo.systems.botmaker.ejb.business.ICommandBusiness;
import com.javaleo.systems.botmaker.ejb.business.IQuestionBusiness;
import com.javaleo.systems.botmaker.ejb.entities.Bot;
import com.javaleo.systems.botmaker.ejb.entities.Command;
import com.javaleo.systems.botmaker.ejb.entities.Question;
import com.javaleo.systems.botmaker.ejb.pojos.Answer;
import com.javaleo.systems.botmaker.ejb.pojos.Dialog;

@Named
@Local
@Stateless
public class TelegramBotListenerSchedule implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IBotBusiness botBusiness;

	@Inject
	private ICommandBusiness commandBusiness;

	@Inject
	private IQuestionBusiness questionBusiness;

	@Inject
	private Logger LOG;

	@Inject
	private ManagerUtils managerUtils;

	@Schedule(dayOfWeek = "*", hour = "*", minute = "*", second = "*/10", persistent = false)
	public void listenBotUpdates() {
		List<Bot> bots = botBusiness.listActiveBots();
		// LOG.info(MessageFormat.format("Verificando updates de {0} bot(s) ativo(s).",
		// bots.size()));
		for (Bot bot : bots) {
			BotGramConfig config = new BotGramConfig();
			config.setToken(bot.getToken());
			BotGramService service = new BotGramService(config);
			try {
				GetUpdatesResponse updatesResponse = service.getUpdates(managerUtils.getNextUpdateOfsetFromBot(bot), 20);
				List<Update> updates = updatesResponse.getUpdates();
				// LOG.info(MessageFormat.format("Bot {0} possui {1} update(s).", bot.getName(),
				// updates.size()));
				managerUtils.addUpdatesToBot(bot, updates);
				processDialogsToBot(bot, updates);
			} catch (BotGramException e) {
				LOG.error(e.getMessage());
			}
		}
	}

	public void processDialogsToBot(Bot bot, List<Update> updates) {
		if (updates == null || updates.isEmpty()) {
			return;
		}
		Set<Dialog> dialogs = managerUtils.getDialogsFromBot(bot);
		Map<Integer, Dialog> dialogMap = new HashMap<Integer, Dialog>();
		for (Dialog d : dialogs) {
			dialogMap.put(d.getIdChat(), d);
		}

		for (Update u : updates) {
			Dialog dialog = dialogMap.get(u.getMessage().getChat().getId());
			// New Dialog
			if (dialog == null) {
				// LOG.info(MessageFormat.format("Novo dialogo estabelecido: {0} / {1}.",
				// u.getMessage().getChat().getUsername(), u.getMessage().getText()));
				dialog = new Dialog();
				dialog.setIdChat(u.getMessage().getChat().getId());
				dialog.setAnswers(new ArrayList<Answer>());
				Command command = commandBusiness.getCommandByBotAndKey(bot, u.getMessage().getText());
				// Command unknown
				if (command == null) {
					sendMessageUnknowCommand(bot, dialog.getIdChat());
				}
				// Command recognized
				else {
					dialog.setCommand(command);
					dialog.setAnswers(new ArrayList<Answer>());
					dialog.setFinish(false);
					Question question = questionBusiness.getNextQuestion(command, 0);
					dialog.setLastQuestion(question);
					dialog.setMessages(Arrays.asList(new Message[] { u.getMessage() }));
					dialog.setPendingServer(true);
					dialog.setUpdate(u);
					sendMessage(bot, dialog.getIdChat(), question, question.getInstruction());
					dialog.setPendingServer(false);
					managerUtils.addDialogToBot(bot, dialog);
				}
			}
			// Old Dialog
			else {
				List<Answer> answers = dialog.getAnswers();
				String requestedMessage;
				Answer ans = new Answer();
				ans.setQuestion(dialog.getLastQuestion());
				ans.setAnswer(u.getMessage().getText());
				answers.add(ans);
				dialog.setAnswers(answers);
				if (questionBusiness.validateAnswer(dialog.getLastQuestion(), ans)) {
					ans.setAccepted(true);
					if (StringUtils.isNotBlank(dialog.getLastQuestion().getSuccessMessage())) {
						sendMessageSuccessAnswer(bot, dialog.getIdChat(), dialog.getLastQuestion());
					}
					Question nextQuestion = questionBusiness.getNextQuestion(dialog.getCommand(), dialog.getLastQuestion().getOrder());
					if (nextQuestion != null) {
						dialog.setLastQuestion(nextQuestion);
						sendMessage(bot, dialog.getIdChat(), nextQuestion, nextQuestion.getInstruction());
						dialog.setPendingServer(false);
						dialog.setLastQuestion(nextQuestion);
						managerUtils.updateDialogToBot(bot, dialog);
					} else {
						sendMessageEndOfDialog(bot, dialog.getIdChat());
						dialog.setFinish(true);
						dialog.setPendingServer(false);
						managerUtils.removeFinishedDialog(bot, dialog);
					}
				} else {
					ans.setAccepted(false);
					sendMessageErrorFormat(bot, dialog.getIdChat(), dialog.getLastQuestion());
					managerUtils.updateDialogToBot(bot, dialog);
				}
			}
		}

	}

	private void sendMessage(Bot bot, Integer idChat, Question question, String text) {
		if (question.getExpectedAnswer().getSnippetType().isSetOfOptions()) {
			List<List<String>> options = questionBusiness.convertOptions(question);
			sendMessageWithOptions(bot, idChat, text, options);
		} else {
			sendMessageWithoutOptions(bot, idChat, text);
		}
	}

	private void sendMessageUnknowCommand(Bot bot, Integer idChat) {
		if (StringUtils.isNotBlank(bot.getUnknownCommadMessage())) {
			if (bot.getListCommands()) {
				List<List<String>> options = commandBusiness.convertCommandsToOptions(bot);
				sendMessageWithOptions(bot, idChat, bot.getUnknownCommadMessage(), options);
			} else {
				sendMessageWithoutOptions(bot, idChat, bot.getUnknownCommadMessage());
			}
		}
	}

	private void sendMessageEndOfDialog(Bot bot, Integer idChat) {
		if (StringUtils.isNotBlank(bot.getEndOfDialogMessage())) {
			sendMessageWithoutOptions(bot, idChat, bot.getEndOfDialogMessage());
		}
	}

	private void sendMessageErrorFormat(Bot bot, Integer idChat, Question question) {
		if (StringUtils.isNotBlank(question.getErrorFormatMessage())) {
			sendMessageWithoutOptions(bot, idChat, question.getErrorFormatMessage());
		}
	}

	private void sendMessageSuccessAnswer(Bot bot, Integer idChat, Question question) {
		if (StringUtils.isNotBlank(question.getSuccessMessage())) {
			sendMessageWithoutOptions(bot, idChat, question.getSuccessMessage());
		}
	}

	private void sendMessageWithoutOptions(Bot bot, Integer idChat, String plainText) {
		SendMessageRequest request = new SendMessageRequest();
		request.setChatId(idChat);
		byte[] textBytes = plainText.getBytes(StandardCharsets.ISO_8859_1);
		String text = new String(textBytes, StandardCharsets.UTF_8);
		request.setParseMode(ParseMode.HTML);
		ReplyKeyboardHide keyboardHide = new ReplyKeyboardHide();
		keyboardHide.setHideKeyboard(true);
		keyboardHide.setSelective(false);
		request.setKeyboard(keyboardHide);
		request.setText(text);
		try {
			BotGramConfig config = new BotGramConfig();
			config.setToken(bot.getToken());
			BotGramService service = new BotGramService(config);
			SendMessageResponse messageResponse = service.sendMessage(request);
			LOG.info(MessageFormat.format("Msg [Ok:{0}|Dsc:{1}]", messageResponse.getOk(), messageResponse.getDescription()));
		} catch (BotGramException e) {
			LOG.error(e.getMessage());
		}
	}

	private void sendMessageWithOptions(Bot bot, Integer idChat, String plainText, List<List<String>> options) {
		SendMessageRequest request = new SendMessageRequest();
		request.setChatId(idChat);
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
			BotGramConfig config = new BotGramConfig();
			config.setToken(bot.getToken());
			BotGramService service = new BotGramService(config);
			SendMessageResponse messageResponse = service.sendMessage(request);
			LOG.info(MessageFormat.format("Msg [Ok:{0}|Dscr:{1}]", messageResponse.getOk(), messageResponse.getDescription()));
		} catch (BotGramException e) {
			LOG.error(e.getMessage());
		}
	}

	// ScriptEngineManager mgr = new ScriptEngineManager();
	// jsEngine = mgr.getEngineByName("JavaScript");

}
