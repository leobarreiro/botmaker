package com.javaleo.systems.botmaker.ejb.schedules;

import java.io.Serializable;
import java.util.ArrayList;
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
import org.javaleo.libs.botgram.exceptions.BotGramException;
import org.javaleo.libs.botgram.model.Update;
import org.javaleo.libs.botgram.response.GetUpdatesResponse;
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
import com.javaleo.systems.botmaker.ejb.utils.TelegramSendMessageUtils;

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
	private TelegramSendMessageUtils sendMessageUtils;

	@Inject
	private Logger LOG;

	@Inject
	private ManagerUtils managerUtils;

	@Schedule(dayOfWeek = "*", hour = "*", minute = "*", second = "*/1", persistent = false)
	public void listenBotUpdates() {
		List<Bot> bots = botBusiness.listActiveBots();
		for (Bot bot : bots) {
			processDialogsToBot(bot);
		}
	}

	public void processDialogsToBot(Bot bot) {
		if (managerUtils.isProcessingBot(bot)) {
			return;
		}
		managerUtils.startProcessBot(bot);
		BotGramConfig config = new BotGramConfig();
		config.setToken(bot.getToken());
		BotGramService service = new BotGramService(config);
		List<Update> updates = new ArrayList<Update>();
		try {
			GetUpdatesResponse updatesResponse = service.getUpdates(managerUtils.getNextUpdateOfsetFromBot(bot), 20);
			updates = updatesResponse.getUpdates();
			// LOG.info(MessageFormat.format("Bot {0} possui {1} update(s).", bot.getName(),
			// updates.size()));
			managerUtils.addUpdatesToBot(bot, updates);
		} catch (BotGramException e) {
			LOG.error(e.getMessage());
		}

		if (updates == null || updates.isEmpty()) {
			managerUtils.finishProcessingBot(bot);
			return;
		}

		Set<Dialog> dialogs = managerUtils.getDialogsFromBot(bot);
		Map<Integer, Dialog> dialogMap = new HashMap<Integer, Dialog>();
		for (Dialog d : dialogs) {
			if (!d.isFinish()) {
				dialogMap.put(d.getId(), d);
			}
		}

		for (Update u : updates) {
			Dialog dialog = dialogMap.get(u.getMessage().getChat().getId());
			// New Dialog
			if (dialog == null) {
				dialog = startNewDialog(bot, u);
				managerUtils.addDialogToBot(bot, dialog);
			}
			// Old Dialog
			else {
				boolean cancelCalled = (StringUtils.isNotEmpty(bot.getCancelKey()) && StringUtils.equalsIgnoreCase(bot.getCancelKey(), u.getMessage().getText()));
				if (cancelCalled) {
					breakDialog(bot, dialog, u);
				} else {
					proceedDialog(bot, dialog, u.getMessage().getText());
				}
			}
		}
		managerUtils.finishProcessingBot(bot);
	}

	private void breakDialog(Bot bot, Dialog dialog, Update u) {
		if (StringUtils.isNotEmpty(bot.getCancelKey()) && StringUtils.equalsIgnoreCase(bot.getCancelKey(), u.getMessage().getText())) {
			dialog.setPendingServer(false);
			if (StringUtils.isNotEmpty(bot.getCancelMessage())) {
				sendMessageUtils.sendSimpleMessage(bot, dialog, bot.getCancelMessage());
			}
		}
		managerUtils.removeDialog(bot, dialog);
	}

	private Dialog startNewDialog(Bot bot, Update update) {
		Dialog dialog = new Dialog();
		dialog.setId(update.getMessage().getChat().getId());
		dialog.setAnswers(new ArrayList<Answer>());
		dialog.setPendingServer(true);
		dialog.setFinish(false);
		Command command = commandBusiness.getCommandByBotAndKey(bot, update.getMessage().getText());
		// Command unknown
		if (command == null) {
			unknowCommand(bot, dialog);
			dialog.setPendingServer(false);
		}
		// Command found
		else {
			dialog.setLastCommand(command);
			dialog.setAnswers(new ArrayList<Answer>());
			Question question = questionBusiness.getNextQuestion(command, 0);
			dialog.setLastQuestion(question);
			dialog.setPendingServer(true);
			dialog.setLastUpdate(update);
			instructUser(bot, dialog, question);
			dialog.setPendingServer(false);
		}
		return dialog;
	}

	private void proceedDialog(Bot bot, Dialog dialog, String userText) {
		List<Answer> answers = dialog.getAnswers();
		Answer ans = new Answer();
		ans.setQuestion(dialog.getLastQuestion());
		ans.setAnswer(userText);
		answers.add(ans);
		dialog.setAnswers(answers);
		dialog.setPendingServer(true);
		if (questionBusiness.validateAnswer(dialog.getLastQuestion(), ans)) {
			ans.setVarName(dialog.getLastQuestion().getVarName());
			ans.setAccepted(true);
			if (StringUtils.isNotBlank(dialog.getLastQuestion().getSuccessMessage())) {
				successAnswer(bot, dialog, dialog.getLastQuestion());
			}
			if (dialog.getLastQuestion().getProcessAnswer()) {
				questionBusiness.postProcessAnswer(dialog, dialog.getLastQuestion(), ans);
				sendMessageUtils.sendSimpleMessage(bot, dialog, ans.getPostProcessedAnswer());
			}
			Question nextQuestion = questionBusiness.getNextQuestion(dialog.getLastCommand(), dialog.getLastQuestion().getOrder());
			if (nextQuestion != null) {
				dialog.setLastQuestion(nextQuestion);
				instructUser(bot, dialog, nextQuestion);
				dialog.setPendingServer(false);
				dialog.setLastQuestion(nextQuestion);
				managerUtils.updateDialogToBot(bot, dialog);
			} else {
				if (dialog.getLastQuestion().getCommand().getPostProcess()) {
					commandBusiness.postProcessCommand(dialog, dialog.getLastQuestion().getCommand());
					sendMessageUtils.sendSimpleMessage(bot, dialog, dialog.getPostProcessedResult());
				}
				endOfCommand(bot, dialog);
				dialog.setFinish(true);
				dialog.setPendingServer(false);
				managerUtils.removeDialog(bot, dialog);
			}
		} else {
			ans.setAccepted(false);
			errorFormat(bot, dialog, dialog.getLastQuestion());
			dialog.setPendingServer(false);
			managerUtils.updateDialogToBot(bot, dialog);
		}
	}

	private void instructUser(Bot bot, Dialog dialog, Question question) {
		if (dialog.isPendingServer()) {
			if (question.getValidator().getScriptType().isSetOfOptions()) {
				List<List<String>> options = questionBusiness.convertOptions(question);
				sendMessageUtils.sendMessageWithOptions(bot, dialog, question.getInstruction(), options);
			} else {
				sendMessageUtils.sendSimpleMessage(bot, dialog, question.getInstruction());
			}
		}
	}

	private void unknowCommand(Bot bot, Dialog dialog) {
		if (dialog.isPendingServer() && StringUtils.isNotBlank(bot.getUnknownCommadMessage())) {
			List<List<String>> options = getAvailableCommands(bot);
			if (options.isEmpty()) {
				sendMessageUtils.sendSimpleMessage(bot, dialog, bot.getUnknownCommadMessage());
			} else {
				sendMessageUtils.sendMessageWithOptions(bot, dialog, bot.getUnknownCommadMessage(), options);
			}
		}
	}

	private void endOfCommand(Bot bot, Dialog dialog) {
		List<List<String>> options = getAvailableCommands(bot);
		if (StringUtils.isNotBlank(bot.getEndOfDialogMessage())) {
			sendMessageUtils.sendMessageWithOptions(bot, dialog, bot.getEndOfDialogMessage(), options);
		}
	}

	private void errorFormat(Bot bot, Dialog dialog, Question question) {
		if (StringUtils.isNotBlank(question.getErrorFormatMessage())) {
			sendMessageUtils.sendSimpleMessage(bot, dialog, question.getErrorFormatMessage());
		}
	}

	private void successAnswer(Bot bot, Dialog dialog, Question question) {
		if (StringUtils.isNotBlank(question.getSuccessMessage())) {
			sendMessageUtils.sendSimpleMessage(bot, dialog, question.getSuccessMessage());
		}
	}

	private List<List<String>> getAvailableCommands(Bot bot) {
		List<List<String>> options = new ArrayList<List<String>>();
		if (bot.getListCommands()) {
			options = commandBusiness.convertCommandsToOptions(bot);
		}
		return options;
	}

	// ScriptEngineManager mgr = new ScriptEngineManager();
	// jsEngine = mgr.getEngineByName("JavaScript");

}
