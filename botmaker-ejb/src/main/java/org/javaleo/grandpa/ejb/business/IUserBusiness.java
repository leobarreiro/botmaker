package org.javaleo.grandpa.ejb.business;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import org.javaleo.grandpa.ejb.entities.User;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;

@Local
public interface IUserBusiness extends Serializable {

	void validateUser(User user, String password, String passwordReview) throws BusinessException;

	void saveUser(User user, String password, String passwordReview) throws BusinessException;

	User findUserByUsernameAndPassphrase(String username, String passphrase);

	User findUserByUsername(String username);

	void sendMessageRecoveryLoginToUser(String email, String emailReview) throws BusinessException;

	List<User> listAllUsers();

}
