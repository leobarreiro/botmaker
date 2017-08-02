package org.javaleo.grandpa.web.action.price;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.javaleo.grandpa.ejb.entities.price.Brand;
import org.javaleo.grandpa.ejb.exceptions.BusinessException;
import org.javaleo.grandpa.ejb.facades.IPriceFacade;
import org.javaleo.grandpa.web.action.AbstractCrudSinglePage;
import org.javaleo.grandpa.web.action.MsgAction;

@Named
@ConversationScoped
public class BrandAction extends AbstractCrudSinglePage<Brand> implements Serializable {

	private static final long serialVersionUID = -8295109693398196977L;

	private static final String THIS_PAGE = "/pages/price/brands.bot?faces-redirect=true";

	@Inject
	private Conversation conversation;

	@Inject
	private IPriceFacade facade;

	@Inject
	private MsgAction msgAction;

	@Override
	public Conversation getConversation() {
		return conversation;
	}

	private Brand brand;
	private List<Brand> brandList;

	@Override
	public String loadPage() {
		startConversation();
		return THIS_PAGE;
	}

	@Override
	public void startNew() {
		this.brand = new Brand();
	}

	@Override
	public void searchAll() {
		brandList = facade.listAllBrands();
	}

	@Override
	public void searchOnlyActives() {
		brandList = facade.listAllActiveBrands();
	}

	@Override
	public void viewDetails(Brand pojo) {
		this.brand = pojo;
	}

	@Override
	public void startEdit(Brand pojo) {
		this.brand = pojo;
	}

	@Override
	public void deactivate(Brand pojo) {
		this.brand = pojo;
		try {
			facade.deactivateBrand(brand);
		} catch (BusinessException e) {
			msgAction.addErrorMessage(e.getMessage());
		}
		searchOnlyActives();
	}

	@Override
	public void save() {
		try {
			facade.saveBrand(brand);
		} catch (BusinessException e) {
			msgAction.addErrorMessage(e.getMessage());
		}
		searchOnlyActives();
	}

	// Getters and Setters

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public List<Brand> getBrandList() {
		return brandList;
	}

	public void setBrandList(List<Brand> brandList) {
		this.brandList = brandList;
	}

}
