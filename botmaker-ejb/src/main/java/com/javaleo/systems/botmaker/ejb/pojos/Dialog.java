package com.javaleo.systems.botmaker.ejb.pojos;

import java.io.Serializable;
import java.util.List;

import org.javaleo.libs.botgram.model.Update;

import com.javaleo.systems.botmaker.ejb.entities.Bot;
import com.javaleo.systems.botmaker.ejb.entities.Command;

public class Dialog implements Serializable {

	private static final long serialVersionUID = 1L;

	private Update update;
	private Bot bot;
	private Command command;
	private boolean pendingServer;
	private boolean finish;
	private List<Answer> answers;

	public Update getUpdate() {
		return update;
	}

	public void setUpdate(Update update) {
		this.update = update;
	}

	public Bot getBot() {
		return bot;
	}

	public void setBot(Bot bot) {
		this.bot = bot;
	}

	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}

	public boolean isPendingServer() {
		return pendingServer;
	}

	public void setPendingServer(boolean pendingServer) {
		this.pendingServer = pendingServer;
	}

	public boolean isFinish() {
		return finish;
	}

	public void setFinish(boolean finish) {
		this.finish = finish;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

}
