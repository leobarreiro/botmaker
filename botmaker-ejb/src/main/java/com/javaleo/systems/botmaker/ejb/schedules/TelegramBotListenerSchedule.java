package com.javaleo.systems.botmaker.ejb.schedules;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
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
import org.javaleo.libs.botgram.model.Document;
import org.javaleo.libs.botgram.model.PhotoSize;
import org.javaleo.libs.botgram.model.Update;
import org.javaleo.libs.botgram.response.GetUpdatesResponse;
import org.javaleo.libs.botgram.service.BotGramConfig;
import org.javaleo.libs.botgram.service.BotGramService;
import org.slf4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javaleo.systems.botmaker.ejb.business.IBotBusiness;
import com.javaleo.systems.botmaker.ejb.business.ICommandBusiness;
import com.javaleo.systems.botmaker.ejb.business.IQuestionBusiness;
import com.javaleo.systems.botmaker.ejb.entities.Bot;
import com.javaleo.systems.botmaker.ejb.entities.Command;
import com.javaleo.systems.botmaker.ejb.entities.Question;
import com.javaleo.systems.botmaker.ejb.enums.AnswerType;
import com.javaleo.systems.botmaker.ejb.pojos.Answer;
import com.javaleo.systems.botmaker.ejb.pojos.Dialog;
import com.javaleo.systems.botmaker.ejb.pojos.UrlFile;
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
		List<Bot> bots = botBusiness.listValidAndActiveBots();
		for (Bot bot : bots) {
			processDialogsToBot(bot);
		}
	}

	@Schedule(dayOfWeek = "*", hour = "*/1", minute = "00,30", second = "00", persistent = false)
	public void cleanDialogsWithoutInteraction() {
		LOG.info("Removing old dialogs without interaction.");
		managerUtils.removeOldDialogWithoutInteraction();
	}

	private void processDialogsToBot(Bot bot) {
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
			dialogMap.put(d.getId(), d);
		}

		for (Update u : updates) {
			Dialog dialog = (dialogMap.containsKey(u.getMessage().getChat().getId())) ? dialogMap.get(u.getMessage().getChat().getId()) : startNewDialog(bot, u);
			if (!dialog.isPendingCommand()) {
				startNewCommand(bot, dialog, u);
			} else {
				proceedDialog(bot, dialog, u);
			}
			managerUtils.updateDialogToBot(bot, dialog);
		}
		managerUtils.finishProcessingBot(bot);
	}

	private Dialog startNewDialog(Bot bot, Update update) {
		Dialog dialog = new Dialog();
		dialog.setId(update.getMessage().getChat().getId());
		dialog.setAnswers(new ArrayList<Answer>());
		dialog.setPendingServer(true);
		dialog.setPendingCommand(false);
		setLastInteraction(dialog);
		dialog.addContextVar("idChat", String.valueOf(dialog.getId()));
		dialog.addContextVar("lastInteraction", String.valueOf(update.getMessage().getDate()));
		dialog.addContextVar("userId", String.valueOf(update.getMessage().getFrom().getId()));
		managerUtils.addDialogToBot(bot, dialog);
		return dialog;
	}

	private void startNewCommand(Bot bot, Dialog dialog, Update update) {
		String[] userTextParts = StringUtils.split(update.getMessage().getText(), "_");
		if (userTextParts == null) {
			return;
		}
		List<String> userInput = new ArrayList<String>(Arrays.asList(userTextParts));
		Command command = commandBusiness.getCommandByBotAndKey(bot, userInput.get(0));
		if (command == null) { // Command unknown
			unknowCommand(bot, dialog);
			dialog.setPendingServer(false);
			dialog.setPendingCommand(false);
		} else { // Command found
			dialog.setLastCommand(command);
			dialog.setPendingCommand(true);
			dialog.setPendingServer(false);
			dialog.setAnswers(new ArrayList<Answer>());
			dialog.setLastUpdate(update);
			if (command.getQuestions() != null && !command.getQuestions().isEmpty()) {
				List<Question> questions = new ArrayList<Question>(command.getQuestions());
				Collections.sort(questions);
				Question question = questions.get(0);
				dialog.setLastQuestion(question);
				if (userInput.size() > 1) {
					userInput.remove(0);
					String userText = StringUtils.join(userInput.toArray(), " ");
					dialog.getLastUpdate().getMessage().setText(userText);
					proceedDialog(bot, dialog, update);
				} else {
					instructQuestionToUser(bot, dialog);
				}
			} else {
				endOfCommand(bot, dialog);
			}
		}

	}

	private void proceedDialog(Bot bot, Dialog dialog, Update update) {
		dialog.setLastUpdate(update);
		String userText = update.getMessage().getText();
		if ((StringUtils.isNotEmpty(bot.getCancelKey()) && StringUtils.equalsIgnoreCase(bot.getCancelKey(), userText))) {
			breakDialog(bot, dialog, userText);
			return;
		}
		List<Answer> answers = dialog.getAnswers();
		Answer ans = new Answer();
		ans.setAnswerType(dialog.getLastQuestion().getAnswerType());
		ans.setQuestion(dialog.getLastQuestion());
		answers.add(ans);
		dialog.setAnswers(answers);
		dialog.setPendingServer(true);
		setLastInteraction(dialog);
		if (questionBusiness.validateAnswer(dialog, dialog.getLastQuestion())) {
			fillAnswer(bot, dialog, ans);
			if (StringUtils.isNotBlank(dialog.getLastQuestion().getSuccessMessage())) {
				successAnswer(bot, dialog);
			}
			if (dialog.getLastQuestion().getProcessAnswer()) {
				questionBusiness.postProcessAnswer(dialog, dialog.getLastQuestion(), ans);
				sendMessageUtils.sendSimpleMessage(bot, dialog, ans.getPostProcessedAnswer(), dialog.getLastQuestion().getParseMode());
			}
			Question nextQuestion = questionBusiness.getNextQuestion(dialog.getLastCommand(), dialog.getLastQuestion().getOrder());
			if (nextQuestion != null) {
				dialog.setLastQuestion(nextQuestion);
				instructQuestionToUser(bot, dialog);
				dialog.setPendingServer(false);
				dialog.setLastQuestion(nextQuestion);
			} else {
				endOfCommand(bot, dialog);
			}
		} else {
			ans.setAccepted(false);
			errorFormat(bot, dialog);
			dialog.setPendingServer(false);
		}
		managerUtils.updateDialogToBot(bot, dialog);
	}

	private void fillAnswer(Bot bot, Dialog dialog, Answer ans) {
		ans.setVarName(dialog.getLastQuestion().getVarName());
		ans.setAccepted(true);
		if (ans.getAnswerType().equals(AnswerType.DOCUMENT) || ans.getAnswerType().equals(AnswerType.PHOTO)) {
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.excludeFieldsWithoutExposeAnnotation();
			Gson gson = gsonBuilder.create();
			if (ans.getAnswerType().equals(AnswerType.DOCUMENT)) {
				Document document = dialog.getLastUpdate().getMessage().getDocument();
				UrlFile uFile = sendMessageUtils.getUrlFile(bot.getToken(), document);
				List<UrlFile> urlFiles = new ArrayList<UrlFile>();
				urlFiles.add(uFile);
				ans.setUrlFiles(urlFiles);
				dialog.addContextVar(ans.getQuestion().getVarName(), gson.toJson(uFile));
			} else {
				List<PhotoSize> photoSizes = dialog.getLastUpdate().getMessage().getPhotosizes();
				List<UrlFile> urlFiles = sendMessageUtils.getUrlFiles(bot.getToken(), photoSizes);
				ans.setUrlFiles(urlFiles);
				dialog.addContextVar(ans.getQuestion().getVarName(), gson.toJson(urlFiles));
			}
		} else {
			ans.setAnswer(dialog.getLastUpdate().getMessage().getText());
			dialog.addContextVar(ans.getQuestion().getVarName(), dialog.getLastUpdate().getMessage().getText());
		}
	}

	private void breakDialog(Bot bot, Dialog dialog, String userText) {
		if (StringUtils.isNotEmpty(bot.getCancelMessage())) {
			sendMessageUtils.sendSimpleMessage(bot, dialog, bot.getCancelMessage(), ParseMode.HTML);
		}
		dialog.setLastCommand(null);
		dialog.setLastQuestion(null);
		dialog.setPendingServer(false);
		dialog.setPendingCommand(false);
		managerUtils.updateDialogToBot(bot, dialog);
		setLastInteraction(dialog);
	}

	private void instructQuestionToUser(Bot bot, Dialog dialog) {
		if (dialog.getLastQuestion() != null) {
			if (dialog.getLastQuestion().getValidator() != null && dialog.getLastQuestion().getValidator().getValidatorType().isSetOfOptions()) {
				List<List<String>> options = questionBusiness.convertOptions(dialog.getLastQuestion());
				sendMessageUtils.sendMessageWithOptions(bot, dialog, dialog.getLastQuestion().getInstruction(), dialog.getLastQuestion().getParseMode(), options);
			} else {
				sendMessageUtils.sendSimpleMessage(bot, dialog, dialog.getLastQuestion().getInstruction(), dialog.getLastQuestion().getParseMode());
			}
		}
	}

	private void unknowCommand(Bot bot, Dialog dialog) {
		if (dialog.isPendingServer() && StringUtils.isNotBlank(bot.getUnknownCommadMessage())) {
			List<List<String>> options = getAvailableCommands(bot);
			if (options.isEmpty()) {
				sendMessageUtils.sendSimpleMessage(bot, dialog, bot.getUnknownCommadMessage(), ParseMode.HTML);
			} else {
				sendMessageUtils.sendMessageWithOptions(bot, dialog, bot.getUnknownCommadMessage(), ParseMode.HTML, options);
			}
		}
	}

	private void setLastInteraction(Dialog dialog) {
		Calendar cal = Calendar.getInstance();
		dialog.setLastInteraction(cal.getTimeInMillis());
	}

	private void endOfCommand(Bot bot, Dialog dialog) {
		if (dialog.getLastCommand().getPostProcess()) {
			commandBusiness.postProcessCommand(dialog, dialog.getLastCommand());
			sendMessageUtils.sendSimpleMessage(bot, dialog, dialog.getPostProcessedResult(), dialog.getLastCommand().getParseMode());
		}
		List<List<String>> options = getAvailableCommands(bot);
		if (StringUtils.isNotBlank(bot.getEndOfDialogMessage())) {
			sendMessageUtils.sendMessageWithOptions(bot, dialog, bot.getEndOfDialogMessage(), ParseMode.HTML, options);
		}
		dialog.setPendingCommand(false);
		dialog.setPendingServer(false);
	}

	private void errorFormat(Bot bot, Dialog dialog) {
		if (StringUtils.isNotBlank(dialog.getLastQuestion().getErrorFormatMessage())) {
			sendMessageUtils.sendSimpleMessage(bot, dialog, dialog.getLastQuestion().getErrorFormatMessage(), ParseMode.HTML);
		}
	}

	private void successAnswer(Bot bot, Dialog dialog) {
		if (StringUtils.isNotBlank(dialog.getLastQuestion().getSuccessMessage())) {
			sendMessageUtils.sendSimpleMessage(bot, dialog, dialog.getLastQuestion().getSuccessMessage(), ParseMode.HTML);
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
