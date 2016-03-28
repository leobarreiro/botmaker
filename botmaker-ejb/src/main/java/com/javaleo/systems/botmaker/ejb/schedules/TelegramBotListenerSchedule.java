package com.javaleo.systems.botmaker.ejb.schedules;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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

import org.javaleo.libs.botgram.exceptions.BotGramException;
import org.javaleo.libs.botgram.model.Message;
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
		// LOG.info("Verificando Updates dos Bots ativos.");
		List<Bot> bots = botBusiness.listActiveBots();
		for (Bot bot : bots) {
			BotGramConfig config = new BotGramConfig();
			config.setToken(bot.getToken());
			BotGramService service = new BotGramService(config);
			try {
				GetUpdatesResponse updatesResponse = service.getUpdates(managerUtils.getNextUpdateOfsetFromBot(bot), 20);
				List<Update> updates = updatesResponse.getUpdates();
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
				dialog = new Dialog();
				dialog.setAnswers(new ArrayList<Answer>());
				Command command = commandBusiness.getCommandByBotAndKey(bot, u.getMessage().getText());

				// Unknowed Command
				if (command == null) {
					sendMessageToBotUser(bot, u.getMessage().getChat().getId(), bot.getUnknownCommadMessage());
				}
				// Command recognized
				else {
					dialog.setCommand(command);
					dialog.setAnswers(new ArrayList<Answer>());
					dialog.setFinish(false);
					dialog.setIdChat(u.getMessage().getChat().getId());
					Question question = questionBusiness.getNextQuestion(command, 0);
					dialog.setLastQuestion(question);
					dialog.setMessages(Arrays.asList(new Message[] { u.getMessage() }));
					dialog.setPendingServer(true);
					dialog.setUpdate(u);
					sendMessageToBotUser(bot, u.getMessage().getChat().getId(), question.getInstruction());
					managerUtils.addDialogToBot(bot, dialog);
				}
			}
			// Old Dialog
			else {
				List<Answer> answers = dialog.getAnswers();
				Answer a = new Answer();
				// TODO: realizar tratamento de respostas para ver se estao corretas.
				a.setAccepted(true);
				a.setAnswer(u.getMessage().getText());
				answers.add(a);
				dialog.setAnswers(answers);
				// sendMessageToBotUser(bot, u.getMessage().getChat().getId(), newQuestion.getInstruction());
				Question newQuestion = questionBusiness.getNextQuestion(dialog.getCommand(), dialog.getLastQuestion().getOrder());
				dialog.setLastQuestion(newQuestion);
				a.setQuestion(newQuestion);
				sendMessageToBotUser(bot, u.getMessage().getChat().getId(), newQuestion.getInstruction());
				managerUtils.updateDialogToBot(bot, dialog);
			}
		}

	}

	private void sendMessageToBotUser(Bot bot, Integer idChat, String instruction) {
		SendMessageRequest request = new SendMessageRequest();
		request.setChatId(idChat);
		byte[] textBytes = instruction.getBytes(StandardCharsets.ISO_8859_1);
		String text = new String(textBytes, Charset.forName("UTF-8"));
		request.setText(text);
		try {
			BotGramConfig config = new BotGramConfig();
			config.setToken(bot.getToken());
			BotGramService service = new BotGramService(config);
			SendMessageResponse messageResponse = service.sendMessage(request);
		} catch (BotGramException e) {
			LOG.error(e.getMessage());
		}

	}
}
