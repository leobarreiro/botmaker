package org.javaleo.grandpa.ejb.facades;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import org.javaleo.grandpa.ejb.entities.Company;
import org.javaleo.grandpa.ejb.entities.Token;
import org.javaleo.grandpa.ejb.entities.User;
import org.javaleo.grandpa.ejb.entities.UserPreference;
import org.javaleo.grandpa.ejb.entities.blog.Blog;
import org.javaleo.grandpa.ejb.entities.blog.Category;
import org.javaleo.grandpa.ejb.entities.blog.Gallery;
import org.javaleo.grandpa.ejb.entities.blog.Page;
import org.javaleo.grandpa.ejb.entities.blog.Photo;
import org.javaleo.grandpa.ejb.entities.bot.BlackListExpression;
import org.javaleo.grandpa.ejb.entities.bot.Bot;
import org.javaleo.grandpa.ejb.entities.bot.Command;
import org.javaleo.grandpa.ejb.entities.bot.Question;
import org.javaleo.grandpa.ejb.entities.bot.Script;
import org.javaleo.grandpa.ejb.entities.bot.Validator;
import org.javaleo.grandpa.ejb.enums.ScriptType;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.filters.BotFilter;
import org.javaleo.grandpa.ejb.filters.GalleryFilter;
import org.javaleo.grandpa.ejb.filters.PageFilter;
import org.javaleo.grandpa.ejb.filters.PhotoFilter;
import org.javaleo.grandpa.ejb.filters.ValidatorFilter;
import org.javaleo.grandpa.ejb.pojos.Dialog;
import org.javaleo.grandpa.ejb.pojos.DialogContextVar;

@Local
public interface IGrandPaFacade extends Serializable {

	Bot validateBotTelegram(String token) throws BusinessException;

	void saveBot(Bot bot) throws BusinessException;

	void deactivateBot(Bot bot) throws BusinessException;

	void reactivateBot(Bot bot) throws BusinessException;

	List<Bot> searchBot(BotFilter filter);

	void validateUser(User user, String password, String passwordReview) throws BusinessException;

	void sendMessageToConfirmMailOwnership(User user) throws BusinessException;

	void saveUser(User user, String password, String passwordReview) throws BusinessException;

	void confirmUserRegistration(User user) throws BusinessException;

	void sendMessageRecoveryLoginToUser(String email, String emailReview) throws BusinessException;

	User findUserByUsernameAndPassword(String username, String passphrase);

	Command getCommandById(Long id);

	List<Command> listCommandsByBot(Bot bot);

	void saveCommand(Command command) throws BusinessException;

	void saveQuestion(Question question) throws BusinessException;

	void dropQuestion(Question question) throws BusinessException;

	void dropCommand(Command command);

	List<Question> listQuestionsFromCommand(Command command);

	Question getLastQuestionFromCommand(Command command);

	void upQuestionOrder(Question question) throws BusinessException;

	List<User> listAllUsers();

	List<Bot> listLastBotsFromCompanyUser();

	void saveValidator(Validator validator) throws BusinessException;

	List<Validator> searchValidatorByFilter(ValidatorFilter filter);

	List<UserPreference> listPreferencesByUser(User user);

	UserPreference getPreferenceByUserAndName(User user, String name);

	void savePreference(User user, UserPreference userPreference);

	void removePreference(UserPreference userPreference);

	List<Company> listAllCompanies();

	void saveCompany(Company company) throws BusinessException;

	void deactivateCompany(Company company) throws BusinessException;

	List<DialogContextVar> getListDialogContextVars();

	List<DialogContextVar> getContextVarsFromDialog(Dialog dialog);

	boolean isValidScript(Script script) throws BusinessException;

	String debugScript(Dialog dialog, Script script);

	String executeScript(Dialog dialog, Script script) throws BusinessException;

	Boolean evaluateBooleanScript(Dialog dialog, Script script) throws BusinessException;

	List<Script> listGenericScriptsFromScriptType(ScriptType scriptType);

	Script getScriptToEdition(Long id);

	void saveScript(Script script) throws BusinessException;

	void saveBlackListExpression(BlackListExpression expression) throws BusinessException;

	List<BlackListExpression> listBlackListExpressionByScriptType(ScriptType scriptType);

	void testScriptAgainstBlackListExpression(String scriptCode, ScriptType scriptType) throws BusinessException;

	void dropBlackListExpression(BlackListExpression expression) throws BusinessException;

	Token getTokenByUUID(String uuid) throws BusinessException;

	List<Script> listLastGenericScriptsFromUser();

	List<Script> listLastCommandScriptsEditedByUser();

	List<Script> listAllGenericScriptsFromCompany();

	void savePage(Page page) throws BusinessException;

	List<Page> listPages(PageFilter pageFilter);

	void disablePage(Page page) throws BusinessException;

	List<Page> listLastPagesEdited();

	List<Category> listAllCategories();

	List<Category> listAllCategoriesFromBlog(Blog blog);

	List<Category> listActiveCategoriesFromBlog(Blog blog);

	void saveCategory(Category category) throws BusinessException;

	void disableCategory(Category category);

	Category getFirstCategoryOptionfromBlog(Blog blog);

	List<Blog> listBlogs();

	void saveBlog(Blog blog) throws BusinessException;

	Blog getBlogFromKey(String key);

	Blog getBlogFromId(Long id);

	Gallery getGalleryByUuid(String uuid);

	Gallery createGallery(String name) throws BusinessException;

	void saveGallery(Gallery gallery) throws BusinessException;

	boolean validateGallery(Gallery gallery) throws BusinessException;

	void deleteGallery(Gallery gallery) throws BusinessException;

	List<Gallery> searchGalleries(GalleryFilter galleryFilter);

	boolean validatePhoto(Photo photo) throws BusinessException;

	File getFileFromPhoto(Photo photo) throws BusinessException, IOException;

	File getFileThumbnailFromPhoto(Photo photo) throws BusinessException, IOException;

	void savePhoto(Photo photo) throws BusinessException;

	void deletePhoto(Photo photo) throws BusinessException;

	List<Photo> searchPhotos(PhotoFilter photoFilter);

}