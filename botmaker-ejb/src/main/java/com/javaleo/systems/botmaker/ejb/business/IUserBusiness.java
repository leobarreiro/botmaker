package com.javaleo.systems.botmaker.ejb.business;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import com.javaleo.systems.botmaker.ejb.entities.User;
import com.javaleo.systems.botmaker.ejb.exceptions.BusinessException;

@Local
public interface IUserBusiness extends Serializable {

	void validateUser(User user, String password, String passwordReview) throws BusinessException;

	void saveUser(User user, String password, String passwordReview) throws BusinessException;

	User findUserByUsernameAndPassphrase(String username, String passphrase);

	List<User> listAllUsers();

}
