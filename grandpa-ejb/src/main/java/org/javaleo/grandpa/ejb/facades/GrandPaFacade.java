package org.javaleo.grandpa.ejb.facades;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.grandpa.ejb.business.ICompanyBusiness;
import org.javaleo.grandpa.ejb.business.ITokenBusiness;
import org.javaleo.grandpa.ejb.business.IUserBusiness;
import org.javaleo.grandpa.ejb.business.IUserPreferenceBusiness;
import org.javaleo.grandpa.ejb.business.blog.IBlogBusiness;
import org.javaleo.grandpa.ejb.business.blog.ICategoryBusiness;
import org.javaleo.grandpa.ejb.business.blog.IGalleryBusiness;
import org.javaleo.grandpa.ejb.business.blog.IPageBusiness;
import org.javaleo.grandpa.ejb.business.blog.IPhotoBusiness;
import org.javaleo.grandpa.ejb.business.bot.IBlackListExpressionBusiness;
import org.javaleo.grandpa.ejb.business.bot.IBotBusiness;
import org.javaleo.grandpa.ejb.business.bot.ICommandBusiness;
import org.javaleo.grandpa.ejb.business.bot.IDialogContextVarBusiness;
import org.javaleo.grandpa.ejb.business.bot.IQuestionBusiness;
import org.javaleo.grandpa.ejb.business.bot.IScriptBusiness;
import org.javaleo.grandpa.ejb.business.bot.IValidatorBusiness;
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

@Named
@Stateless
public class GrandPaFacade implements IGrandPaFacade {

	private static final long serialVersionUID = 1L;

	@Inject
	private ICompanyBusiness companyBusiness;

	@Inject
	private IBotBusiness botBusiness;

	@Inject
	private ICommandBusiness commandBusiness;

	@Inject
	private IQuestionBusiness questionBusiness;

	@Inject
	private IValidatorBusiness validatorBusiness;

	@Inject
	private IUserBusiness userBusiness;

	@Inject
	private IUserPreferenceBusiness userPreferenceBusiness;

	@Inject
	private IDialogContextVarBusiness dialogcontextVarBusiness;

	@Inject
	private IScriptBusiness scriptBusiness;

	@Inject
	private IBlackListExpressionBusiness blackListExpressionBusiness;

	@Inject
	private ITokenBusiness tokenBusiness;

	@Inject
	private IBlogBusiness blogBusiness;

	@Inject
	private IPageBusiness pageBusiness;

	@Inject
	private ICategoryBusiness categoryBusiness;

	@Inject
	private IGalleryBusiness galleryBusiness;

	@Inject
	private IPhotoBusiness photoBusiness;

	@Override
	public List<Company> listAllCompanies() {
		return companyBusiness.listAllCompanies();
	}

	@Override
	public void saveCompany(Company company) throws BusinessException {
		companyBusiness.saveCompany(company);
	}

	public void activateCompany(Company company) throws BusinessException {
		companyBusiness.activateCompany(company);
	}

	@Override
	public void deactivateCompany(Company company) throws BusinessException {
		companyBusiness.deactivateCompany(company);
	}

	@Override
	public Bot validateBotTelegram(String token) throws BusinessException {
		return botBusiness.validateBotTelegram(token);
	}

	@Override
	public void saveBot(Bot bot) throws BusinessException {
		botBusiness.saveBot(bot);
	}

	@Override
	public void deactivateBot(Bot bot) throws BusinessException {
		botBusiness.deactivateBot(bot);
	}

	@Override
	public void reactivateBot(Bot bot) throws BusinessException {
		botBusiness.reactivateBot(bot);
	}

	@Override
	public List<Bot> searchBot(BotFilter filter) {
		return botBusiness.searchBot(filter);
	}

	@Override
	public void validateUser(User user, String password, String passwordReview) throws BusinessException {
		userBusiness.validateUser(user, password, passwordReview);
	}

	@Override
	public void sendMessageToConfirmMailOwnership(User user) throws BusinessException {
		userBusiness.sendMessageToConfirmMailOwnership(user);
	}

	@Override
	public void saveUser(User user, String password, String passwordReview) throws BusinessException {
		userBusiness.saveUser(user, password, passwordReview);
	}

	@Override
	public void confirmUserRegistration(User user) throws BusinessException {
		userBusiness.confirmUserRegistration(user);
	}

	@Override
	public void sendMessageRecoveryLoginToUser(String email, String emailReview) throws BusinessException {
		userBusiness.sendMessageRecoveryLoginToUser(email, emailReview);
	}

	@Override
	public User findUserByUsernameAndPassword(String username, String passphrase) {
		return userBusiness.findUserByUsernameAndPassphrase(username, passphrase);
	}

	@Override
	public List<Bot> listLastBotsFromCompanyUser() {
		return botBusiness.listLastBotsFromCompanyUser();
	}

	@Override
	public Command getCommandById(Long id) {
		return commandBusiness.getCommandById(id);
	}

	@Override
	public List<Command> listCommandsByBot(Bot bot) {
		return commandBusiness.listCommandsByBot(bot);
	}

	@Override
	public void saveCommand(Command command) throws BusinessException {
		commandBusiness.saveCommand(command);
	}

	@Override
	public void dropCommand(Command command) {
		commandBusiness.dropCommand(command);
	}

	@Override
	public void saveQuestion(Question question) throws BusinessException {
		questionBusiness.saveQuestion(question);
	}

	@Override
	public void dropQuestion(Question question) throws BusinessException {
		questionBusiness.dropQuestion(question);
	}

	@Override
	public List<Question> listQuestionsFromCommand(Command command) {
		return questionBusiness.listQuestionsFromCommand(command);
	}

	@Override
	public Question getLastQuestionFromCommand(Command command) {
		return questionBusiness.getLastQuestionFromCommand(command);
	}

	@Override
	public void upQuestionOrder(Question question) throws BusinessException {
		questionBusiness.upQuestionOrder(question);
	}

	@Override
	public List<User> listAllUsers() {
		return userBusiness.listAllUsers();
	}

	@Override
	public void saveValidator(Validator validator) throws BusinessException {
		validatorBusiness.saveValidator(validator);
	}

	@Override
	public List<Validator> searchValidatorByFilter(ValidatorFilter filter) {
		return validatorBusiness.searchValidatorByFilter(filter);
	}

	@Override
	public List<UserPreference> listPreferencesByUser(User user) {
		return userPreferenceBusiness.listPreferencesByUser(user);
	}

	@Override
	public UserPreference getPreferenceByUserAndName(User user, String name) {
		return userPreferenceBusiness.getPreferenceByUserAndName(user, name);
	}

	@Override
	public void savePreference(User user, UserPreference userPreference) {
		userPreferenceBusiness.savePreference(user, userPreference);
	}

	@Override
	public void removePreference(UserPreference userPreference) {
		userPreferenceBusiness.removePreference(userPreference);
	}

	@Override
	public List<DialogContextVar> getListDialogContextVars() {
		return dialogcontextVarBusiness.getListDialogContextVars();
	}

	@Override
	public List<DialogContextVar> getContextVarsFromDialog(Dialog dialog) {
		return dialogcontextVarBusiness.getContextVarsFromDialog(dialog);
	}

	@Override
	public boolean isValidScript(Script script) throws BusinessException {
		return scriptBusiness.isValidScript(script);
	}

	@Override
	public String debugScript(Dialog dialog, Script script) {
		return scriptBusiness.debugScript(dialog, script);
	}

	@Override
	public String executeScript(Dialog dialog, Script script) throws BusinessException {
		return scriptBusiness.executeScript(dialog, script);
	}

	@Override
	public Boolean evaluateBooleanScript(Dialog dialog, Script script) throws BusinessException {
		return scriptBusiness.evaluateBooleanScript(dialog, script);
	}

	@Override
	public List<Script> listGenericScriptsFromScriptType(ScriptType scriptType) {
		return scriptBusiness.listGenericScriptsFromScriptType(scriptType);
	}

	@Override
	public List<Script> listLastGenericScriptsFromUser() {
		return scriptBusiness.listLastGenericScriptsFromUser();
	}

	@Override
	public List<Script> listLastCommandScriptsEditedByUser() {
		return scriptBusiness.listLastCommandScriptsEditedByUser();
	}

	@Override
	public List<Script> listAllGenericScriptsFromCompany() {
		return scriptBusiness.listAllGenericScriptsFromCompany();
	}

	@Override
	public Script getScriptToEdition(Long id) {
		return scriptBusiness.getScriptToEdition(id);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveScript(Script script) throws BusinessException {
		scriptBusiness.saveScript(script);
	}

	@Override
	public void saveBlackListExpression(BlackListExpression expression) throws BusinessException {
		blackListExpressionBusiness.saveBlackListExpression(expression);
	}

	@Override
	public List<BlackListExpression> listBlackListExpressionByScriptType(ScriptType scriptType) {
		return blackListExpressionBusiness.listBlackListExpressionByScriptType(scriptType);
	}

	@Override
	public void testScriptAgainstBlackListExpression(String scriptCode, ScriptType scriptType) throws BusinessException {
		blackListExpressionBusiness.testScriptAgainstBlackListExpression(scriptCode, scriptType);
	}

	@Override
	public void dropBlackListExpression(BlackListExpression expression) throws BusinessException {
		blackListExpressionBusiness.dropBlackListExpression(expression);
	}

	@Override
	public Token getTokenByUUID(String uuid) throws BusinessException {
		return tokenBusiness.getTokenByUUID(uuid);
	}

	@Override
	public void savePage(Page page) throws BusinessException {
		pageBusiness.savePage(page);
	}

	@Override
	public List<Page> listPages(PageFilter pageFilter) {
		return pageBusiness.listPages(pageFilter);
	}

	@Override
	public List<Page> listLastPagesEdited() {
		return pageBusiness.listLastPagesEdited();
	}

	@Override
	public void disablePage(Page page) throws BusinessException {
		pageBusiness.disablePage(page);
	}

	@Override
	public List<Blog> listBlogs() {
		return blogBusiness.listBlogs();
	}

	@Override
	public void saveBlog(Blog blog) throws BusinessException {
		blogBusiness.saveBlog(blog);
	}

	@Override
	public Blog getBlogFromKey(String key) {
		return blogBusiness.getBlogFromKey(key);
	}

	@Override
	public Blog getBlogFromId(Long id) {
		return blogBusiness.getBlogFromId(id);
	}

	@Override
	public List<Category> listAllCategories() {
		return categoryBusiness.listAllCategories();
	}

	@Override
	public List<Category> listAllCategoriesFromBlog(Blog blog) {
		return categoryBusiness.listAllCategoriesFromBlog(blog);
	}

	@Override
	public List<Category> listActiveCategoriesFromBlog(Blog blog) {
		return categoryBusiness.listActiveCategoriesFromBlog(blog);
	}

	@Override
	public void saveCategory(Category category) throws BusinessException {
		categoryBusiness.saveCategory(category);
	}

	@Override
	public void disableCategory(Category category) {
		categoryBusiness.disableCategory(category);
	}

	@Override
	public Category getFirstCategoryOptionfromBlog(Blog blog) {
		return categoryBusiness.getFirstCategoryOptionFromBlog(blog);
	}

	@Override
	public Gallery getGalleryByUuid(String uuid) {
		return galleryBusiness.getGalleryByUuid(uuid);
	}

	@Override
	public Gallery createGallery(String name) throws BusinessException {
		return galleryBusiness.createGallery(name);
	}

	@Override
	public void saveGallery(Gallery gallery) throws BusinessException {
		galleryBusiness.saveGallery(gallery);
	}

	@Override
	public boolean validateGallery(Gallery gallery) throws BusinessException {
		return galleryBusiness.validateGallery(gallery);
	}

	@Override
	public void deleteGallery(Gallery gallery) throws BusinessException {
		galleryBusiness.deleteGallery(gallery);
	}

	@Override
	public List<Gallery> searchGalleries(GalleryFilter galleryFilter) {
		return galleryBusiness.searchGalleries(galleryFilter);
	}

	@Override
	public boolean validatePhoto(Photo photo) throws BusinessException {
		return photoBusiness.validatePhoto(photo);
	}

	@Override
	public File getFileFromPhoto(Photo photo) throws BusinessException, IOException {
		return photoBusiness.getFileFromPhoto(photo);
	}

	@Override
	public File getFileThumbnailFromPhoto(Photo photo) throws BusinessException, IOException {
		return photoBusiness.getFileThumbnailFromPhoto(photo);
	}

	@Override
	public void savePhoto(Photo photo) throws BusinessException {
		photoBusiness.savePhoto(photo);
	}

	@Override
	public void deletePhoto(Photo photo) throws BusinessException {
		photoBusiness.deletePhoto(photo);
	}

	@Override
	public List<Photo> searchPhotos(PhotoFilter photoFilter) {
		return photoBusiness.searchPhotos(photoFilter);
	}

}
