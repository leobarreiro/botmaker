package org.javaleo.grandpa.ejb.business.bot;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.javaleo.grandpa.ejb.business.ICompanyBusiness;
import org.javaleo.grandpa.ejb.entities.Company;
import org.javaleo.grandpa.ejb.entities.bot.Bot;
import org.javaleo.grandpa.ejb.entities.bot.Command;
import org.javaleo.grandpa.ejb.enums.BotType;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.filters.BotFilter;
import org.javaleo.grandpa.ejb.security.BotMakerCredentials;
import org.javaleo.libs.botgram.exceptions.BotGramException;
import org.javaleo.libs.botgram.model.User;
import org.javaleo.libs.botgram.response.GetMeResponse;
import org.javaleo.libs.botgram.service.BotGramConfig;
import org.javaleo.libs.botgram.service.IBotGramService;
import org.javaleo.libs.jee.core.persistence.IPersistenceBasic;

@Stateless
public class BotBusiness implements IBotBusiness {

	private static final long serialVersionUID = 1L;

	@Inject
	private ICompanyBusiness companyBusiness;

	@Inject
	private BotMakerCredentials credentials;

	@Inject
	private IPersistenceBasic<Bot> persistence;

	@Inject
	private IBotGramService botgramService;

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Bot validateBotTelegram(String token) throws BusinessException {
		if (StringUtils.isBlank(token)) {
			throw new BusinessException("Token não pode ser nulo ou estar em branco.");
		}
		Bot bot = new Bot();
		bot.setValid(false);
		BotGramConfig config = new BotGramConfig();
		config.setToken(token);
		botgramService.setConfiguration(config);
		try {
			GetMeResponse response = botgramService.getMe();
			if (!response.getOk()) {
				throw new BusinessException(response.getDescription());
			}
			User user = response.getUser();
			bot.setBotType(BotType.TELEGRAM);
			bot.setName(user.getFirstName());
			bot.setUsername(user.getUsername());
			bot.setToken(token);
			bot.setValid(true);
		} catch (BotGramException e) {
			throw new BusinessException("O Token informado não foi validado no Telegram.", e);
		}
		return bot;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveBot(Bot bot) throws BusinessException {
		Company company = companyBusiness.getCompanyById(credentials.getCompany().getId());
		bot.setCompany(company);
		persistence.saveOrUpdate(bot);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deactivateBot(Bot bot) throws BusinessException {
		bot.setActive(false);
		persistence.saveOrUpdate(bot);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void reactivateBot(Bot bot) throws BusinessException {
		bot.setActive(true);
		persistence.saveOrUpdate(bot);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Bot> searchBot(BotFilter filter) {
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<Bot> query = cb.createQuery(Bot.class);
		Root<Bot> from = query.from(Bot.class);
		Join<Bot, Company> joinCompany = from.join("company", JoinType.INNER);
		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(cb.equal(joinCompany.get("id"), credentials.getCompany().getId()));
		if (StringUtils.isNotBlank(filter.getName())) {
			Expression<String> path = from.get("name");
			predicates.add(cb.like(path, "%" + filter.getName() + "%"));
		}
		if (filter.getBotType() != null) {
			predicates.add(cb.equal(from.get("botType"), filter.getBotType()));
		}
		if (filter.getActive() != null) {
			predicates.add(cb.equal(from.get("active"), filter.getActive()));
		}
		query.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		return persistence.getResultList(query);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Bot> listLastBotsFromCompanyUser() {
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<Bot> cq = cb.createQuery(Bot.class);
		Root<Bot> from = cq.from(Bot.class);
		Join<Bot, Company> joinCompany = from.join("company", JoinType.INNER);
		Join<Bot, Command> joinCommand = from.join("commands", JoinType.LEFT);
		joinCommand.join("questions", JoinType.LEFT);
		cq.where(cb.equal(joinCompany.get("id"), credentials.getCompany().getId()));
		cq.orderBy(cb.desc(from.get("id")));
		return persistence.getResultList(cq);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Bot> listValidAndActiveBots(BotType botType) {
		CriteriaBuilder cb = persistence.getCriteriaBuilder();
		CriteriaQuery<Bot> cq = cb.createQuery(Bot.class);
		Root<Bot> from = cq.from(Bot.class);
		// Join<Bot, Company> joinCompany = from.join("company", JoinType.INNER);
		// Join<Command, Question> joinQuestion = joinCommand.join("questions", JoinType.LEFT);
		from.join("company", JoinType.INNER);
		Join<Bot, Command> joinCommand = from.join("commands", JoinType.LEFT);
		joinCommand.join("questions", JoinType.LEFT);
		cq.where(cb.and(cb.equal(from.get("botType"), botType), cb.equal(from.get("active"), true), cb.equal(from.get("valid"), true)));
		return persistence.getResultList(cq);
	}

}
