package org.javaleo.grandpa.web.action.price;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.grandpa.ejb.entities.price.Measure;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.facades.IPriceFacade;
import org.javaleo.grandpa.web.action.AbstractCrudSinglePage;
import org.javaleo.grandpa.web.action.MsgAction;

@Named
@ConversationScoped
public class MeasureAction extends AbstractCrudSinglePage<Measure> implements Serializable {

	private static final long serialVersionUID = -7849248458876308669L;

	private static final String THIS_PAGE = "/pages/price/measures.bot?faces-redirect=true";

	@Inject
	private Conversation conversation;

	@Inject
	private IPriceFacade facade;

	@Inject
	private MsgAction msgAction;

	private Measure measure;

	private List<Measure> measureList;

	@Override
	public String loadPage() {
		startConversation();
		startNew();
		searchOnlyActives();
		return THIS_PAGE;
	}

	@Override
	public void startNew() {
		this.measure = new Measure();
	}

	@Override
	public void viewDetails(Measure pojo) {
		this.measure = pojo;
	}

	@Override
	public void searchAll() {
		this.measureList = facade.listAllActiveMeasures();
	}

	@Override
	public void searchOnlyActives() {
		this.measureList = facade.listAllActiveMeasures();
	}

	@Override
	public void startEdit(Measure pojo) {
		this.measure = pojo;
	}

	@Override
	public void deactivate(Measure pojo) {
		this.measure = pojo;
		try {
			facade.deactivateMeasure(measure);
		} catch (BusinessException e) {
			msgAction.addErrorMessage(e.getMessage());
		}
		searchOnlyActives();
	}

	@Override
	public void save() {
		try {
			facade.saveMeasure(measure);
		} catch (BusinessException e) {
			msgAction.addErrorMessage(e.getMessage());
		}
		searchOnlyActives();
	}

	@Override
	public Conversation getConversation() {
		return conversation;
	}

	// Getters and Setters

	public Measure getMeasure() {
		return measure;
	}

	public void setMeasure(Measure measure) {
		this.measure = measure;
	}

	public List<Measure> getMeasureList() {
		return measureList;
	}

	public void setMeasureList(List<Measure> measureList) {
		this.measureList = measureList;
	}

}
