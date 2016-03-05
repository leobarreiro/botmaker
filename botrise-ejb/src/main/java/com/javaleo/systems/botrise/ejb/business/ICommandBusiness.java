package com.javaleo.systems.botrise.ejb.business;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import com.javaleo.systems.botrise.ejb.entities.Bot;
import com.javaleo.systems.botrise.ejb.entities.Command;
import com.javaleo.systems.botrise.ejb.exceptions.BotRiseException;

@Local
public interface ICommandBusiness extends Serializable {

	List<Command> listCommandsByBot(Bot bot);

	void saveCommand(Command command) throws BotRiseException;

}
