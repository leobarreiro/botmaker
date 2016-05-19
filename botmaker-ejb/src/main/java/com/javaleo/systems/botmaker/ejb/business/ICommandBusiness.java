package com.javaleo.systems.botmaker.ejb.business;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import org.javaleo.libs.botgram.enums.ParseMode;

import com.javaleo.systems.botmaker.ejb.entities.Bot;
import com.javaleo.systems.botmaker.ejb.entities.Command;
import com.javaleo.systems.botmaker.ejb.enums.ScriptType;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;
import com.javaleo.systems.botmaker.ejb.pojos.Dialog;

@Local
public interface ICommandBusiness extends Serializable {

	List<Command> listCommandsByBot(Bot bot);

	void saveCommand(Command command) throws BusinessException;
	
	void saveCommandPostScript(Long idCommand, String scriptCode, ParseMode parseMode, ScriptType scriptType) throws BusinessException;

	Command getCommandByBotAndKey(Bot bot, String text);

	List<List<String>> convertCommandsToOptions(Bot bot);

	void dropCommand(Command command);
	
	void postProcessCommand(Dialog dialog, Command command);

}
