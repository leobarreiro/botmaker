package org.javaleo.grandpa.web.action.price;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.grandpa.ejb.entities.price.Supplier;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.facades.IPriceFacade;
import org.javaleo.grandpa.web.action.AbstractCrudSinglePage;
import org.javaleo.grandpa.web.action.MsgAction;

@Named
@ConversationScoped
public class SupplierAction extends AbstractCrudSinglePage<Supplier> implements Serializable {

	private static final long serialVersionUID = -6731730184231110848L;

	private static final String THIS_PAGE = "/pages/price/suppliers.bot?faces-redirect=true";

	@Inject
	private Conversation conversation;

	@Inject
	private IPriceFacade facade;

	@Inject
	private MsgAction msgAction;

	private Supplier supplier;

	private List<Supplier> suppliers;

	@Override
	public String loadPage() {
		startConversation();
		startNew();
		searchOnlyActives();
		return THIS_PAGE;
	}

	@Override
	public void startNew() {
		this.supplier = new Supplier();
	}

	@Override
	public void searchAll() {
		this.suppliers = facade.listAllSuppliers();
	}

	@Override
	public void searchOnlyActives() {
		this.suppliers = facade.listAllActiveSuppliers();
	}

	@Override
	public void viewDetails(Supplier pojo) {
		this.supplier = pojo;
	}

	@Override
	public void startEdit(Supplier pojo) {
		this.supplier = pojo;
	}

	@Override
	public void deactivate(Supplier pojo) {
		this.supplier = pojo;
		try {
			facade.deactivateSupplier(supplier);
		} catch (BusinessException e) {
			msgAction.addErrorMessage(e.getMessage());
		}
		searchOnlyActives();
	}

	@Override
	public void save() {
		try {
			facade.saveSupplier(supplier);
		} catch (BusinessException e) {
			msgAction.addErrorMessage(e.getMessage());
		}
		searchOnlyActives();
	}

	@Override
	public Conversation getConversation() {
		return conversation;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public List<Supplier> getSuppliers() {
		return suppliers;
	}

	public void setSuppliers(List<Supplier> suppliers) {
		this.suppliers = suppliers;
	}

}
