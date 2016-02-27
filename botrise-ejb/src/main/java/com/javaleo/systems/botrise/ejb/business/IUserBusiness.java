package com.javaleo.systems.botrise.ejb.business;

import java.io.Serializable;

import javax.ejb.Local;

import com.javaleo.systems.botrise.ejb.entities.User;
import com.javaleo.systems.botrise.ejb.exceptions.BotRiseException;

@Local
public interface IUserBusiness extends Serializable {

	void saveUser(User user, String password) throws BotRiseException;

	User findUserByUsernameAndPassphrase(String username, String passphrase);

}
